package io.github.ayohee.createendergateway.register;


import io.github.ayohee.createendergateway.content.items.DimensionalTunerItem.PortalTuning;
import net.minecraft.core.component.DataComponentType;

import java.util.function.UnaryOperator;

public class EGDataComponents {
    public static final DataComponentType<PortalTuning> PORTAL_TUNING = register(
            "portal_tuning",
            builder -> builder.persistent(PortalTuning.CODEC).networkSynchronized(PortalTuning.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        EGRegistries.DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    public static void register() { }
}
