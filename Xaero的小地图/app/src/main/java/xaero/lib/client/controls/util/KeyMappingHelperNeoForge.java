package xaero.lib.client.controls.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/controls/util/KeyMappingHelperNeoForge.class */
public class KeyMappingHelperNeoForge implements IKeyMappingHelper {
    @Override // xaero.lib.client.controls.util.IKeyMappingHelper
    public InputConstants.Key getBoundKeyOf(KeyMapping km) {
        return km.getKey();
    }

    @Override // xaero.lib.client.controls.util.IKeyMappingHelper
    public boolean modifiersAreActive(KeyMapping km, int keyConflictContext) {
        KeyConflictContext keyConflictContext2;
        KeyModifier keyModifier = km.getKeyModifier();
        if (keyConflictContext == 0) {
            keyConflictContext2 = KeyConflictContext.GUI;
        } else if (keyConflictContext == 1) {
            keyConflictContext2 = KeyConflictContext.IN_GAME;
        } else {
            keyConflictContext2 = KeyConflictContext.UNIVERSAL;
        }
        return keyModifier.isActive(keyConflictContext2);
    }
}
