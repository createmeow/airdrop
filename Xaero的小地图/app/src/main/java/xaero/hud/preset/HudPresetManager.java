package xaero.hud.preset;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/preset/HudPresetManager.class */
public class HudPresetManager {
    private final Map<ResourceLocation, HudPreset> presets = new LinkedHashMap();

    public void register(HudPreset preset) {
        this.presets.put(preset.getId(), preset);
    }

    public Iterable<HudPreset> getPresets() {
        return this.presets.values();
    }
}
