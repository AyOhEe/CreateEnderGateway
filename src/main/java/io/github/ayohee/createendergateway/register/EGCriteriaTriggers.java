package io.github.ayohee.createendergateway.register;

import io.github.ayohee.createendergateway.content.criterion.ManualTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EGCriteriaTriggers {
    public static final DeferredHolder<CriterionTrigger<?>, ManualTrigger> USED_SYNTHETIC_EYE = register("use_synthetic_eye", ManualTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ManualTrigger> USED_WRONG_EYE = register("use_wrong_eye", ManualTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ManualTrigger> REPAIR_GATEWAY = register("repair_gateway", ManualTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ManualTrigger> LINK_GATEWAYS = register("link_gateways", ManualTrigger::new);

    public static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String name, Supplier<T> trigger) {
        return EGRegistries.TRIGGER_TYPES.register(name, trigger);
    }

    public static void register() { }
}
