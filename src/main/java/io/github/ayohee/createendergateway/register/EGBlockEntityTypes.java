package io.github.ayohee.createendergateway.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import io.github.ayohee.createendergateway.content.blockentityrenderer.GatewayBlockEntityRenderer;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGBlockEntityTypes {
    public static final BlockEntityEntry<GatewayBlockEntity> GATEWAY_PORTAL = REGISTRATE.blockEntity("gateway_portal", GatewayBlockEntity::new)
            .validBlocks(EGBlocks.GATEWAY_PORTAL)
            //FIXME this shouldn't be done this way. No clue why it wasn't working through registrate.
            //.renderer(() -> GatewayBlockEntityRenderer::new)
            .register();

    public static void register() {}
}
