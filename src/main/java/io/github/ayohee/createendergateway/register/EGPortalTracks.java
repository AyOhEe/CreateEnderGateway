package io.github.ayohee.createendergateway.register;

import com.simibubi.create.api.contraption.train.PortalTrackProvider;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;

public class EGPortalTracks {
    public static void register() {
        ResourceLocation blockID = EGBlocks.GATEWAY_PORTAL.getId();
        PortalTrackProvider p = (level, face) ->
                PortalTrackProvider.fromPortal(level, face, Level.OVERWORLD, Level.END, (Portal) BuiltInRegistries.BLOCK.get(blockID));
        AllPortalTracks.tryRegisterIntegration(blockID, p);
    }
}
