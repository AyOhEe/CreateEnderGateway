package io.github.ayohee.createendergateway;

import io.github.ayohee.createendergateway.datagen.EGRecipeProvider;
import io.github.ayohee.createendergateway.datagen.recipes.EGMechanicalCraftingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGSequencedAssemblyRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(MODID)) {
            addExtraRegistrateData();
        }
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(MODID)) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        //EIGeneratedEntriesProvider generatedEntriesProvider = new EIGeneratedEntriesProvider(output, lookupProvider);
        //lookupProvider = generatedEntriesProvider.getRegistryProvider();
        //generator.addProvider(event.includeServer(), generatedEntriesProvider);


        generator.addProvider(event.includeServer(), new EGMechanicalCraftingRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new EGSequencedAssemblyRecipeGen(output, lookupProvider));


        System.out.println("Gathering data for Create: Ender Gateway");
        System.out.println(event.includeServer());
        if (event.includeServer()) {
            EGRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
        }
    }

    private static void addExtraRegistrateData() {
        EGRegistrateTags.addGenerators();
    }
}
