package io.github.ayohee.createendergateway;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.ayohee.createendergateway.register.*;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.EventPriority;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(CreateEnderGateway.MODID)
public class CreateEnderGateway {
    public static final String MODID = "createendergateway";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            ).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    public static final Logger LOGGER = LogUtils.getLogger();


    public CreateEnderGateway(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(EventPriority.HIGHEST, EGDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, EGDatagen::gatherData);

        REGISTRATE.registerEventListeners(modEventBus);


        EGBlocks.register();
        EGBlockEntityTypes.register();
        EGItems.register();
        EGFluids.register();
        EGEntityTypes.register();
        EGCriteriaTriggers.register();
        EGTags.register();
        EGCreativeTabs.register();

        EGRegistries.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EGPortalTracks.register();
        });
    }
}
