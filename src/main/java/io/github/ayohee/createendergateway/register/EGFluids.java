package io.github.ayohee.createendergateway.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGFluids {
    static {
        REGISTRATE.setCreativeTab(EGCreativeTabs.MAIN_TAB);
    }

    public static final FluidEntry<BaseFlowingFluid.Flowing> DORMANT_ENDER_SOLUTION = REGISTRATE
            .standardFluid("dormant_ender_solution", SolidRenderedPlaceableFluidType.create(0x003627, () -> 1f / 8f))
            .tag(EGTags.DORMANT_ENDER_FLUID, AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .properties(b -> b.viscosity(1500)
                    .density(1200))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(8)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .renderType(() -> RenderType::solid)
            .source(BaseFlowingFluid.Source::new)
            .block()
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .build()
            .bucket()
            .tag(EGTags.DORMANT_ENDER_FLUID_BUCKET)
            .build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> ACTIVE_ENDER_SOLUTION = REGISTRATE
            .standardFluid("active_ender_solution", SolidRenderedPlaceableFluidType.create(0x003627, () -> 1f / 8f))
            .tag(EGTags.ACTIVE_ENDER_FLUID, AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .properties(b -> b.viscosity(1500)
                    .density(1200))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(8)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .renderType(() -> RenderType::solid)
            .source(BaseFlowingFluid.Source::new)
            .block()
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .build()
            .bucket()
            .tag(EGTags.ACTIVE_ENDER_FLUID_BUCKET)
            .build()
            .register();

    public static void register() { }



    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }
}
