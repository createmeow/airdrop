package xaero.hud.xminimap.controls.key;

import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import xaero.hud.controls.key.KeyMappingControllerManager;
import xaero.hud.minimap.controls.key.MinimapKeyMappings;
import xaero.hud.xminimap.controls.key.function.XMinimapKeyMappingFunctions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/xminimap/controls/key/XMinimapKeyMappings.class */
public class XMinimapKeyMappings {
    public static KeyMapping SETTINGS = new KeyMapping("gui.xaero_minimap_settings", 89, MinimapKeyMappings.CATEGORY);
    public static KeyMapping SERVER_PROFILES = new KeyMapping("gui.xaero_minimap_server_profiles", -1, MinimapKeyMappings.CATEGORY);

    public static void registerAll(KeyMappingControllerManager controllerManager, Consumer<KeyMapping> registry) {
        controllerManager.registerController(SETTINGS, true, registry);
        controllerManager.registerController(SERVER_PROFILES, true, registry);
        XMinimapKeyMappingFunctions.registerAll(controllerManager);
    }
}
