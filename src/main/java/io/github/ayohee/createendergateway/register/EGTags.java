package io.github.ayohee.createendergateway.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGTags {
    public static final TagKey<Fluid> DORMANT_ENDER_FLUID = modFluidTag("dormant_ender_fluid");
    public static final TagKey<Item> DORMANT_ENDER_FLUID_BUCKET = commonItemTag("buckets/dormant_ender_fluid");
    public static final TagKey<Fluid> ACTIVE_ENDER_FLUID = modFluidTag("active_ender_fluid");
    public static final TagKey<Item> ACTIVE_ENDER_FLUID_BUCKET = commonItemTag("buckets/active_ender_fluid");

    public static final TagKey<Block> GATEWAY_FRAME = modBlockTag("gateway_frame");
    public static final TagKey<Structure> SYNTHETIC_EYE_LOCATED = modStructureTag("synthetic_eye_located");

    public static TagKey<Fluid> modFluidTag(String name) {
        return TagKey.create(BuiltInRegistries.FLUID.key(), ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static TagKey<Item> commonItemTag(String name) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static TagKey<Block> modBlockTag(String name) {
        return TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static TagKey<Structure> modStructureTag(String name) {
        return TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void register() { }
}
