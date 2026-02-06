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
    public SyntheticEyeEntity(EntityType<? extends SyntheticEyeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SyntheticEyeEntity(Level level, double x, double y, double z) {
        this(EGEntityTypes.SYNTHETIC_EYE.get(), level);
        this.setPos(x, y, z);
    }


    // Need to override these to use our getDefaultItem
    public void setItem(ItemStack stack) {
        if (stack.isEmpty()) {
            super.setItem(this.getDefaultItem());
        } else {
            super.setItem(stack);
        }
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
