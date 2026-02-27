package io.github.ayohee.createendergateway.content.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import io.github.ayohee.createendergateway.content.blocks.GatewayPortalBlock;
import io.github.ayohee.createendergateway.content.blocks.VerticalGatewayBlock;
import io.github.ayohee.createendergateway.content.itemrenderer.DimensionalTunerRenderer;
import io.github.ayohee.createendergateway.register.EGBlockEntityTypes;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGDataComponents;
import io.github.ayohee.createendergateway.register.EGPointsOfInterest;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.data.Pair;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import static io.github.ayohee.createendergateway.content.blocks.GatewayPortalBlock.getPortalRect;

public class DimensionalTunerItem extends Item {
    public DimensionalTunerItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DimensionalTunerRenderer()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player who = context.getPlayer();
        BlockPos where = context.getClickedPos();
        Level level = context.getLevel();
        if (who == null) {
            return InteractionResult.FAIL;
        }


        ItemStack heldInHand = context.getItemInHand();
        if (context.isSecondaryUseActive() && heldInHand.has(EGDataComponents.PORTAL_TUNING)) {
            heldInHand.remove(EGDataComponents.PORTAL_TUNING);
            level.playSound(who, who.blockPosition(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        if (!level.getBlockState(where).is(EGBlocks.GATEWAY_PORTAL)) {
            return InteractionResult.PASS;
        }

        if (((GatewayBlockEntity) level.getBlockEntity(where)).isLinked()) {
            who.displayClientMessage(Component.translatable("tooltip.createendergateway.portal_already_tuned").withColor(0xFF4040), true);
            return InteractionResult.FAIL;
        }

        if (isAlreadyTunedTo(heldInHand, where, level)) {
            who.displayClientMessage(Component.translatable("tooltip.createendergateway.already_tuned_to_portal").withColor(0xFF4040), true);
            return InteractionResult.FAIL;
        }

        if (isAlreadyTunedToDimension(heldInHand, level)) {
            who.displayClientMessage(Component.translatable("tooltip.createendergateway.already_tuned_to_dimension").withColor(0xFF4040), true);
            return InteractionResult.FAIL;
        }

        if (heldInHand.has(EGDataComponents.PORTAL_TUNING)) {
            PortalTuning tuning = heldInHand.get(EGDataComponents.PORTAL_TUNING);
            heldInHand.remove(EGDataComponents.PORTAL_TUNING);

            tryLinkPortals(level, who, where, tuning);

            return InteractionResult.SUCCESS;
        }

        heldInHand.set(EGDataComponents.PORTAL_TUNING, new PortalTuning(getPortalRect(where, level).getFirst().minCorner, level.dimension()));
        return InteractionResult.SUCCESS;
    }

    private void tryLinkPortals(Level level, Player who, BlockPos where, PortalTuning tuning) {
        if (!(level instanceof ServerLevel sLevel)) {
            return;
        }

        if (sLevel.getServer().getLevel(tuning.sourceDimension()).getBlockState(tuning.linkingFrom()).is(EGBlocks.GATEWAY_PORTAL)) {
            linkPortals(sLevel, getPortalRect(where, level).getFirst().minCorner, who, tuning);
        } else {
            who.displayClientMessage(Component.translatable("tooltip.createendergateway.source_portal_broken").withColor(0xFF4040), true);
        }
    }

    private void linkPortals(ServerLevel level, BlockPos where, Player who, PortalTuning tuning) {

        ServerLevel foreignLevel = level.getServer().getLevel(tuning.sourceDimension());
        BlockPos offset = tuning.linkingFrom().subtract(where);

        Pair<BlockUtil.FoundRectangle, Direction.Axis> localPair = getPortalRect(where, level);
        BlockUtil.FoundRectangle localRect = localPair.getFirst();
        Direction.Axis localAxis = localPair.getSecond();

        Pair<BlockUtil.FoundRectangle, Direction.Axis> foreignPair = getPortalRect(tuning.linkingFrom(), foreignLevel);
        BlockUtil.FoundRectangle foreignRect = foreignPair.getFirst();
        Direction.Axis foreignAxis = foreignPair.getSecond();

        // Local portal -> Foreign portal
        for (int i = 0; i < localRect.axis1Size; i++) {
           for (int j = 0; j < localRect.axis2Size; j++) {
               BlockPos localPos = localRect.minCorner.relative(Direction.Axis.Y, i).relative(localAxis, j);
               //Link Local -> Foreign
               linkPortalBlock(level, localPos, foreignLevel, offset);
           }
        }

        // Foreign portal -> Local portal
        for (int i = 0; i < foreignRect.axis1Size; i++) {
            for (int j = 0; j < foreignRect.axis2Size; j++) {
                BlockPos foreignPos = foreignRect.minCorner.relative(Direction.Axis.Y, i).relative(foreignAxis, j);
                //Link Foreign -> Local
                linkPortalBlock(foreignLevel, foreignPos, level, offset.multiply(-1));
            }
        }


        who.sendSystemMessage(Component.literal("Linking from [" + tuning.linkingFrom().toShortString() + " in " + tuning.sourceDimension().location() + "] to [" + where.toShortString() + " in " + level.dimension().location() + "]").withColor(0x40FF40));
    }

    private void linkPortalBlock(ServerLevel localLevel, BlockPos localPos, ServerLevel foreignLevel, Vec3i offset) {
        PoiManager poiManager = foreignLevel.getPoiManager();
        BlockPos checkOrigin = localPos.offset(offset);
        localLevel.getBlockEntity(localPos, EGBlockEntityTypes.GATEWAY_PORTAL.get()).ifPresent((be) -> {
            poiManager.ensureLoadedAndValid(foreignLevel, checkOrigin, VerticalGatewayBlock.MAX_FRAME_SIZE * 2);
            poiManager.findClosestWithType(EGPointsOfInterest.GATEWAY_PORTAL::equals, checkOrigin, VerticalGatewayBlock.MAX_FRAME_SIZE * 2, PoiManager.Occupancy.ANY).ifPresent(
                    p -> be.link(p.getSecond())
            );
        });
    }

    private boolean isAlreadyTunedToDimension(ItemStack heldInHand, Level level) {
        // If it isn't tuned, it can't already be linked
        if (!heldInHand.has(EGDataComponents.PORTAL_TUNING)) {
            return false;
        }

        return heldInHand.get(EGDataComponents.PORTAL_TUNING).sourceDimension.equals(level.dimension());
    }

    private boolean isAlreadyTunedTo(ItemStack heldInHand, BlockPos where, Level level) {
        // If it isn't tuned, it can't already be linked
        if (!heldInHand.has(EGDataComponents.PORTAL_TUNING)) {
            return false;
        }

        // Start searching at the current block
        Queue<BlockPos> checkQueue = new LinkedList<>();
        checkQueue.add(where);

        // Where are we looking for?
        BlockPos linkedPos = heldInHand.get(EGDataComponents.PORTAL_TUNING).linkingFrom();

        // Keep track of the blocks we've already checked - we can skip those when we see them again.
        Set<BlockPos> alreadySeen = new HashSet<>();
        while (!checkQueue.isEmpty()) {
            // Make sure we haven't already seen this one
            BlockPos checkPos = checkQueue.poll();
            if (alreadySeen.contains(checkPos)) {
                continue;
            }
            alreadySeen.add(checkPos); // And mark it as seen

            // Make sure it's actually a portal
            BlockState checkState = level.getBlockState(checkPos);
            if (!checkState.is(EGBlocks.GATEWAY_PORTAL)) {
                continue;
            }

            // Is it the portal we were looking for?
            if (checkPos.equals(linkedPos)) {
                return true;
            }

            // Nope - look at all of the neighbours
            checkQueue.addAll(GatewayPortalBlock.getNeighbours(level, checkPos));
        }

        // Didn't find it.
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(EGDataComponents.PORTAL_TUNING);
    }

    public record PortalTuning(BlockPos linkingFrom, ResourceKey<Level> sourceDimension) {
        static Codec<ResourceKey<Level>> CODEC_DIMENSION = ResourceKey.codec(Registries.DIMENSION);
        static StreamCodec<ByteBuf, ResourceKey<Level>> STREAM_CODEC_DIMENSION = ResourceKey.streamCodec(Registries.DIMENSION);

        public static final Codec<PortalTuning> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos_from").forGetter(PortalTuning::linkingFrom),
                CODEC_DIMENSION.fieldOf("sourceDimension").forGetter(PortalTuning::sourceDimension))
        .apply(i, PortalTuning::new));

        public static final StreamCodec<ByteBuf, PortalTuning> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, PortalTuning::linkingFrom,
                STREAM_CODEC_DIMENSION, PortalTuning::sourceDimension,
                PortalTuning::new
        );
    }
}
