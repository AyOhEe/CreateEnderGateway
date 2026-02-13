package io.github.ayohee.createendergateway.content.blocks;

import com.simibubi.create.AllShapes;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.ayohee.createendergateway.register.EGItems;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class VerticalGatewayBlock extends Block {
    private static final VoxelShape BASE_SHAPE = Block.box(0, 0, 3, 16, 16, 16);
    private static final VoxelShape EYE_SHAPE = Block.box(4, 4, 0, 12, 12, 3);
    public static final VoxelShaper VERTICAL_GATEWAY_SHAPE = new AllShapes.Builder(BASE_SHAPE).forDirectional(Direction.NORTH);
    public static final VoxelShaper FILLED_GATEWAY_SHAPE = new AllShapes.Builder(Shapes.or(EYE_SHAPE, BASE_SHAPE)).forDirectional(Direction.NORTH);

    // UP and DOWN signify that the front of the block (eye socket) is facing up/down. NORTH signifies that it is horizontal.
    public static final DirectionProperty BACK_ALIGNMENT = DirectionProperty.create("alignment", Direction.UP, Direction.DOWN, Direction.NORTH);

    public VerticalGatewayBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BACK_ALIGNMENT, Direction.NORTH)
                .setValue(BlockStateProperties.EYE, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_FACING, BACK_ALIGNMENT, BlockStateProperties.EYE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction socket = state.getValue(BACK_ALIGNMENT);
        Direction facing = socket != Direction.NORTH ? socket : state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        if (state.getValue(BlockStateProperties.EYE)) {
            return FILLED_GATEWAY_SHAPE.get(facing);
        } else {
            return VERTICAL_GATEWAY_SHAPE.get(facing);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction horizontal = context.getHorizontalDirection();
        Direction looking = context.getNearestLookingDirection();

        if (context.isSecondaryUseActive()) {
            horizontal = horizontal.getOpposite();
            looking = looking.getOpposite();
        }

        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, horizontal.getOpposite())
                .setValue(BACK_ALIGNMENT, looking.getAxis() == Direction.Axis.Y ? looking : Direction.NORTH);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return blockState.getValue(BlockStateProperties.EYE) ? 15 : 0;
    }

    //TODO fix
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public static void blockstate(DataGenContext<Block, VerticalGatewayBlock> ctx, RegistrateBlockstateProvider prov) {
        ResourceLocation baseModel = ResourceLocation.fromNamespaceAndPath(MODID, "block/" + ctx.getName());
        ResourceLocation filledModel = ResourceLocation.fromNamespaceAndPath(MODID, "block/" + ctx.getName() + "_filled");
        prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
            ResourceLocation model = state.getValue(BlockStateProperties.EYE) ? filledModel : baseModel;

            return ConfiguredModel.builder()
                    .modelFile(prov.models().getExistingFile(model))
                    .rotationY(getModelYRot(state))
                    .rotationX(getSocketRotation(state.getValue(BACK_ALIGNMENT)))
                    .build();
        });
    }

    private static int getModelYRot(BlockState state) {
        int desiredRot = (int)(state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180);
        desiredRot += state.getValue(BACK_ALIGNMENT) != Direction.NORTH ? 90 : 0;
        return desiredRot % 360;
    }

    private static int getSocketRotation(Direction value) {
        return switch (value) {
            case UP -> 90;
            case DOWN -> 270;
            default -> 0;
        };
    }

    public static int lightLevel(BlockState bs) {
        return bs.getValue(BlockStateProperties.EYE) ? 15 : 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(BlockStateProperties.EYE)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }


        if (stack.is(Items.ENDER_EYE)) {
            for (int i = 0; i < 10; i++){
                double shiftX = (double) pos.getX() + level.random.nextDouble() * 1.2;
                double shiftY = (double) pos.getY() + level.random.nextDouble() * 0.8;
                double shiftZ = (double) pos.getZ() + level.random.nextDouble() * 1.2;
                level.addParticle(ParticleTypes.SMOKE, shiftX, shiftY, shiftZ, 0.0, 0.1, 0.0);
            }

            level.playSound(player, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.2f, 0.5f);
            return ItemInteractionResult.SUCCESS;
        }

        if (!stack.is(EGItems.SYNTHETIC_EYE)) {
            return ItemInteractionResult.FAIL;
        }


        level.playSound(player, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!(level instanceof ServerLevel)) {
            return ItemInteractionResult.SUCCESS;
        }

        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.EYE, true));
        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
        }

        return ItemInteractionResult.SUCCESS;
    }
}
