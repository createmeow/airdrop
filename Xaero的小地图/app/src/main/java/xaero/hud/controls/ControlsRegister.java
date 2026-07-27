package xaero.hud.controls;

import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import xaero.hud.controls.key.KeyMappingControllerManager;
import xaero.hud.minimap.controls.key.MinimapKeyMappings;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/controls/ControlsRegister.class */
public abstract class ControlsRegister {
    protected final KeyMappingControllerManager keyMappingControllers = new KeyMappingControllerManager();

    protected ControlsRegister() {
    }

    public void registerKeybindings(Consumer<KeyMapping> registry) {
        MinimapKeyMappings.registerAll(this.keyMappingControllers, registry);
    }

    public void onStage2() {
    }

    public KeyMappingControllerManager getKeyMappingControllers() {
        return this.keyMappingControllers;
    }
}
