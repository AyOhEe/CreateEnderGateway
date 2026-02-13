package io.github.ayohee.createendergateway.datagen;

import io.github.ayohee.createendergateway.datagen.bootstrapped.EGStructureSets;
import io.github.ayohee.createendergateway.datagen.bootstrapped.EGStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.STRUCTURE, EGStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, EGStructureSets::bootstrap);

    public EGGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MODID));
    }

    @Override
    public String getName() {
        return "Create: Ender Gateway's Generated Registry Entries";
    }
}
