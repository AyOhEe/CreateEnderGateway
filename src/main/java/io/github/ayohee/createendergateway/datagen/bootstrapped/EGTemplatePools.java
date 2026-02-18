package io.github.ayohee.createendergateway.datagen.bootstrapped;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGTemplatePools {
    public static final List<Pair<String, Integer>> OVERWORLD_STATIONS = List.of(
            Pair.of("overworld/abandoned_station_1", 1)
    );

    public static final List<Pair<String, Integer>> END_STATIONS = List.of(
            Pair.of("end/end_abandoned_station_1", 1)
    );

    public static final ResourceKey<StructureTemplatePool> OVERWORLD_ABANDONED_STATION_POOL = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            ResourceLocation.fromNamespaceAndPath(MODID, "overworld_abandoned_station_pool")
    );

    public static final ResourceKey<StructureTemplatePool> END_ABANDONED_STATION_POOL = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            ResourceLocation.fromNamespaceAndPath(MODID, "end_abandoned_station_pool")
    );

    public static void bootstrap(BootstrapContext<StructureTemplatePool> ctx) {
        ctx.register(OVERWORLD_ABANDONED_STATION_POOL,
                new StructureTemplatePool(ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY),
                        OVERWORLD_STATIONS.stream().map(EGTemplatePools::toPoolPair).toList()
                )
        );
        ctx.register(END_ABANDONED_STATION_POOL,
                new StructureTemplatePool(ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY),
                        END_STATIONS.stream().map(EGTemplatePools::toPoolPair).toList()
                )
        );
    }

    public static Pair<StructurePoolElement, Integer> toPoolPair(Pair<String, Integer> entry) {
        return Pair.of(
                StructurePoolElement.single(MODID + ":" + entry.getFirst()).apply(StructureTemplatePool.Projection.RIGID),
                entry.getSecond()
        );
    }
}
