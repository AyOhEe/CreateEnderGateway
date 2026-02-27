package io.github.ayohee.createendergateway.content.blockentity;

import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import io.github.ayohee.createendergateway.content.blocks.GatewayPortalBlock;
import io.github.ayohee.createendergateway.register.EGBlockEntityTypes;
import net.createmod.catnip.data.Pair;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GatewayBlockEntity extends BlockEntity implements IHaveHoveringInformation {
    private BlockPos linkedPos = null;

    public GatewayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean shouldRenderFace(Direction direction) {
        return getBlockState().getValue(BlockStateProperties.HORIZONTAL_AXIS) != direction.getAxis();
    }

    public boolean isLinked() {
        return linkedPos != null;
    }

    public BlockPos getLinkedPos() {
        return linkedPos;
    }

    public void link(BlockPos where) {
        if (linkedPos == null) {
            linkedPos = where;
            setChanged();
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void unlink() {
        linkedPos = null;
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void propagateUnlink() {
        Pair<BlockUtil.FoundRectangle, Direction.Axis> portalPair = GatewayPortalBlock.getPortalRect(getBlockPos(), getLevel());
        BlockUtil.FoundRectangle portalRect = portalPair.getFirst();
        Direction.Axis portalAxis = portalPair.getSecond();
        for (int i = 0; i < portalRect.axis1Size; i++) {
            for (int j = 0; j < portalRect.axis2Size; j++) {
                BlockPos currentPos = portalRect.minCorner.relative(Direction.Axis.Y, i).relative(portalAxis, j);
                getLevel().getBlockEntity(currentPos, EGBlockEntityTypes.GATEWAY_PORTAL.get()).ifPresent(GatewayBlockEntity::unlink);
            }
        }
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (isLinked()) {
            return false;
        }

        tooltip.add(
                Component.literal("    ")
                .append(Component.translatable("tooltip.createendergateway.gateway_not_linked"))
        );

        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (linkedPos != null) {
            tag.put("linked_pos", NbtUtils.writeBlockPos(linkedPos));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("linked_pos")) {
            linkedPos = NbtUtils.readBlockPos(tag, "linked_pos").get();
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
