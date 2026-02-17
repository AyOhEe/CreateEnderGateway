package io.github.ayohee.createendergateway.content.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGItems;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGPonderTags {
    public static final ResourceLocation ENDER_GATEWAY = ResourceLocation.fromNamespaceAndPath(MODID, "ender_gateway");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(ENDER_GATEWAY)
                .addToIndex()
                .item(EGItems.SYNTHETIC_EYE, true, false)
                .title("Create: Ender Gateway")
                .description("Train transport between the End and the Overworld")
                .register();

        HELPER.addToTag(ENDER_GATEWAY)
                .add(EGItems.SYNTHETIC_EYE)
                .add(EGBlocks.ABANDONED_GATEWAY)
                .add(EGBlocks.MECHANICAL_GATEWAY)
                .add(EGItems.DIMENSIONAL_TUNER);
    }
}
