package io.github.ayohee.createendergateway.register;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
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
            .properties(p -> p.stacksTo(16))
            .lang("Synthetic Eye")
            .register();

    public static final ItemEntry<DimensionalTunerItem> DIMENSIONAL_TUNER = REGISTRATE.item("dimensional_tuner", DimensionalTunerItem::new)
            .properties(p -> p.stacksTo(1))
            .lang("Dimensional Tuner")
            .register();

    public static final ItemEntry<Item> AMETHYST_DUST = REGISTRATE.item("amethyst_dust", Item::new)
            .lang("Amethyst Dust")
            .register();

    public static final ItemEntry<Item> ECHO_DUST = REGISTRATE.item("echo_dust", Item::new)
            .lang("Echo Dust")
            .register();

    public static final ItemEntry<Item> SATURATED_EYE_FRAGMENT = REGISTRATE.item("saturated_eye_fragment", Item::new)
            .lang("Saturated Eye Fragment")
            .register();

    public static final ItemEntry<Item> SYNTHETIC_EYE_FRAGMENT = REGISTRATE.item("synthetic_eye_fragment", Item::new)
            .lang("Synthetic Eye Fragment")
            .register();


    static {
        REGISTRATE.setCreativeTab(null);
    }

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SYNTHETIC_EYE = REGISTRATE.item("incomplete_synthetic_eye", SequencedAssemblyItem::new)
            .lang("Incomplete Synthetic Eye")
            .register();


    public static void register() { }
}
