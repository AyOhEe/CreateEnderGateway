package io.github.ayohee.createendergateway.content.blocks;

import com.simibubi.create.AllShapes;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class VerticalGatewayBlock extends Block {
    public static final VoxelShaper VERTICAL_GATEWAY_SHAPE = new AllShapes.Builder(Block.box(0, 0, 3, 16, 16, 16)).forHorizontal(Direction.NORTH);


    public VerticalGatewayBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return VERTICAL_GATEWAY_SHAPE.get(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VERTICAL_GATEWAY_SHAPE.get(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    public static void blockstate(DataGenContext<Block, VerticalGatewayBlock> ctx, RegistrateBlockstateProvider prov) {
            ResourceLocation model = ResourceLocation.fromNamespaceAndPath(MODID, "block/" + ctx.getName());
            prov.horizontalBlock(ctx.get(), prov.models().getExistingFile(model));
    }

    public static int lightLevel(BlockState bs) {
        //TODO filled blocks should emit light
        return 0;
    }
}
