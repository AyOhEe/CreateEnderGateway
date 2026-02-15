package io.github.ayohee.createendergateway.register;

import com.simibubi.create.api.contraption.ContraptionMovementSetting;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static io.github.ayohee.createendergateway.register.EGBlocks.*;

public class EGContraptionMovementSettings {
    private static final Supplier<?>[] unmovables = {
            GATEWAY_PORTAL,
            ABANDONED_GATEWAY,
            MECHANICAL_GATEWAY
    };

    public static void registerDefaults() {
        for (Supplier<?> b : unmovables) {
            ContraptionMovementSetting.REGISTRY.register(((Supplier<Block>)b).get(), () -> ContraptionMovementSetting.UNMOVABLE);
        }
    }
}
