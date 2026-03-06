package io.github.ayohee.createendergateway.compat;

import net.irisshaders.iris.Iris;

public class IrisCompat {
    public static boolean isUsingShaders() {
        return Iris.getCurrentPack().isPresent();
    }
}
