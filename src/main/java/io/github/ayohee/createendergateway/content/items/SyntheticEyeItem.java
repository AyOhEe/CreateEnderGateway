package io.github.ayohee.createendergateway.content.items;

import io.github.ayohee.createendergateway.content.entity.SyntheticEyeEntity;
import io.github.ayohee.createendergateway.register.EGTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SyntheticEyeItem extends Item {
    public SyntheticEyeItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        BlockState hitBlockstate = level.getBlockState(blockhitresult.getBlockPos());
        if (blockhitresult.getType() == HitResult.Type.BLOCK && hitBlockstate.getTags().anyMatch(EGTags.GATEWAY_FRAME::equals)) {
            return InteractionResultHolder.pass(held);
        }

        player.startUsingItem(hand);
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.consume(held);
        }

        BlockPos gatewayPosition = serverLevel.findNearestMapStructure(EGTags.SYNTHETIC_EYE_LOCATED, player.blockPosition(), 100, false);
        if (gatewayPosition == null) {
            return InteractionResultHolder.consume(held);
        }

        SyntheticEyeEntity eyeEntity = new SyntheticEyeEntity(level, player.getX(), player.getY(0.5), player.getZ());
        eyeEntity.setItem(held);
        eyeEntity.signalTo(gatewayPosition);
        level.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeEntity.position(), GameEvent.Context.of(player));
        level.addFreshEntity(eyeEntity);
        if (player instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.USED_ENDER_EYE.trigger(serverplayer, gatewayPosition);
        }

        float pitch = Mth.lerp(level.random.nextFloat(), 0.33F, 0.5F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, pitch);
        held.consume(1, player);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.swing(hand, true);
        return InteractionResultHolder.success(held);

    }
}
