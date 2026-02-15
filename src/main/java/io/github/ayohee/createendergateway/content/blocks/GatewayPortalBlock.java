package io.github.ayohee.createendergateway.content.blocks;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.ayohee.createendergateway.content.blockentity.GatewayBlockEntity;
import io.github.ayohee.createendergateway.register.EGBlockEntityTypes;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

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
        if (!((GatewayBlockEntity)level.getBlockEntity(pos)).isLinked() && entity instanceof LivingEntity le) {
            teleportAway(level, le);
            return;
        }

        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    private void teleportAway(Level level, LivingEntity le) {
        //TODO
    }

    @Override
    public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        if (!((GatewayBlockEntity)level.getBlockEntity(pos)).isLinked()) {
            return null;
        }

        ResourceKey<Level> targetDimension = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
        ServerLevel dimensionLevel = level.getServer().getLevel(targetDimension);
        if (dimensionLevel == null) {
            return null;
        }

        return new DimensionTransition(dimensionLevel, new Vec3(100, 100, 100), Vec3.ZERO, entity.getYRot(), entity.getXRot(), (Entity e) -> {});
    }



    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + random.nextDouble();
        double d1 = (double)pos.getY() + 0.8;
        double d2 = (double)pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
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
}
