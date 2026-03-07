package io.github.ayohee.createendergateway.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.irisshaders.iris.helpers.StringPair;
import net.irisshaders.iris.shaderpack.IdMap;
import net.irisshaders.iris.shaderpack.materialmap.BlockEntry;
import net.irisshaders.iris.shaderpack.materialmap.NamespacedId;
import net.irisshaders.iris.shaderpack.option.ShaderPackOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static io.github.ayohee.createendergateway.CreateEnderGateway.MODID;

@Pseudo
@Mixin(IdMap.class)
public class IrisIdMapMixin {
    @Unique
    private static final BlockEntry GATEWAY_PORTAL = new BlockEntry(new NamespacedId(MODID, "gateway_portal"), Collections.emptyMap());

    @Shadow
    private Int2ObjectLinkedOpenHashMap<List<BlockEntry>> blockPropertiesMap;


    @Inject(method="<init>", at=@At("TAIL"))
    public void ceg$ctor(Path shaderPath, ShaderPackOptions shaderPackOptions, Iterable<StringPair> environmentDefines, CallbackInfo ci){
        HashMap<Integer, List<BlockEntry>> replacements = new HashMap<>();
        blockPropertiesMap.forEach((id, list) -> {
            boolean found = list.stream().anyMatch(entry ->
                    entry.id().getNamespace().equals("minecraft") && entry.id().getName().equals("end_portal")
            );

            if (found) {
                List<BlockEntry> newEntries = new ArrayList<>(list);
                newEntries.add(GATEWAY_PORTAL);
                replacements.put(id, Collections.unmodifiableList(newEntries));
            }
        });

        // Can't directly modify the lists - they're made with Collections.unmodifiableList
        blockPropertiesMap.putAll(replacements);
    }
}
