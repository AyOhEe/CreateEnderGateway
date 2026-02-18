package io.github.ayohee.createendergateway.foundation.structure;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;


public class FixedOrientationJigsawPlacement {
    static final Logger LOGGER = LogUtils.getLogger();

    public static Optional<Structure.GenerationStub> addPieces(
            Structure.GenerationContext context,
            Holder<StructureTemplatePool> startPool,
            Optional<ResourceLocation> startJigsawName,
            int maxDepth,
            BlockPos pos,
            Rotation rotation,
            boolean useExpansionHack,
            Optional<Heightmap.Types> projectStartToHeightmap,
            int maxDistanceFromCenter,
            PoolAliasLookup aliasLookup,
            DimensionPadding dimensionPadding,
            LiquidSettings liquidSettings
    ) {
        RegistryAccess registryaccess = context.registryAccess();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureTemplateManager structuretemplatemanager = context.structureTemplateManager();
        LevelHeightAccessor levelheightaccessor = context.heightAccessor();
        WorldgenRandom worldgenrandom = context.random();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool structuretemplatepool = startPool.unwrapKey()
                .flatMap(p_314915_ -> registry.getOptional(aliasLookup.lookup((ResourceKey<StructureTemplatePool>)p_314915_)))
                .orElse(startPool.value());
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos blockpos;
            if (startJigsawName.isPresent()) {
                ResourceLocation resourcelocation = startJigsawName.get();
                Optional<BlockPos> optional = JigsawPlacement.getRandomNamedJigsaw(
                        structurepoolelement, resourcelocation, pos, rotation, structuretemplatemanager, worldgenrandom
                );
                if (optional.isEmpty()) {
                    LOGGER.error(
                            "No starting jigsaw {} found in start pool {}",
                            resourcelocation,
                            startPool.unwrapKey().map(p_248484_ -> p_248484_.location().toString()).orElse("<unregistered>")
                    );
                    return Optional.empty();
                }

                blockpos = optional.get();
            } else {
                blockpos = pos;
            }

            Vec3i vec3i = blockpos.subtract(pos);
            BlockPos blockpos1 = pos.subtract(vec3i);
            PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(
                    structuretemplatemanager,
                    structurepoolelement,
                    blockpos1,
                    structurepoolelement.getGroundLevelDelta(),
                    rotation,
                    structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation),
                    liquidSettings
            );
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            if (projectStartToHeightmap.isPresent()) {
                k = pos.getY() + chunkgenerator.getFirstFreeHeight(i, j, projectStartToHeightmap.get(), levelheightaccessor, context.randomState());
            } else {
                k = blockpos1.getY();
            }

            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            int i1 = k + vec3i.getY();
            return Optional.of(
                    new Structure.GenerationStub(
                            new BlockPos(i, i1, j),
                            p_352014_ -> {
                                List<PoolElementStructurePiece> list = Lists.newArrayList();
                                list.add(poolelementstructurepiece);
                                if (maxDepth > 0) {
                                    AABB aabb = new AABB(
                                            (double)(i - maxDistanceFromCenter),
                                            (double)Math.max(i1 - maxDistanceFromCenter, levelheightaccessor.getMinBuildHeight() + dimensionPadding.bottom()),
                                            (double)(j - maxDistanceFromCenter),
                                            (double)(i + maxDistanceFromCenter + 1),
                                            (double)Math.min(i1 + maxDistanceFromCenter + 1, levelheightaccessor.getMaxBuildHeight() - dimensionPadding.top()),
                                            (double)(j + maxDistanceFromCenter + 1)
                                    );
                                    VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
                                    JigsawPlacement.addPieces(
                                            context.randomState(),
                                            maxDepth,
                                            useExpansionHack,
                                            chunkgenerator,
                                            structuretemplatemanager,
                                            levelheightaccessor,
                                            worldgenrandom,
                                            registry,
                                            poolelementstructurepiece,
                                            list,
                                            voxelshape,
                                            aliasLookup,
                                            liquidSettings
                                    );
                                    list.forEach(p_352014_::addPiece);
                                }
                            }
                    )
            );
        }
    }
}
