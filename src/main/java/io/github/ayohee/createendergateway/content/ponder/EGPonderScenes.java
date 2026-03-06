package io.github.ayohee.createendergateway.content.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class EGPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(EGBlocks.MECHANICAL_GATEWAY, EGBlocks.ABANDONED_GATEWAY)
                .addStoryBoard("abandoned_gateway", EGPonderScenes::abandonedGateway)
                .addStoryBoard("not_fully_mechanical", EGPonderScenes::notFullyMechanical);
        
        HELPER.forComponents(EGItems.DIMENSIONAL_TUNER)
                .addStoryBoard("linking_gateways", EGPonderScenes::dimensionalTuner)
                .addStoryBoard("train_travel", EGPonderScenes::trainTravel);
    }

    private static void abandonedGateway(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("abandoned_gateway", "Abandoned Portals");
        builder.configureBasePlate(0, 0, 7);
        builder.world().showSection(util.select().layer(0), Direction.UP);
        builder.addKeyframe();

        builder.idle(5);
        builder.world().showSection(util.select().position(1, 1, 3), Direction.DOWN);
        builder.world().showSection(util.select().position(5, 1, 3), Direction.DOWN);

        builder.idle(10);
        builder.world().showSection(util.select().position(1, 2, 3), Direction.DOWN);
        builder.world().showSection(util.select().fromTo(2, 1, 4, 4, 1, 3), Direction.DOWN);
        builder.world().showSection(util.select().position(5, 2, 3), Direction.DOWN);

        builder.idle(5);
        builder.world().showSection(util.select().position(5, 3, 3), Direction.DOWN);

        builder.idle(5);

        builder.overlay().showText(50)
                .colored(PonderPalette.WHITE)
                .text("In abandoned stations, damaged portals can be found")
                .attachKeyFrame();

        builder.idle(80);
        builder.overlay().showText(80)
                .colored(PonderPalette.GREEN)
                .text("These portals can be repaired with Mechanical Gateway Frames and Synthetic Eyes")
                .attachKeyFrame();

        builder.idle(30);
        builder.world().showSection(util.select().position(1, 4, 3), Direction.EAST);
        builder.world().showSection(util.select().position(1, 3, 3), Direction.EAST);
        builder.world().showSection(util.select().position(5, 4, 3), Direction.WEST);

        builder.idle(5);
        builder.world().showSection(util.select().fromTo(1, 5, 3, 5, 5, 3), Direction.DOWN);

        builder.idleSeconds(2);
        builder.world().showSection(util.select().fromTo(2, 2, 3, 4, 4, 3), Direction.NORTH);


        builder.effects().indicateSuccess(new BlockPos(3, 3, 4));
        builder.markAsFinished();
        builder.setNextUpEnabled(true);
    }

    private static void notFullyMechanical(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("not_fully_mechanical", "Construction Criteria");
        builder.configureBasePlate(0, 0, 7);
        builder.world().showSection(util.select().layer(0), Direction.UP);
        builder.addKeyframe();

        builder.idle(10);
        builder.world().showSection(util.select().fromTo(1, 1, 3, 5, 5, 3), Direction.NORTH);

        builder.overlay().showText(70)
                .colored(PonderPalette.WHITE)
                .text("At least one Abandoned Gateway Frame must be present to construct an End portal")
                .attachKeyFrame();


        builder.idle(90);
        builder.overlay().showText(70)
                .colored(PonderPalette.RED)
                .text("A portal constructed from ONLY Mechanical Gateway Frames will not light")
                .pointAt(util.vector().centerOf(3, 5, 3))
                .attachKeyFrame();

        builder.idle(20);
        builder.markAsFinished();
    }

    private static void dimensionalTuner(SceneBuilder builder, SceneBuildingUtil util) {
        builder.title("dimensional_tuner", "Linking Portals");
        builder.world().showSection(util.select().layer(0), Direction.UP);
        builder.configureBasePlate(0, 0, 10);
        builder.addKeyframe();

        builder.world().showSection(util.select().fromTo(1, 1, 8, 5, 1, 8), Direction.DOWN);
        builder.world().showSection(util.select().position(1, 2, 8), Direction.DOWN);
        builder.world().showSection(util.select().fromTo(5, 2, 8, 5, 3, 8), Direction.DOWN);


        builder.world().showSection(util.select().fromTo(8, 1, 1, 8, 1, 5), Direction.DOWN);
        builder.world().showSection(util.select().position(8, 2, 5), Direction.DOWN);
        builder.world().showSection(util.select().fromTo(8, 2, 1, 8, 3, 1), Direction.DOWN);


        builder.idle(15);
        builder.world().showSection(util.select().fromTo(1, 3, 8, 1, 4, 8), Direction.EAST);
        builder.world().showSection(util.select().position(5, 4, 8), Direction.WEST);

        builder.idle(10);
        builder.world().showSection(util.select().fromTo(1, 5, 8, 5, 5, 8), Direction.DOWN);

        builder.idle(20);
        builder.world().showSection(util.select().fromTo(2, 2, 8, 4, 4, 8), Direction.NORTH);

        builder.idle(15);
        builder.overlay().showText(70)
                .colored(PonderPalette.GREEN)
                .text("Once a portal has been repaired in the Overworld...")
                .independent(30)
                .attachKeyFrame();


        builder.idle(90);
        builder.world().showSection(util.select().position(8, 4, 1), Direction.SOUTH);
        builder.world().showSection(util.select().fromTo(8, 3, 5, 8, 4, 5), Direction.NORTH);

        builder.idle(10);
        builder.world().showSection(util.select().fromTo(8, 5, 1, 8, 5, 5), Direction.DOWN);

        builder.idle(20);
        builder.world().showSection(util.select().fromTo(8, 2, 2, 8, 4, 4), Direction.WEST);

        builder.idle(20);
        builder.overlay().showText(50)
                .colored(PonderPalette.BLUE)
                .text("And in the End...")
                .independent(30)
                .attachKeyFrame();

        builder.idle(70);
        builder.overlay().showText(70)
                .colored(PonderPalette.WHITE)
                .text("A Dimensional Tuner can be used to link them together")
                .independent(30)
                .attachKeyFrame();

        builder.idle(90);
        builder.overlay().showControls(new Vec3(5, 3, 8), Pointing.RIGHT, 30).rightClick()
                .withItem(new ItemStack(EGItems.DIMENSIONAL_TUNER.get()));

        builder.idle(40);
        builder.overlay().showControls(new Vec3(8, 3, 5), Pointing.LEFT, 30).rightClick()
                .withItem(new ItemStack(EGItems.DIMENSIONAL_TUNER.get()));

        builder.idle(30);
        builder.effects().indicateSuccess(new BlockPos(0, 50, 0)); // Offscreen
        builder.markAsFinished();
        builder.setNextUpEnabled(true);
    }

    private static void trainTravel(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("train_travel", "Traveling Through Portals");
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.world().showSection(util.select().fromTo(1, 1, 0, 5, 1, 5), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(5, 1, 5, 1, 5, 5), Direction.NORTH);
        scene.configureBasePlate(0, 0, 7);
        scene.addKeyframe();


        scene.idle(15);
        for (int i = 0; i <= 4; i++) {
            scene.world().showSection(util.select().position(3, 2, i), Direction.DOWN);
            scene.idle(2);
        }

        scene.idle(10);
        scene.overlay().showText(70)
                .colored(PonderPalette.WHITE)
                .text("Once two portals have been linked, trains can travel through them")
                .attachKeyFrame();
        scene.idle(80);

        BlockPos stationPos = new BlockPos(0, 1, 3);
        Selection train = util.select().fromTo(2, 3, 0, 4, 4, 4);

        scene.world().showSection(util.select().position(stationPos), Direction.DOWN);
        scene.idle(5);
        ElementLink<WorldSectionElement> trainElement = scene.world().showIndependentSection(train, Direction.DOWN);
        scene.idle(10);
        scene.world().animateTrainStation(stationPos, true);
        scene.idle(10);
        scene.addKeyframe();


        scene.idle(30);
        scene.world().moveSection(trainElement, util.vector().of(0, 0, 6), 30);
        scene.world().animateBogey(util.grid().at(3, 3, 2), -6f, 60);
        scene.world().animateTrainStation(stationPos, false);
        scene.idle(5);
        scene.world().hideIndependentSection(trainElement, null);
        scene.idle(25);

        scene.markAsFinished();
    }
}
