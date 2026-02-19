package io.github.ayohee.createendergateway.content.blockentity;

import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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

    protected void link(BlockPos where) {
        linkedPos = where;
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
            CompoundTag asNBT = new CompoundTag();
            asNBT.putInt("X", linkedPos.getX());
            asNBT.putInt("Y", linkedPos.getY());
            asNBT.putInt("Z", linkedPos.getZ());
            tag.put("linked_pos", asNBT);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("linked_pos")) {
            linkedPos = NBTHelper.readBlockPos(tag, "linked_pos");
        }
    }
}
