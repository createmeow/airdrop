package xaero.lib.client.controls.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/controls/util/KeyMappingUtils.class */
public class KeyMappingUtils {
    public static boolean inputMatches(InputConstants.Type type, int code, KeyMapping kb, int keyConflictContext) {
        IKeyMappingHelper keyBindingHelper = Services.PLATFORM.getKeyMappingHelper();
        return kb != null && code != -1 && keyBindingHelper.getBoundKeyOf(kb).getType() == type && keyBindingHelper.getBoundKeyOf(kb).getValue() == code && keyBindingHelper.modifiersAreActive(kb, keyConflictContext);
    }

    public static void setKeyState(KeyMapping keyMapping, boolean pressed) {
        KeyMapping.set(Services.PLATFORM.getKeyMappingHelper().getBoundKeyOf(keyMapping), pressed);
    }

    public static boolean isPhysicallyDown(KeyMapping keyMapping) {
        IKeyMappingHelper keyBindingHelper = Services.PLATFORM.getKeyMappingHelper();
        if (keyBindingHelper.getBoundKeyOf(keyMapping).getValue() == -1) {
            return false;
        }
        if (keyBindingHelper.getBoundKeyOf(keyMapping).getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keyBindingHelper.getBoundKeyOf(keyMapping).getValue()) == 1;
        }
        if (keyBindingHelper.getBoundKeyOf(keyMapping).getType() == InputConstants.Type.KEYSYM) {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyBindingHelper.getBoundKeyOf(keyMapping).getValue());
        }
        return false;
    }

    public static String getKeyName(KeyMapping kb) {
        if (kb == null || Services.PLATFORM.getKeyMappingHelper().getBoundKeyOf(kb).getValue() == -1) {
            return "(unset)";
        }
        return kb.getTranslatedKeyMessage().getString().toUpperCase();
    }
}
