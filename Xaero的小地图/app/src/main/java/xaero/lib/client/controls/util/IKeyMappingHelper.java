package xaero.lib.client.controls.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/controls/util/IKeyMappingHelper.class */
public interface IKeyMappingHelper {
    InputConstants.Key getBoundKeyOf(KeyMapping keyMapping);

    boolean modifiersAreActive(KeyMapping keyMapping, int i);
}
