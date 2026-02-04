package io.github.ayohee.createendergateway;

import io.github.ayohee.createendergateway.register.EGEntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CreateEnderGateway.MODID, dist = Dist.CLIENT)
//@EventBusSubscriber(modid = CreateEnderGateway.MODID, value = Dist.CLIENT)
public class CreateEnderGatewayClient {
    public CreateEnderGatewayClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
