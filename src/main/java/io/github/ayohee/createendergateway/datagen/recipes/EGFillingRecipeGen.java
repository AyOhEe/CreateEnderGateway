package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGFillingRecipeGen extends FillingRecipeGen {
    public final GeneratedRecipe GATEWAY_CORNER = create("gateway_corner", b -> b
            .require(Blocks.CUT_COPPER)
            .require(EGFluids.DORMANT_ENDER_SOLUTION.get(), 100)
            .output(EGBlocks.GATEWAY_CORNER)
    );

    public EGFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
