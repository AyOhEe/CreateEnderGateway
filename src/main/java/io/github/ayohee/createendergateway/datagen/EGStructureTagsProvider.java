package io.github.ayohee.createendergateway.datagen;

import io.github.ayohee.createendergateway.register.EGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class EGStructureTagsProvider extends TagsProvider<Structure> {
    public EGStructureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.STRUCTURE, provider, MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Create: Ender Gateway's Structure Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EGTags.SYNTHETIC_EYE_LOCATED).add(BuiltinStructures.STRONGHOLD);
    }
}
