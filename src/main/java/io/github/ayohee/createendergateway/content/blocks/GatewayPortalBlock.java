package io.github.ayohee.createendergateway.content.blocks;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import io.github.ayohee.createendergateway.register.EGBlockEntityTypes;
import io.github.ayohee.createendergateway.register.EGBlocks;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

public class GatewayPortalBlock extends Block implements Portal, IBE<GatewayBlockEntity> {
    private static final VoxelShape BASE_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    public static final VoxelShaper GATEWAY_PORTAL_SHAPE = new AllShapes.Builder(BASE_SHAPE).forHorizontalAxis();

    public GatewayPortalBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return GATEWAY_PORTAL_SHAPE.get(state.getValue(BlockStateProperties.HORIZONTAL_AXIS));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.canUsePortal(false)) {
            return;
        }
        entity.setAsInsidePortal(this, pos);

        if (!(level instanceof ServerLevel sLevel)) {
            return;
        }

        if (askBlockEntityForPortal(sLevel, pos) == null && entity instanceof LivingEntity le) {
            teleportAway(sLevel, le);
            return;
        }
    }

    private void teleportAway(ServerLevel level, LivingEntity entity) {
        if (entity.isOnPortalCooldown()) {
            return;
        }

        for (int i = 0; i < 16; i++) {
            double targetX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            double targetY = Mth.clamp(
                    entity.getY() + (entity.getRandom().nextInt(16) - 8),
                    level.getMinBuildHeight(),
                    level.getMaxBuildHeight()
            );
            double targetZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            if (entity.isPassenger()) {
                entity.stopRiding();
            }

            Vec3 posBeforeTP = entity.position();
            if (entity.randomTeleport(targetX, targetY, targetZ, true)) {
                level.gameEvent(GameEvent.TELEPORT, posBeforeTP, GameEvent.Context.of(entity));

                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                entity.resetFallDistance();

                // Small cooldown to prevent other portal blocks from making particles where there shouldn't be
                entity.setPortalCooldown(20);
                break;
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level instanceof ServerLevel sLevel) {
            unlinkPortal(sLevel, pos);
        }

        for (BlockPos neighbourPos : getNeighbours(level, pos, state)) {
            level.setBlock(neighbourPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }

        // Do this last so we have a BlockEntity to consult
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void unlinkPortal(ServerLevel sLevel, BlockPos pos) {
        BlockPos linkedPos = askBlockEntityForPortal(sLevel, pos);
        if (linkedPos == null) {
            return;
        }

        ResourceKey<Level> targetDimension = sLevel.dimension() == Level.END ? Level.OVERWORLD : Level.END;
        BlockEntity linkedBE = sLevel.getServer().getLevel(targetDimension).getBlockEntity(linkedPos);

        if (!(linkedBE instanceof GatewayBlockEntity gateway)) {
            return;
        }

        if (!gateway.isLinked()) {
            return;
        }

        gateway.propagateUnlink();
    }

    public static List<BlockPos> getNeighbours(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return getNeighbours(level, pos, state);
    }

    private static List<BlockPos> getNeighbours(LevelAccessor level, BlockPos pos, BlockState state) {
        List<BlockPos> neighbours = new ArrayList<>();

        if (!state.is(EGBlocks.GATEWAY_PORTAL)) {
            return neighbours;
        }

        for (Direction d : Direction.values()) {
            if (d.getAxis() != Direction.Axis.Y && d.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_AXIS)) {
                continue;
            }

            BlockPos neighbourPos = pos.relative(d);
            BlockState neighbourState = level.getBlockState(neighbourPos);

            if (neighbourState.is(EGBlocks.GATEWAY_PORTAL.get())) {
                neighbours.add(neighbourPos);
            }
        }

        return neighbours;
    }

    @Override
    public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> targetDimension = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
        ServerLevel dimensionLevel = level.getServer().getLevel(targetDimension);
        if (dimensionLevel == null) {
            return null;
        }

        BlockPos where = askBlockEntityForPortal(level, pos);
        if (where == null) {
            return null;
        }

        BlockState foreignBS = dimensionLevel.getBlockState(where);
        BlockState localBS = level.getBlockState(pos);
        if (!(foreignBS.is(EGBlocks.GATEWAY_PORTAL) && localBS.is(EGBlocks.GATEWAY_PORTAL))) {
            return null;
        }
        float yOffset = foreignBS.getValue(BlockStateProperties.HORIZONTAL_AXIS) == localBS.getValue(BlockStateProperties.HORIZONTAL_AXIS) ? 0 : 90;

        Vec3 blockCenter = pos.getCenter();
        Vec3 entityOffset = blockCenter.subtract(entity.position());

        return new DimensionTransition(dimensionLevel, where.getCenter().subtract(0, entityOffset.y(), 0), Vec3.ZERO, (entity.getYRot() + yOffset) % 360, entity.getXRot(), (Entity e) -> {});
    }

    private BlockPos askBlockEntityForPortal(ServerLevel level, BlockPos pos) {
        Optional<GatewayBlockEntity> optionalBE = level.getBlockEntity(pos, EGBlockEntityTypes.GATEWAY_PORTAL.get());
        if (optionalBE.isEmpty()) {
            return null;
        }

        GatewayBlockEntity be = optionalBE.get();
        BlockPos linkedPos = be.getLinkedPos();
        if (linkedPos == null) {
            return null;
        }

        ResourceKey<Level> targetDimension = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
        ServerLevel dimensionLevel = level.getServer().getLevel(targetDimension);
        if (dimensionLevel == null) {
            return null;
        }

        Optional<GatewayBlockEntity> linkedBE = dimensionLevel.getBlockEntity(linkedPos, EGBlockEntityTypes.GATEWAY_PORTAL.get());
        if (linkedBE.isEmpty()) {
            return null;
        }

        return linkedPos;
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + 0.8;
        double z = pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    public static void blockstate(DataGenContext<Block, GatewayPortalBlock> ctx, RegistrateBlockstateProvider prov) {
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(MODID, "block/" + ctx.getName());
        prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
                    .modelFile(prov.models().getExistingFile(model))
                    .rotationY(state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X ? 0 : 90)
                    .build();
        });
    }

    @Override
    public Class<GatewayBlockEntity> getBlockEntityClass() {
        return GatewayBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GatewayBlockEntity> getBlockEntityType() {
        return EGBlockEntityTypes.GATEWAY_PORTAL.get();
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        Direction.Axis alignment = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
        return switch (rotation) {
            case NONE, CLOCKWISE_180 -> state;
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> state.setValue(BlockStateProperties.HORIZONTAL_AXIS, alignment == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        };
    }


    public static Pair<BlockUtil.FoundRectangle, Direction.Axis> getPortalRect(BlockPos where, Level level) {
        Direction.Axis portalAxis = level.getBlockState(where).getValue(BlockStateProperties.HORIZONTAL_AXIS);
        return Pair.of(BlockUtil.getLargestRectangleAround(where,
                Direction.Axis.Y, VerticalGatewayBlock.MAX_FRAME_SIZE,
                portalAxis, VerticalGatewayBlock.MAX_FRAME_SIZE,
                (p) -> {
                    BlockState bs = level.getBlockState(p);
                    return bs.is(EGBlocks.GATEWAY_PORTAL) && bs.getValue(BlockStateProperties.HORIZONTAL_AXIS) == portalAxis;
                }
        ), portalAxis);
    }
}
