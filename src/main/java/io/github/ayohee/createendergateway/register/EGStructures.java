package io.github.ayohee.createendergateway.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructures {
    public static final ResourceKey<Structure> ABANDONED_STATION = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "abandoned_station")
    );
}
