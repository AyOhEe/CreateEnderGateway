package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGFluids;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {
    public final GeneratedRecipe SYNTHETIC_EYE = create("synthetic_eye", b -> b
            .require(EGItems.SYNTHETIC_EYE_FRAGMENT)
            .transitionTo(EGItems.INCOMPLETE_SYNTHETIC_EYE)
            .addOutput(EGItems.SYNTHETIC_EYE, 100)
            .addStep(DeployerApplicationRecipe::new, r -> r.require(EGItems.AMETHYST_DUST))
            .addStep(DeployerApplicationRecipe::new, r -> r.require(EGItems.SYNTHETIC_EYE_FRAGMENT))
            .addStep(DeployerApplicationRecipe::new, r -> r.require(EGItems.ECHO_DUST))
            .loops(3)
    );

    public final GeneratedRecipe MECHANICAL_GATEWAY = create("mechanical_gateway", b -> b
            .require(Blocks.END_STONE_BRICKS)
            .transitionTo(EGItems.INCOMPLETE_MECHANICAL_GATEWAY)
            .addOutput(EGBlocks.MECHANICAL_GATEWAY, 80)
            .addOutput(Blocks.END_STONE, 10)
            .addOutput(AllItems.PRECISION_MECHANISM, 10)
            .addStep(FillingRecipe::new, r -> r.require(EGFluids.ACTIVE_ENDER_SOLUTION.get(), 250))
            .addStep(DeployerApplicationRecipe::new, r -> r.require(AllItems.PRECISION_MECHANISM))
            .addStep(DeployerApplicationRecipe::new, r -> r.require(AllBlocks.COGWHEEL))
            .addStep(DeployerApplicationRecipe::new, r -> r.require(Blocks.END_STONE))
            .addStep(PressingRecipe::new, r -> r)
            .addStep(PressingRecipe::new, r -> r)
            .loops(3)
    );

    public EGSequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
