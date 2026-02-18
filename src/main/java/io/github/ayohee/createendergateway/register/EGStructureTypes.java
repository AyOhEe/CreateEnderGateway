package io.github.ayohee.createendergateway.register;

import io.github.ayohee.createendergateway.foundation.structure.FixedOrientationJigsawStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EGStructureTypes {
    public static final DeferredHolder<StructureType<?>, StructureType<FixedOrientationJigsawStructure>> FIXED_ORIENTATION_JIGSAW = EGRegistries.STRUCTURE_TYPES.register(
            "fixed_orientation_jigsaw",
            () -> () -> FixedOrientationJigsawStructure.CODEC
    );

    public static void register() {}
}
