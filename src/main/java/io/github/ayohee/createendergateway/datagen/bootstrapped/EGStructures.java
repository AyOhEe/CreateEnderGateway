package io.github.ayohee.createendergateway.datagen.bootstrapped;

import io.github.ayohee.createendergateway.register.EGTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructures {
    public static final ResourceKey<Structure> ABANDONED_STATION = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_station")
    );

    public static void bootstrap(BootstrapContext<Structure> ctx) {
        ctx.register(ABANDONED_STATION, new JigsawStructure(
                new Structure.StructureSettings.Builder(ctx.lookup(Registries.BIOME).getOrThrow(EGTags.HAS_ABANDONED_STATIONS))
                        .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(EGTemplatePools.ABANDONED_STATION_POOL),
                1,
                ConstantHeight.ZERO,
                false,
                Heightmap.Types.WORLD_SURFACE
        ));
    }
}
