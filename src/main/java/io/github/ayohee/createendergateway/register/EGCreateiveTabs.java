package io.github.ayohee.createendergateway.register;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGCreateiveTabs {
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = EGRegistries.CREATIVE_MODE_TABS.register("main_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.createendergateway"))
            .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
            .icon(EGBlocks.MECHANICAL_GATEWAY::asStack)
            .displayItems((parameters, output) -> {
                for (RegistryEntry<Block, Block> entry : REGISTRATE.getAll(Registries.BLOCK)) {
                    if (!CreateRegistrate.isInCreativeTab(entry, EGCreateiveTabs.MAIN_TAB))
                        continue;
                    if (entry.get() instanceof LiquidBlock)
                        continue;

                    output.accept(entry.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
                for (RegistryEntry<Item, Item> entry : REGISTRATE.getAll(Registries.ITEM)) {
                    if (!CreateRegistrate.isInCreativeTab(entry, EGCreateiveTabs.MAIN_TAB))
                        continue;
                    output.accept(entry.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            })
            .build());


    public static void register() { }
}
