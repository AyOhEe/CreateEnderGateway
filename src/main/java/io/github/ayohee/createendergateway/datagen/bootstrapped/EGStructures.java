package io.github.ayohee.createendergateway.datagen.bootstrapped;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructures {
    public static final ResourceKey<Structure> ABANDONED_STATION = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_station")
    );

    public static void bootstrap(BootstrapContext<Structure> ctx) {
        ctx.register(ABANDONED_STATION, new StrongholdStructure(
                new Structure.StructureSettings.Builder(ctx.lookup(Registries.BIOME).getOrThrow(BiomeTags.HAS_STRONGHOLD)).terrainAdapation(TerrainAdjustment.BURY).build()
        ));
    }
}
