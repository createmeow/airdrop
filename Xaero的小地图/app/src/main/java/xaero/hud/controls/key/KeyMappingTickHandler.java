package xaero.hud.controls.key;

import java.util.Iterator;
import net.minecraft.client.KeyMapping;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.lib.client.controls.util.KeyMappingUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/controls/key/KeyMappingTickHandler.class */
public class KeyMappingTickHandler {
    public static boolean DISABLE_KEY_MAPPING_OVERRIDES;
    private final KeyMappingControllerManager controllerManager;

    public KeyMappingTickHandler(KeyMappingControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    public void tick() {
        Iterator<KeyMappingController> it = this.controllerManager.iterator();
        while (it.hasNext()) {
            KeyMappingController controller = it.next();
            handleExtraPresses(controller);
        }
        Iterator<KeyMappingController> it2 = this.controllerManager.iterator();
        while (it2.hasNext()) {
            KeyMappingController controller2 = it2.next();
            handleRelease(controller2);
        }
        Iterator<KeyMappingController> it3 = this.controllerManager.iterator();
        while (it3.hasNext()) {
            KeyMappingController controller3 = it3.next();
            handlePress(controller3);
        }
    }

    private void handlePress(KeyMappingController controller) {
        boolean startingPress = false;
        if (!controller.isPressed()) {
            boolean updatedPressedValue = getUpdatedPressedValue(controller);
            if (!updatedPressedValue) {
                return;
            }
            startingPress = true;
            controller.setPressed(true);
        }
        handlePressFunctions(controller, startingPress);
    }

    private void handleExtraPresses(KeyMappingController controller) {
        if (!controller.isPressed()) {
            return;
        }
        while (controller.isXaeroKey() && controller.getKeyMapping().consumeClick()) {
            Iterator<KeyMappingFunction> it = controller.iterator();
            while (it.hasNext()) {
                KeyMappingFunction func = it.next();
                if (!func.isHeld()) {
                    func.onRelease();
                }
            }
            Iterator<KeyMappingFunction> it2 = controller.iterator();
            while (it2.hasNext()) {
                KeyMappingFunction func2 = it2.next();
                if (!func2.isHeld()) {
                    func2.onPress();
                }
            }
        }
    }

    private void handleRelease(KeyMappingController controller) {
        if (!controller.isPressed() || isPhysicallyDown(controller)) {
            return;
        }
        handleReleaseFunctions(controller);
        controller.setPressed(false);
    }

    private void handlePressFunctions(KeyMappingController controller, boolean startingPress) {
        Iterator<KeyMappingFunction> it = controller.iterator();
        while (it.hasNext()) {
            KeyMappingFunction func = it.next();
            if (startingPress || func.isHeld()) {
                func.onPress();
            }
        }
    }

    private void handleReleaseFunctions(KeyMappingController controller) {
        Iterator<KeyMappingFunction> it = controller.iterator();
        while (it.hasNext()) {
            KeyMappingFunction func = it.next();
            func.onRelease();
        }
    }

    private boolean getUpdatedPressedValue(KeyMappingController controller) {
        KeyMapping keyMapping = controller.getKeyMapping();
        if (!controller.isXaeroKey()) {
            return isPhysicallyDown(controller);
        }
        boolean result = keyMapping.consumeClick();
        if (!result) {
            return false;
        }
        while (keyMapping.consumeClick()) {
            handlePressFunctions(controller, true);
            handleReleaseFunctions(controller);
        }
        return true;
    }

    private boolean isPhysicallyDown(KeyMappingController controller) {
        KeyMapping keyMapping = controller.getKeyMapping();
        if (!controller.isXaeroKey()) {
            DISABLE_KEY_MAPPING_OVERRIDES = true;
            boolean down = keyMapping.isDown();
            DISABLE_KEY_MAPPING_OVERRIDES = false;
            return down;
        }
        return KeyMappingUtils.isPhysicallyDown(keyMapping);
    }
}
