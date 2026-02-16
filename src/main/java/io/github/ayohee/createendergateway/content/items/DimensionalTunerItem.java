package io.github.ayohee.createendergateway.content.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import io.github.ayohee.createendergateway.content.itemrenderer.DimensionalTunerRenderer;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

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

        context.getItemInHand().set(EGDataComponents.PORTAL_TUNING, new PortalTuning(where));
        return InteractionResult.SUCCESS;
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
