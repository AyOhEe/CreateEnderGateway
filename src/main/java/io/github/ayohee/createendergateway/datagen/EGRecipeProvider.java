package io.github.ayohee.createendergateway.datagen;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGCompactingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGCrushingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGMixingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGPressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EGRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    public EGRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new EGCrushingRecipeGen(output, registries));
        GENERATORS.add(new EGMixingRecipeGen(output, registries));
        GENERATORS.add(new EGPressingRecipeGen(output, registries));
        GENERATORS.add(new EGCompactingRecipeGen(output, registries));

        gen.addProvider(true, new DataProvider() {
            @Override
            public @NotNull String getName() {
                return "Create: Ender Gateway's Processing Recipes";
            }
            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream().map(gen -> gen.run(dc)).toArray(CompletableFuture[]::new));
            }
        });
    }
}
