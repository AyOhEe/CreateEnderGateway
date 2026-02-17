package io.github.ayohee.createendergateway.content.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGItems;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class EGPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(EGBlocks.MECHANICAL_GATEWAY)
                .addStoryBoard("mechanical_gateway", EGPonderScenes::mechanicalGateway);
        HELPER.forComponents(EGBlocks.ABANDONED_GATEWAY)
                .addStoryBoard("abandoned_gateway", EGPonderScenes::abandonedGateway);
        
        HELPER.forComponents(EGItems.SYNTHETIC_EYE)
                .addStoryBoard("synthetic_eye", EGPonderScenes::syntheticEye);
        
        HELPER.forComponents(EGItems.DIMENSIONAL_TUNER)
                .addStoryBoard("dimensional_tuner", EGPonderScenes::dimensionalTuner);
    }

    private static void mechanicalGateway(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("mechanical_gateway", "Repairing Portals");
        builder.world().showSection(util.select().layer(0), Direction.UP);
        builder.configureBasePlate(0, 0, 5);

        builder.markAsFinished();
    }

    private static void abandonedGateway(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("abandoned_gateway", "Abandoned Portals");
        builder.world().showSection(util.select().layer(0), Direction.UP);
        builder.configureBasePlate(0, 0, 5);

        builder.markAsFinished();
    }

    private static void syntheticEye(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("synthetic_eye", "Finding Abandoned Stations");
        builder.configureBasePlate(0, 0, 5);
        builder.world().showSection(util.select().layer(0), Direction.UP);

        builder.markAsFinished();
    }

    private static void dimensionalTuner(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("dimensional_tuner", "Linking Portals");
        builder.configureBasePlate(0, 0, 5);
        builder.world().showSection(util.select().layer(0), Direction.UP);

        builder.markAsFinished();
    }
}
