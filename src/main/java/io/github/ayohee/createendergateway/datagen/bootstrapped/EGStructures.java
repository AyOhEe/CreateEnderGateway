package io.github.ayohee.createendergateway.datagen.bootstrapped;

import io.github.ayohee.createendergateway.foundation.structure.FixedOrientationJigsawStructure;
import io.github.ayohee.createendergateway.register.EGTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructures {
    public static final ResourceKey<Structure> ABANDONED_STATION_OVERWORLD = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_station_overworld")
    );

    public static final ResourceKey<Structure> ABANDONED_STATION_END = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_station_end")
    );

    public static void bootstrap(BootstrapContext<Structure> ctx) {
        ctx.register(ABANDONED_STATION_OVERWORLD, new FixedOrientationJigsawStructure(
                new Structure.StructureSettings.Builder(ctx.lookup(Registries.BIOME).getOrThrow(EGTags.HAS_ABANDONED_STATIONS_OVERWORLD))
                        .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(EGTemplatePools.OVERWORLD_ABANDONED_STATION_POOL),
                1,
                ConstantHeight.ZERO,
                Rotation.NONE,
                false,
                Heightmap.Types.WORLD_SURFACE
        ));

        ctx.register(ABANDONED_STATION_END, new FixedOrientationJigsawStructure(
                new Structure.StructureSettings.Builder(ctx.lookup(Registries.BIOME).getOrThrow(EGTags.HAS_ABANDONED_STATIONS_END))
                        .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                        .terrainAdapation(TerrainAdjustment.NONE)
                        .build(),
                ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(EGTemplatePools.END_ABANDONED_STATION_POOL),
                1,
                UniformHeight.of(VerticalAnchor.absolute(55), VerticalAnchor.absolute(80)),
                Rotation.NONE,
                false,
                Heightmap.Types.WORLD_SURFACE
        ));
    }
}
