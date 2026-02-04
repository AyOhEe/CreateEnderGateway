package io.github.ayohee.createendergateway.content.entity;

import io.github.ayohee.createendergateway.register.EGEntityTypes;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SyntheticEyeEntity extends EyeOfEnder {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SyntheticEyeEntity.class, EntityDataSerializers.ITEM_STACK);


    public SyntheticEyeEntity(EntityType<? extends SyntheticEyeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SyntheticEyeEntity(Level level, double x, double y, double z) {
        this(EGEntityTypes.SYNTHETIC_EYE.get(), level);
        this.setPos(x, y, z);
    }


    // Need to override these to use our EntityDataAccessor and getDefaultItem
    public void setItem(ItemStack stack) {
        if (stack.isEmpty()) {
            this.getEntityData().set(DATA_ITEM_STACK, this.getDefaultItem());
        } else {
            this.getEntityData().set(DATA_ITEM_STACK, stack.copyWithCount(1));
        }
    }

    @Override
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM_STACK, this.getDefaultItem());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Item", 10)) {
            this.setItem(ItemStack.parse(this.registryAccess(), compound.getCompound("Item")).orElse(this.getDefaultItem()));
        } else {
            this.setItem(this.getDefaultItem());
        }
    }

    private ItemStack getDefaultItem() {
        return new ItemStack(EGItems.SYNTHETIC_EYE.get());
    }
}
