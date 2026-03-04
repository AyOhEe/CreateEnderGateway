package io.github.ayohee.createendergateway.content.itemrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
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
        Matrix4f matrix4f = poseStack.last().pose();
        this.renderPortal(matrix4f, bufferSource.getBuffer(RenderType.endPortal()));
    }

    private void renderPortal(Matrix4f pose, VertexConsumer consumer)  {
        float offset = 0.375f;
        this.renderFace(pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F - offset, 1.0F - offset, 1.0F - offset, 1.0F - offset, Direction.SOUTH);
        this.renderFace(pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F + offset, 0.0F + offset, 0.0F + offset, 0.0F + offset, Direction.NORTH);
        this.renderFace(pose, consumer, 1.0F - offset, 1.0F - offset, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(pose, consumer, 0.0F + offset, 0.0F + offset, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
    }

    private void renderFace(
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
        consumer.addVertex(pose, x0, y0, z0);
        consumer.addVertex(pose, x1, y0, z1);
        consumer.addVertex(pose, x1, y1, z2);
        consumer.addVertex(pose, x0, y1, z3);
    }
}
