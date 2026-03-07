package io.github.ayohee.createendergateway.content.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;

public class GatewayBlockEntityRenderer extends TheEndPortalRenderer<GatewayBlockEntity> {
    public GatewayBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GatewayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.scale(
                blockEntity.shouldRenderFace(Direction.SOUTH) ? 1.0f : 0.25f,
                1.0f,
                blockEntity.shouldRenderFace(Direction.EAST) ? 1.0f : 0.25f
        );
        poseStack.translate(
                blockEntity.shouldRenderFace(Direction.SOUTH) ? 0f : 1.5f,
                0,
                blockEntity.shouldRenderFace(Direction.EAST) ? 0f : 1.5f
        );
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    protected float getOffsetUp() {
        return 1.0f;
    }

    @Override
    protected float getOffsetDown() {
        return 0;
    }
}
