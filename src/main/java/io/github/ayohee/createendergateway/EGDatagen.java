package io.github.ayohee.createendergateway;

import com.tterrag.registrate.providers.ProviderType;
import io.github.ayohee.createendergateway.content.ponder.EGPonderPlugin;
import io.github.ayohee.createendergateway.datagen.EGGeneratedEntriesProvider;
import io.github.ayohee.createendergateway.datagen.EGRecipeProvider;
import io.github.ayohee.createendergateway.datagen.recipes.EGMechanicalCraftingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGSequencedAssemblyRecipeGen;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;
import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(MODID))
            addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(MODID)) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();


        generator.addProvider(event.includeServer(), new EGMechanicalCraftingRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new EGSequencedAssemblyRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new EGGeneratedEntriesProvider(output, lookupProvider));

        System.out.println("Gathering data for Create: Ender Gateway");
        System.out.println(event.includeServer());
        if (event.includeServer()) {
            EGRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
        }
    }

    private static void addExtraRegistrateData() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            providePonderLang(provider::add);
        });
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new EGPonderPlugin());
        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }
}
