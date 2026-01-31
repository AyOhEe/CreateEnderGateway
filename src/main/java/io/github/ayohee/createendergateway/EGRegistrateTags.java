package io.github.ayohee.createendergateway;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGRegistrateTags {
    public static void addGenerators() {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, EGRegistrateTags::genBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, EGRegistrateTags::genItemTags);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, EGRegistrateTags::genFluidTags);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, EGRegistrateTags::genEntityTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> blockIntrinsic) {
    }

    private static void genItemTags(RegistrateTagsProvider<Item> registrateItemTagsProvider) {
    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> fluidIntrinsic) {
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> entityTypeIntrinsic) {
    }
}
