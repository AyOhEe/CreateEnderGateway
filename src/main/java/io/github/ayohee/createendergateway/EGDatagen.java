package io.github.ayohee.createendergateway;

import io.github.ayohee.createendergateway.datagen.EGRecipeProvider;
import io.github.ayohee.createendergateway.datagen.recipes.EGMechanicalCraftingRecipeGen;
import io.github.ayohee.createendergateway.datagen.recipes.EGSequencedAssemblyRecipeGen;
import io.github.ayohee.createendergateway.register.EGStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
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


        generator.addProvider(event.includeServer(), new EGMechanicalCraftingRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new EGSequencedAssemblyRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider,
                new RegistrySetBuilder()
                        .add(Registries.STRUCTURE, bootstrap -> {
                            bootstrap.register(EGStructures.ABANDONED_STATION, new StrongholdStructure(
                                    new Structure.StructureSettings.Builder(bootstrap.lookup(Registries.BIOME).getOrThrow(BiomeTags.HAS_STRONGHOLD)).terrainAdapation(TerrainAdjustment.BURY).build()
                            ));
                        }),
                Set.of(MODID)
        ));


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
