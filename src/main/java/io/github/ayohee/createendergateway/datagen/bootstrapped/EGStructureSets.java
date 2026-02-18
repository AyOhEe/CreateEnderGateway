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
    public static final ResourceKey<StructureSet> ABANDONED_STATIONS_OVERWORLD = ResourceKey.create(
            Registries.STRUCTURE_SET,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_stations_overworld")
    );
    public static final ResourceKey<StructureSet> ABANDONED_STATIONS_END = ResourceKey.create(
            Registries.STRUCTURE_SET,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_stations_end")
    );

    public static void bootstrap(BootstrapContext<StructureSet> ctx) {
        ctx.register(ABANDONED_STATIONS_OVERWORLD,
                new StructureSet(
                        List.of(new StructureSet.StructureSelectionEntry(ctx.lookup(Registries.STRUCTURE).getOrThrow(EGStructures.ABANDONED_STATION_OVERWORLD), 1)),
                        new RandomSpreadStructurePlacement(75, 30, RandomSpreadType.LINEAR, 1189370870)
                )
        );
        ctx.register(ABANDONED_STATIONS_END,
                new StructureSet(
                        List.of(new StructureSet.StructureSelectionEntry(ctx.lookup(Registries.STRUCTURE).getOrThrow(EGStructures.ABANDONED_STATION_END), 1)),
                        new RandomSpreadStructurePlacement(50, 30, RandomSpreadType.LINEAR, 1189370870)
                )
        );
    }
}
