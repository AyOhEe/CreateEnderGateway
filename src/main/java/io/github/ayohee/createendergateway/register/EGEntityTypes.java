package io.github.ayohee.createendergateway.register;


import com.tterrag.registrate.util.entry.EntityEntry;

import io.github.ayohee.createendergateway.content.entity.SyntheticEyeEntity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;
import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGEntityTypes {
    public static final EntityEntry<SyntheticEyeEntity> SYNTHETIC_EYE = REGISTRATE
            .entity("synthetic_eye", (EntityType<SyntheticEyeEntity> t, Level l) -> new SyntheticEyeEntity(t, l), MobCategory.MISC)
            .properties(b -> b
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(4)
            ).renderer(() -> (ctx -> new ThrownItemRenderer<>(ctx, 1.0f, true)))
            .register();

    public static void register() { }
}
