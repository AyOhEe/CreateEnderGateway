package io.github.ayohee.createendergateway.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGItems {
    static {
        REGISTRATE.setCreativeTab(EGCreativeTabs.MAIN_TAB);
    }

    public static final ItemEntry<Item> SYNTHETIC_EYE = REGISTRATE.item("synthetic_eye", Item::new)
            .register();

    public static final ItemEntry<Item> DIMENSIONAL_TUNER = REGISTRATE.item("dimensional_tuner", Item::new)
            .register();

    public static void register() { }
}
