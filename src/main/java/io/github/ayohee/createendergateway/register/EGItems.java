package io.github.ayohee.createendergateway.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.ayohee.createendergateway.content.items.DimensionalTunerItem;
import io.github.ayohee.createendergateway.content.items.SyntheticEyeItem;
import net.minecraft.world.item.Item;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGItems {
    static {
        REGISTRATE.setCreativeTab(EGCreativeTabs.MAIN_TAB);
    }

    public static final ItemEntry<SyntheticEyeItem> SYNTHETIC_EYE = REGISTRATE.item("synthetic_eye", SyntheticEyeItem::new)
            .register();

    public static final ItemEntry<DimensionalTunerItem> DIMENSIONAL_TUNER = REGISTRATE.item("dimensional_tuner", DimensionalTunerItem::new)
            .register();

    public static final ItemEntry<Item> AMETHYST_DUST = REGISTRATE.item("amethyst_dust", Item::new)
            .register();

    public static final ItemEntry<Item> ECHO_DUST = REGISTRATE.item("echo_dust", Item::new)
            .register();

    public static void register() { }
}
