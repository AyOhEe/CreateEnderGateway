package io.github.ayohee.createendergateway.register;

import io.github.ayohee.createendergateway.content.criterion.UsedSyntheticEyeTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGCriteriaTriggers {
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> USED_SYNTHETIC_EYE = register("used_synthetic_eye", UsedSyntheticEyeTrigger::new);

    public static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String name, Supplier<T> trigger) {
        return EGRegistries.TRIGGER_TYPES.register(name, trigger);
    }

    public static void register() { }
}
