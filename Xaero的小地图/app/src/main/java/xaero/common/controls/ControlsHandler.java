package xaero.common.controls;

import java.util.Iterator;
import net.minecraft.client.KeyMapping;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.hud.HudSession;
import xaero.hud.controls.key.KeyMappingController;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.lib.client.controls.util.KeyMappingUtils;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/controls/ControlsHandler.class */
public class ControlsHandler {
    protected IXaeroMinimap modMain;
    protected HudSession hudSession;

    public ControlsHandler(IXaeroMinimap modMain, HudSession hudSession) {
        this.modMain = modMain;
        this.hudSession = hudSession;
    }

    @Deprecated
    public void setKeyState(KeyMapping kb, boolean pressed) {
        KeyMappingUtils.setKeyState(kb, pressed);
    }

    @Deprecated
    public boolean isDown(KeyMapping kb) {
        return KeyMappingUtils.isPhysicallyDown(kb);
    }

    @Deprecated
    public void keyDown(KeyMapping kb, boolean tickEnd, boolean isRepeat) {
        if (tickEnd) {
            return;
        }
        KeyMappingController controller = HudMod.INSTANCE.getKeyMappingControllers().getController(kb);
        Iterator<KeyMappingFunction> it = controller.iterator();
        while (it.hasNext()) {
            KeyMappingFunction function = it.next();
            if (!function.isHeld()) {
                function.onPress();
            }
        }
        Iterator<KeyMappingFunction> it2 = controller.iterator();
        while (it2.hasNext()) {
            KeyMappingFunction function2 = it2.next();
            if (!function2.isHeld()) {
                function2.onRelease();
            }
        }
    }

    @Deprecated
    public void keyUp(KeyMapping kb, boolean tickEnd) {
    }
}
