package xaero.hud.minimap.radar.icon.definition;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/RadarIconDefinitionManager.class */
public class RadarIconDefinitionManager {
    private final Map<ResourceLocation, RadarIconDefinition> definitions = new HashMap();
    private final RadarIconDefinitionReloader reloader = new RadarIconDefinitionReloader();

    public RadarIconDefinition get(ResourceLocation key) {
        return this.definitions.get(key);
    }

    public void reloadResources() {
        this.reloader.reloadResources(this.definitions);
    }
}
