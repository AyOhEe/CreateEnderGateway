package io.github.ayohee.createendergateway.register;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;
import static net.minecraft.core.registries.Registries.*;

public class EGRegistries {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ENTITY_TYPE, MODID);
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(TRIGGER_TYPE, MODID);

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        TRIGGER_TYPES.register(modEventBus);
    }
}
