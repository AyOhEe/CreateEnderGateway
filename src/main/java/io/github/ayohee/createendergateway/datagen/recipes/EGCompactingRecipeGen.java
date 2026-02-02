package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.ayohee.createendergateway.register.EGFluids;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGCompactingRecipeGen extends CompactingRecipeGen {
    public final GeneratedRecipe DORMANT_ENDER_FLUID = create("dormant_ender_fluid", b -> b
            .require(Items.ENDER_PEARL)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .output(EGFluids.DORMANT_ENDER_SOLUTION.get(), 1200)
    );

    public final GeneratedRecipe SYNTHETIC_EYE_FRAGMENT = create("synthetic_eye_fragment", b -> b
            .require(EGItems.SATURATED_EYE_FRAGMENT)
            .output(EGItems.SYNTHETIC_EYE_FRAGMENT)
            .output(EGFluids.DORMANT_ENDER_SOLUTION.get(), 50)
    );

    public EGCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
