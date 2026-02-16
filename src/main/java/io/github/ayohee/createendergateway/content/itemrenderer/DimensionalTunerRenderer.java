package io.github.ayohee.createendergateway.content.itemrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueHandler;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.ayohee.createendergateway.content.items.DimensionalTunerItem;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DimensionalTunerRenderer extends CustomRenderedItemModelRenderer {
    private static final PartialModel GEAR = PartialModel.of(Create.asResource("item/wrench/gear"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);

        ms.mulPose(Axis.YP.rotationDegrees(0));//DimensionalTunerItem.getCogRotation()));

        ms.translate(-1/16f, -1/32f, 0);
        renderer.render(GEAR.get(), light);
        ms.translate(1/16f, 1/32f, 0);
    }
}
