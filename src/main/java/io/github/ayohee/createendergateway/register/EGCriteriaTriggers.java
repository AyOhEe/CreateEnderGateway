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
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> USED_SYNTHETIC_EYE = register("use_synthetic_eye", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> USED_WRONG_EYE = register("use_wrong_eye", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> FIND_STATION = register("find_station", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> REPAIR_GATEWAY = register("repair_gateway", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> LINK_GATEWAYS = register("link_gateways", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> TRAVEL_WITH_TRAIN = register("travel_with_train", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> TRAVEL_10K = register("travel_10k", UsedSyntheticEyeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, UsedSyntheticEyeTrigger> TRAVEL_100K = register("travel_100k", UsedSyntheticEyeTrigger::new);

    public static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String name, Supplier<T> trigger) {
        return EGRegistries.TRIGGER_TYPES.register(name, trigger);
    }

    public static void register() { }
}
