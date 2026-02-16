package io.github.ayohee.createendergateway.content.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import io.github.ayohee.createendergateway.content.blocks.GatewayPortalBlock;
import io.github.ayohee.createendergateway.content.itemrenderer.DimensionalTunerRenderer;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

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
        BlockPos where = context.getClickedPos();
        Level level = context.getLevel();

        if (!level.getBlockState(where).is(EGBlocks.GATEWAY_PORTAL)) {
            return InteractionResult.PASS;
        }

        ItemStack heldInHand = context.getItemInHand();
        if (isAlreadyTunedTo(heldInHand, where, level)) {
            context.getPlayer().displayClientMessage(Component.translatable("tooltip.createendergateway.already_tuned_to_portal").withColor(0xFF4040), true);
            return InteractionResult.FAIL;
        }

        if (heldInHand.has(EGDataComponents.PORTAL_TUNING)) {
            heldInHand.remove(EGDataComponents.PORTAL_TUNING);
            return InteractionResult.SUCCESS;
        }

        heldInHand.set(EGDataComponents.PORTAL_TUNING, new PortalTuning(where));
        return InteractionResult.SUCCESS;
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

    public record PortalTuning(BlockPos linkingFrom) {
        public static final Codec<PortalTuning> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos_from").forGetter(PortalTuning::linkingFrom))
        .apply(i, PortalTuning::new));

        public static final StreamCodec<ByteBuf, PortalTuning> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, PortalTuning::linkingFrom,
                PortalTuning::new
        );
    }
}
