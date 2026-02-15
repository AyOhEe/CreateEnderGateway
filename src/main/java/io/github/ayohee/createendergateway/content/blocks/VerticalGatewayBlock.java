package io.github.ayohee.createendergateway.content.blocks;

import com.simibubi.create.AllShapes;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.ayohee.createendergateway.register.EGBlocks;
import io.github.ayohee.createendergateway.register.EGItems;
import io.github.ayohee.createendergateway.register.EGTags;
import net.createmod.catnip.data.Pair;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class VerticalGatewayBlock extends Block {
    private static final VoxelShape BASE_SHAPE = Block.box(0, 0, 3, 16, 16, 16);
    private static final VoxelShape EYE_SHAPE = Block.box(4, 4, 0, 12, 12, 3);
    public static final VoxelShaper VERTICAL_GATEWAY_SHAPE = new AllShapes.Builder(BASE_SHAPE).forDirectional(Direction.NORTH);
    public static final VoxelShaper FILLED_GATEWAY_SHAPE = new AllShapes.Builder(Shapes.or(EYE_SHAPE, BASE_SHAPE)).forDirectional(Direction.NORTH);

    public static final int MAX_FRAME_SIZE = 16;
    public static final int MIN_FRAME_SIZE = 5;

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

        if (looking.getAxis() == Direction.Axis.Y) {
            horizontal = horizontal.getClockWise();
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        Direction neighbourDir = state.getValue(BACK_ALIGNMENT);
        if (neighbourDir == Direction.NORTH) {
            neighbourDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }

        BlockPos neighbourPos = pos.relative(neighbourDir);
        BlockState neighbourState = level.getBlockState(neighbourPos);

        if (neighbourState.is(EGBlocks.GATEWAY_PORTAL.get())) {
            level.setBlock(neighbourPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
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

        checkForPortalFormation(level, pos);

        return ItemInteractionResult.SUCCESS;
    }

    private static boolean isFrame(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos).is(EGTags.GATEWAY_FRAME);
    }

    private static boolean isFrame(BlockState state) {
        return state.is(EGTags.GATEWAY_FRAME);
    }

    private void checkForPortalFormation(LevelAccessor level, BlockPos pos) {
        // Find the corners
        Pair<Direction, Direction.Axis> alignmentPair = getPortalAlignment(level, pos);
        Pair<BlockPos, BlockPos> corners = findPortalCorners(level, pos, alignmentPair.getFirst(), alignmentPair.getSecond());
        if (corners == null) {
            return;
        }
        BlockPos minCorner = corners.getFirst();
        BlockPos maxCorner = corners.getSecond();


        Direction.Axis portalAxis = (minCorner.getX() == maxCorner.getX()) ? Direction.Axis.Z : Direction.Axis.X;
        int width = maxCorner.get(portalAxis) - minCorner.get(portalAxis) + 1;
        int height = maxCorner.getY() - minCorner.getY() + 1;

        if (width < MIN_FRAME_SIZE || width > MAX_FRAME_SIZE || height < MIN_FRAME_SIZE || height > MAX_FRAME_SIZE) {
            return;
        }


        // Check that the inside is only air
        Direction posHorizontal = Direction.get(Direction.AxisDirection.POSITIVE, portalAxis);
        Direction negHorizontal = Direction.get(Direction.AxisDirection.NEGATIVE, portalAxis);
        BlockPos insideCorner = minCorner.above().relative(posHorizontal);
        for (BlockPos alongHorizontal : forBlockAlongDirection(insideCorner, posHorizontal, width - 2)) {
            for (BlockPos checkPos : forBlockAlongDirection(alongHorizontal, Direction.UP, height - 2)) {
                BlockState bs = level.getBlockState(checkPos);
                if (!bs.is(Blocks.AIR)) {
                    return;
                }
            }
        }


        // Check each side
        Direction forward = posHorizontal.getClockWise();
        Direction backward = negHorizontal.getClockWise();
        Predicate<BlockState> topFrameCheck = (s) -> checkFrameBlockState(s, Direction.DOWN, List.of(backward, forward));
        Predicate<BlockState> bottomFrameCheck = (s) -> checkFrameBlockState(s, Direction.UP, List.of(backward, forward));
        Predicate<BlockState> minSideFrameCheck = (s) -> checkFrameBlockState(s, Direction.NORTH, List.of(negHorizontal));
        Predicate<BlockState> maxSideFrameCheck = (s) -> checkFrameBlockState(s, Direction.NORTH, List.of(posHorizontal));

        // The only reason I use atomic here is so i can modify it within a lambda
        AtomicBoolean sawWrongState = new AtomicBoolean(false);
        AtomicBoolean hasSeenAbandoned = new AtomicBoolean(false);
        BiConsumer<BlockPos, Predicate<BlockState>> frameTest = (p, t) -> {
            BlockState checkState = level.getBlockState(p);
            if (!t.test(checkState)) {
                sawWrongState.set(true);
            }

            if (checkState.is(EGBlocks.ABANDONED_GATEWAY.get())) {
                hasSeenAbandoned.set(true);
            }
        };

        forBlockAlongDirection(minCorner.above(), Direction.UP, height - 2)
                .forEach((p) -> frameTest.accept(p, minSideFrameCheck));
        forBlockAlongDirection(maxCorner.below(), Direction.DOWN, height - 2)
                .forEach((p) -> frameTest.accept(p, maxSideFrameCheck));
        forBlockAlongDirection(minCorner.relative(posHorizontal), posHorizontal, width - 2)
                .forEach((p) -> frameTest.accept(p, bottomFrameCheck));
        forBlockAlongDirection(maxCorner.relative(negHorizontal), negHorizontal, width - 2)
                .forEach((p) -> frameTest.accept(p, topFrameCheck));

        // Need at least one abandoned frame for assembly - need to make sure they're only built at structure locations
        if (!hasSeenAbandoned.get() || sawWrongState.get()) {
            return;
        }


        // Place the portals
        BlockState portalBS = EGBlocks.GATEWAY_PORTAL.getDefaultState();
        portalBS = portalBS.setValue(BlockStateProperties.HORIZONTAL_AXIS, portalAxis);

        for (BlockPos alongHorizontal : forBlockAlongDirection(insideCorner, posHorizontal, width - 2)) {
            for (BlockPos placePos : forBlockAlongDirection(alongHorizontal, Direction.UP, height - 2)) {
                level.setBlock(placePos, portalBS, Block.UPDATE_CLIENTS);
            }
        }
    }

    private List<BlockPos> forBlockAlongDirection(BlockPos start, Direction direction, int length) {
        BlockPos current = start;
        List<BlockPos> positions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            positions.add(current);
            current = current.relative(direction);
        }

        return positions;
    }

    private Pair<BlockPos, BlockPos> findPortalCorners(LevelAccessor level, BlockPos pos, Direction backFacing, Direction.Axis portalAxis) {
        BlockPos oppositeSide = exploreFrameWhile(level, pos, backFacing, s -> s.is(Blocks.AIR));
        if (oppositeSide == null || !isFrame(level, oppositeSide)) {
            return null;
        }

        BlockPos positiveExtent = exploreFrameWhile(level, pos, Direction.get(Direction.AxisDirection.POSITIVE, portalAxis), VerticalGatewayBlock::isFrame);
        BlockPos negativeExtent = exploreFrameWhile(level, pos, Direction.get(Direction.AxisDirection.NEGATIVE, portalAxis), VerticalGatewayBlock::isFrame);

        if (positiveExtent == null || negativeExtent == null) {
            return null;
        }

        // Find the corners, check the width
        BlockPos minCorner = BlockPos.min(BlockPos.min(positiveExtent, negativeExtent), oppositeSide);
        BlockPos maxCorner = BlockPos.max(BlockPos.max(positiveExtent, negativeExtent), oppositeSide);

        return Pair.of(minCorner, maxCorner);
    }

    private Pair<Direction, Direction.Axis> getPortalAlignment(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return switch (state.getValue(BACK_ALIGNMENT)) {
            case UP   -> Pair.of(Direction.UP,         facing.getClockWise().getAxis());
            case DOWN -> Pair.of(Direction.DOWN,       facing.getClockWise().getAxis());
            default   -> Pair.of(facing.getOpposite(), Direction.Axis.Y);
        };
    }

    private boolean checkFrameBlockState(BlockState s, Direction backAlignment, List<Direction> allowedFacing) {
        if (!s.is(EGTags.GATEWAY_FRAME)) {
            return false;
        }

        if (!s.getValue(BlockStateProperties.EYE)) {
            return false;
        }

        if (s.getValue(BACK_ALIGNMENT) != backAlignment) {
            return false;
        }

        if (!allowedFacing.contains(s.getValue(BlockStateProperties.HORIZONTAL_FACING))) {
            return false;
        }

        return true;
    }

    // Returns the BlockPos that it failed on.
    private BlockPos exploreFrameWhile(LevelAccessor level, BlockPos pos, Direction exploreDir, Predicate<BlockState> check) {
        BlockPos checkPos = pos.relative(exploreDir);
        BlockState checkState = level.getBlockState(checkPos);
        int checked = 0;
        while (check.test(checkState)) {
            if (checked >= MAX_FRAME_SIZE) {
                return null;
            }

            checkPos = checkPos.relative(exploreDir);
            checkState = level.getBlockState(checkPos);
            checked++;
        }

        return checkPos;
    }
}
