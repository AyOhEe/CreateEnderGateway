package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.ayohee.createendergateway.register.EGFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGMixingRecipeGen extends MixingRecipeGen {
    public final GeneratedRecipe ACTIVE_ENDER_SOLUTION = create("active_ender_solution", b -> b
            .require(EGFluids.DORMANT_ENDER_SOLUTION.get(), 1000)
            .require(Items.NETHER_WART)
            .require(Items.BLAZE_POWDER)
            .requiresHeat(HeatCondition.HEATED)
            .output(EGFluids.ACTIVE_ENDER_SOLUTION.get(), 1000)
    );

    public EGMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
