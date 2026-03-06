package io.github.ayohee.createendergateway.content.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ayohee.createendergateway.compat.IrisCompat;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import net.irisshaders.iris.uniforms.SystemTimeUniforms;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.neoforged.fml.loading.LoadingModList;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GatewayBlockEntityRenderer implements BlockEntityRenderer<GatewayBlockEntity> {
    public GatewayBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(GatewayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.renderPortal(blockEntity, poseStack.last(), bufferSource.getBuffer(getRenderType()), packedOverlay, packedLight);
    }

    private void renderPortal(GatewayBlockEntity blockEntity, PoseStack.Pose pose, VertexConsumer consumer, int overlay, int light)  {
        float offset = 0.375f;
        this.renderFace(blockEntity, pose, consumer, overlay, light, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F - offset, 1.0F - offset, 1.0F - offset, 1.0F - offset, Direction.SOUTH);
        this.renderFace(blockEntity, pose, consumer, overlay, light, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F + offset, 0.0F + offset, 0.0F + offset, 0.0F + offset, Direction.NORTH);
        this.renderFace(blockEntity, pose, consumer, overlay, light, 1.0F - offset, 1.0F - offset, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(blockEntity, pose, consumer, overlay, light, 0.0F + offset, 0.0F + offset, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
    }

    private void renderFace(
            GatewayBlockEntity blockEntity,
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
        if (!isUsingIris()) {
            if (blockEntity.shouldRenderFace(direction)) {
                consumer.addVertex(pose, x0, y0, z0);
                consumer.addVertex(pose, x1, y0, z1);
                consumer.addVertex(pose, x1, y1, z2);
                consumer.addVertex(pose, x0, y1, z3);
            }
        } else {
            float progress = SystemTimeUniforms.TIMER.getFrameTimeCounter() * 0.01F % 1.0F;
            renderFaceIris(blockEntity, consumer, pose, direction, progress, overlay, light, x0, y0, z0, x1, y0, z1, x1, y1, z2, x0, y1, z3);
        }
    }

    private void renderFaceIris(GatewayBlockEntity entity, VertexConsumer vertexConsumer, PoseStack.Pose pose, Direction direction, float progress, int overlay, int light, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (entity.shouldRenderFace(direction)) {
            float nx = (float)direction.getStepX();
            float ny = (float)direction.getStepY();
            float nz = (float)direction.getStepZ();
            vertexConsumer.addVertex(pose, x1, y1, z1).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.0F + progress, 0.0F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
            vertexConsumer.addVertex(pose, x2, y2, z2).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.0F + progress, 0.2F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
            vertexConsumer.addVertex(pose, x3, y3, z3).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.2F + progress, 0.2F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
            vertexConsumer.addVertex(pose, x4, y4, z4).setColor(0.075F, 0.15F, 0.2F, 1.0F).setUv(0.2F + progress, 0.0F + progress).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        }
    }

    public static RenderType getRenderType() {
        return isUsingIris() ? RenderType.entitySolid(TheEndPortalRenderer.END_PORTAL_LOCATION) : RenderType.endPortal();
    }

    public static boolean isUsingIris() {
        return LoadingModList.get().getModFileById("iris") != null && IrisCompat.isUsingShaders();
    }
}
