package io.github.ayohee.createendergateway.register;

import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.ayohee.createendergateway.content.blocks.VerticalGatewayBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGBlocks {
    /*-----THESE BLOCKS WILL BE SEEN IN THE CREATIVE TAB-----*/
    static {
        REGISTRATE.setCreativeTab(EGCreateiveTabs.MAIN_TAB);
    }

    public static final BlockEntry<VerticalGatewayBlock> MECHANICAL_GATEWAY = REGISTRATE.block("mechanical_gateway", VerticalGatewayBlock::new)
            .initialProperties(() -> Blocks.END_GATEWAY)
            .properties((p) -> p.strength(5, 6).lightLevel(VerticalGatewayBlock::lightLevel))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL, BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
            .blockstate(VerticalGatewayBlock::blockstate)
            .simpleItem()
            .register();
    public static final BlockEntry<VerticalGatewayBlock> ABANDONED_GATEWAY = REGISTRATE.block("abandoned_gateway", VerticalGatewayBlock::new)
            .initialProperties(() -> Blocks.END_GATEWAY)
            .properties((p) -> p.strength(-1).lightLevel(VerticalGatewayBlock::lightLevel))
            .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
            .blockstate(VerticalGatewayBlock::blockstate)
            .simpleItem()
            .register();

    public static void register() { }
}
