package io.github.ayohee.createendergateway.content.itemrenderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class CustomRendererItemExtension implements IClientItemExtensions {
    BlockEntityWithoutLevelRenderer renderer;

    public CustomRendererItemExtension(BlockEntityWithoutLevelRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
