package io.github.ayohee.createendergateway.datagen.recipes;

import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import io.github.ayohee.createendergateway.register.EGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGCrushingRecipeGen extends CrushingRecipeGen {
    public final GeneratedRecipe AMETHYST_DUST = create(
            "amethyst_crushing",
            b -> b.require(Items.AMETHYST_SHARD)
                    .output(EGItems.AMETHYST_DUST.asStack(3))
                    .output(0.5f, EGItems.AMETHYST_DUST.asStack())
    );

    public final GeneratedRecipe ECHO_DUST = create(
            "echo_crushing",
            b -> b.require(Items.ECHO_SHARD)
                    .output(EGItems.ECHO_DUST.asStack(12))
                    .output(0.5f, EGItems.ECHO_DUST.asStack(4))
    );

    public EGCrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MODID);
    }
}
