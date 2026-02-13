package io.github.ayohee.createendergateway.register;

import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.ayohee.createendergateway.content.blocks.GatewayPortalBlock;
import io.github.ayohee.createendergateway.content.blocks.VerticalGatewayBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import static io.github.ayohee.createendergateway.CreateEnderGateway.REGISTRATE;

public class EGBlocks {
    /*-----THESE BLOCKS WILL BE SEEN IN THE CREATIVE TAB-----*/
    static {
        REGISTRATE.setCreativeTab(EGCreativeTabs.MAIN_TAB);
    }

    public static final BlockEntry<VerticalGatewayBlock> MECHANICAL_GATEWAY = REGISTRATE.block("mechanical_gateway", VerticalGatewayBlock::new)
            .initialProperties(() -> Blocks.END_PORTAL_FRAME)
            .properties((p) -> p
                    .lightLevel(VerticalGatewayBlock::lightLevel)
                    .strength(5, 6)
            )
            .tag(EGTags.GATEWAY_FRAME, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL, BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
            .blockstate(VerticalGatewayBlock::blockstate)
            .simpleItem()
            .register();

    public static final BlockEntry<VerticalGatewayBlock> ABANDONED_GATEWAY = REGISTRATE.block("abandoned_gateway", VerticalGatewayBlock::new)
            .initialProperties(() -> Blocks.END_PORTAL_FRAME)
            .properties((p) -> p
                    .lightLevel(VerticalGatewayBlock::lightLevel)
                    .strength(-1)
            )
            .tag(EGTags.GATEWAY_FRAME, BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
            .blockstate(VerticalGatewayBlock::blockstate)
            .simpleItem()
            .register();


    /*-----THESE BLOCKS WILL *NOT* BE SEEN IN THE CREATIVE TAB-----*/
    static {
        REGISTRATE.setCreativeTab(null);
    }

    public static final BlockEntry<GatewayPortalBlock> GATEWAY_PORTAL = REGISTRATE.block("gateway_portal", GatewayPortalBlock::new)
            .initialProperties(() -> Blocks.NETHER_PORTAL)
            .properties((p) -> p

            )
            .tag(BlockTags.PORTALS, BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE, BlockTags.INVALID_SPAWN_INSIDE)
            .blockstate(GatewayPortalBlock::blockstate)
            .simpleItem()
            .register();

    public static void register() { }
}
