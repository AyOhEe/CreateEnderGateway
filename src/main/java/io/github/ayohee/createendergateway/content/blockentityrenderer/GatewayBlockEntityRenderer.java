package io.github.ayohee.createendergateway.content.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

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

        if (blockEntity.getLevel() instanceof PonderLevel) {
            this.renderCube(blockEntity, poseStack.last().pose(), bufferSource.getBuffer(this.renderType()));
        } else {
            super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderCube(GatewayBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer) {
        float f = this.getOffsetDown();
        float f1 = this.getOffsetUp();
        this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(blockEntity, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(blockEntity, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(
            GatewayBlockEntity blockEntity,
            Matrix4f pose,
            VertexConsumer consumer,
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
        if (blockEntity.shouldRenderFace(direction)) {
            consumer.addVertex(pose, x0, y0, z0);
            consumer.addVertex(pose, x1, y0, z1);
            consumer.addVertex(pose, x1, y1, z2);
            consumer.addVertex(pose, x0, y1, z3);
        }
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
