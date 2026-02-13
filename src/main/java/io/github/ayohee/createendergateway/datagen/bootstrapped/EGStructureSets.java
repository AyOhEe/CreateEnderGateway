package io.github.ayohee.createendergateway.datagen.bootstrapped;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructureSets {
    public static final ResourceKey<StructureSet> ABANDONED_STATIONS = ResourceKey.create(
            Registries.STRUCTURE_SET,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_stations")
    );
    public static void bootstrap(BootstrapContext<StructureSet> ctx) {
        ctx.register(ABANDONED_STATIONS,
                new StructureSet(
                        List.of(new StructureSet.StructureSelectionEntry(ctx.lookup(Registries.STRUCTURE).getOrThrow(EGStructures.ABANDONED_STATION), 1)),
                        new RandomSpreadStructurePlacement(150, 50, RandomSpreadType.LINEAR, 1189370870)
                )
        );
    }
}
