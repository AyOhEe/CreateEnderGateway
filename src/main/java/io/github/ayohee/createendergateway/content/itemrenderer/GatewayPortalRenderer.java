package io.github.ayohee.createendergateway.content.itemrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import io.github.ayohee.createendergateway.content.blockentityrenderer.GatewayBlockEntityRenderer;
import net.irisshaders.iris.uniforms.SystemTimeUniforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class GatewayPortalRenderer extends BlockEntityWithoutLevelRenderer {
    public GatewayPortalRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.renderPortal(poseStack.last(), bufferSource.getBuffer(GatewayBlockEntityRenderer.getRenderType()), packedOverlay, packedLight);
    }

    private void renderPortal(PoseStack.Pose pose, VertexConsumer consumer, int overlay, int light)  {
        float offset = 0.375f;
        this.renderFace(pose, consumer, overlay, light, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F - offset, 1.0F - offset, 1.0F - offset, 1.0F - offset, Direction.SOUTH);
        this.renderFace(pose, consumer, overlay, light, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F + offset, 0.0F + offset, 0.0F + offset, 0.0F + offset, Direction.NORTH);
        this.renderFace(pose, consumer, overlay, light, 1.0F - offset, 1.0F - offset, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(pose, consumer, overlay, light, 0.0F + offset, 0.0F + offset, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
    }

    private void renderFace(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int overlay,
            int light,
            float x0,
            float x1,
            float y0,
            float y1,
            float z0,
            float z1,
            float z2,
            float z3,
            Direction direction
    ) {
        if (!GatewayBlockEntityRenderer.isUsingIris()) {
            consumer.addVertex(pose, x0, y0, z0);
            consumer.addVertex(pose, x1, y0, z1);
            consumer.addVertex(pose, x1, y1, z2);
            consumer.addVertex(pose, x0, y1, z3);
        } else {
            float progress = SystemTimeUniforms.TIMER.getFrameTimeCounter() * 0.01F % 1.0F;
            renderFaceIris(consumer, pose, direction, progress, overlay, light, x0, y0, z0, x1, y0, z1, x1, y1, z2, x0, y1, z3);
        }
    }

    private void renderFaceIris(VertexConsumer vertexConsumer, PoseStack.Pose pose, Direction direction, float progress, int overlay, int light, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        float nx = (float)direction.getStepX();
        float ny = (float)direction.getStepY();
        float nz = (float)direction.getStepZ();
        vertexConsumer.addVertex(pose, x1, y1, z1).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.0F + progress, 0.0F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vertexConsumer.addVertex(pose, x2, y2, z2).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.0F + progress, 0.2F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vertexConsumer.addVertex(pose, x3, y3, z3).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.2F + progress, 0.2F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vertexConsumer.addVertex(pose, x4, y4, z4).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.2F + progress, 0.0F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }
}
