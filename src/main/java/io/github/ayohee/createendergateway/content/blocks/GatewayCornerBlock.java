package io.github.ayohee.createendergateway.content.blocks;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class GatewayCornerBlock extends Block {
    public GatewayCornerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_AXIS, context.getHorizontalDirection().getClockWise().getAxis());
    }

    public static void blockstate(DataGenContext<Block, GatewayCornerBlock> ctx, RegistrateBlockstateProvider prov) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(MODID, "block/" + ctx.getName());
        prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
                    .modelFile(prov.models().cubeAll(ctx.getName(), texture))
                    .rotationY(state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X ? 0 : 90)
                    .build();
        });
    }
}
