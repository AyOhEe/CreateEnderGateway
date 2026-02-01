package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {
    public final GeneratedRecipe DIMENSIONAL_TUNER = create(EGItems.DIMENSIONAL_TUNER::asItem).recipe(b -> b
            .key('E', Items.ECHO_SHARD)
            .key('A', Items.AMETHYST_SHARD)
            .key('B', AllItems.BRASS_SHEET)
            .key('C', AllBlocks.COGWHEEL)
            .key('S', Items.STICK)
            .patternLine("E A")
            .patternLine("E A")
            .patternLine("BBB")
            .patternLine(" C ")
            .patternLine(" S "));

    public EGMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
