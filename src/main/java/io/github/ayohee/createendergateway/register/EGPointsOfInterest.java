package io.github.ayohee.createendergateway.register;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EGPointsOfInterest {
    public static final DeferredHolder<PoiType, PoiType> GATEWAY_PORTAL = EGRegistries.POINT_OF_INTEREST_TYPES.register(
            "gateway_portal",
            () -> new PoiType(ImmutableSet.copyOf(EGBlocks.GATEWAY_PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1)
    );

    public static void register() { }
}
