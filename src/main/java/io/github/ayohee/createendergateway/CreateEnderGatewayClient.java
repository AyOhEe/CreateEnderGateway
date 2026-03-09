package io.github.ayohee.createendergateway;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import io.github.ayohee.createendergateway.content.blockentityrenderer.GatewayBlockEntityRenderer;
import io.github.ayohee.createendergateway.content.itemrenderer.CustomRendererItemExtension;
import io.github.ayohee.createendergateway.content.itemrenderer.DimensionalTunerRenderer;
import io.github.ayohee.createendergateway.content.itemrenderer.GatewayPortalRenderer;
import io.github.ayohee.createendergateway.content.ponder.EGPonderPlugin;
import io.github.ayohee.createendergateway.register.EGBlockEntityTypes;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGEntityTypes;
import io.github.ayohee.createendergateway.register.EGItems;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CreateEnderGateway.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CreateEnderGateway.MODID, value = Dist.CLIENT)
public class CreateEnderGatewayClient {
    public CreateEnderGatewayClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        //FIXME this shouldn't be done this way. No clue why it wasn't working through registrate.
        BlockEntityRenderers.register(EGBlockEntityTypes.GATEWAY_PORTAL.get(), GatewayBlockEntityRenderer::new);


        PonderIndex.addPlugin(new EGPonderPlugin());
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                SimpleCustomRenderer.create(EGItems.DIMENSIONAL_TUNER.asItem(), new DimensionalTunerRenderer()),
                EGItems.DIMENSIONAL_TUNER
        );
        event.registerItem(
                new CustomRendererItemExtension(new GatewayPortalRenderer()),
                EGBlocks.GATEWAY_PORTAL.asItem()
        );
    }

    @SubscribeEvent
    static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EGEntityTypes.SYNTHETIC_EYE.get(), ctx -> new ThrownItemRenderer<>(ctx, 1.0f, true));
    }
}
