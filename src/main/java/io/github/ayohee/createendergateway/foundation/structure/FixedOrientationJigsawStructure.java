package io.github.ayohee.createendergateway.foundation.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ayohee.createendergateway.register.EGStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.levelgen.structure.structures.JigsawStructure.*;

public class FixedOrientationJigsawStructure extends Structure {
    public static final MapCodec<FixedOrientationJigsawStructure> CODEC = RecordCodecBuilder.<FixedOrientationJigsawStructure>mapCodec(
                    builder -> builder.group(
                                    settingsCodec(builder),
                                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                                    Codec.intRange(MIN_DEPTH, MAX_DEPTH).fieldOf("size").forGetter(structure -> structure.maxDepth),
                                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                                    Rotation.CODEC.fieldOf("rotation").forGetter(structure -> structure.rotation),
                                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter(structure -> structure.useExpansionHack),
                                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                                    Codec.intRange(1, MAX_TOTAL_STRUCTURE_RANGE).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
                                    Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(structure -> structure.poolAliases),
                                    DimensionPadding.CODEC
                                            .optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING)
                                            .forGetter(structure -> structure.dimensionPadding),
                                    LiquidSettings.CODEC.optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter(structure -> structure.liquidSettings)
                            )
                            .apply(builder, FixedOrientationJigsawStructure::new)
            )
            .validate(FixedOrientationJigsawStructure::verifyRange);

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final Rotation rotation;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final List<PoolAliasBinding> poolAliases;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;

    private static DataResult<FixedOrientationJigsawStructure> verifyRange(FixedOrientationJigsawStructure structure) {
        int i = switch (structure.terrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE -> 12;
        };
        return structure.maxDistanceFromCenter + i > MAX_TOTAL_STRUCTURE_RANGE
                ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128")
                : DataResult.success(structure);
    }

    public FixedOrientationJigsawStructure(
            Structure.StructureSettings settings,
            Holder<StructureTemplatePool> startPool,
            Optional<ResourceLocation> startJigsawName,
            int maxDepth,
            HeightProvider startHeight,
            Rotation rotation,
            boolean useExpansionHack,
            Optional<Heightmap.Types> projectStartToHeightmap,
            int maxDistanceFromCenter,
            List<PoolAliasBinding> poolAliases,
            DimensionPadding dimensionPadding,
            LiquidSettings liquidSettings
    ) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.rotation = rotation;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.poolAliases = poolAliases;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    public FixedOrientationJigsawStructure(
            Structure.StructureSettings settings,
            Holder<StructureTemplatePool> startPool,
            int maxDepth,
            HeightProvider startHeight,
            Rotation rotation,
            boolean useExpansionHack,
            Heightmap.Types projectStartToHeightmap
    ) {
        this(
                settings,
                startPool,
                Optional.empty(),
                maxDepth,
                startHeight,
                rotation,
                useExpansionHack,
                Optional.of(projectStartToHeightmap),
                80,
                List.of(),
                DEFAULT_DIMENSION_PADDING,
                DEFAULT_LIQUID_SETTINGS
        );
    }

    public FixedOrientationJigsawStructure(
            Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, Rotation rotation, boolean useExpansionHack
    ) {
        this(
                settings,
                startPool,
                Optional.empty(),
                maxDepth,
                startHeight,
                rotation,
                useExpansionHack,
                Optional.empty(),
                80,
                List.of(),
                DEFAULT_DIMENSION_PADDING,
                DEFAULT_LIQUID_SETTINGS
        );
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkpos = context.chunkPos();
        int i = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        return FixedOrientationJigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.maxDepth,
                blockpos,
                this.rotation,
                this.useExpansionHack,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter,
                PoolAliasLookup.create(this.poolAliases, blockpos, context.seed()),
                this.dimensionPadding,
                this.liquidSettings
        );
    }

    @Override
    public StructureType<?> type() {
        return EGStructureTypes.FIXED_ORIENTATION_JIGSAW.get();
    }
}
