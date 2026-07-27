package xaero.hud.xminimap.controls;

import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import xaero.hud.controls.ControlsRegister;
import xaero.hud.xminimap.controls.key.XMinimapKeyMappings;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/xminimap/controls/XMinimapControlsRegister.class */
public class XMinimapControlsRegister extends ControlsRegister {
    @Override // xaero.hud.controls.ControlsRegister
    public void registerKeybindings(Consumer<KeyMapping> registry) {
        XMinimapKeyMappings.registerAll(this.keyMappingControllers, registry);
        super.registerKeybindings(registry);
    }
}
