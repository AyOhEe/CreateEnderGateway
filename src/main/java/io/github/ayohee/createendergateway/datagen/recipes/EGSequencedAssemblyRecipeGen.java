package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

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

    public EGSequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
