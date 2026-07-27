package xaero.hud.module;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/module/ModuleManager.class */
public final class ModuleManager {
    private final Map<ResourceLocation, HudModule<?>> modules = new LinkedHashMap();

    public void register(HudModule<?> hudModule) {
        this.modules.put(hudModule.getId(), hudModule);
    }

    public HudModule<?> get(ResourceLocation id) {
        return this.modules.get(id);
    }

    public Iterable<HudModule<?>> getModules() {
        return this.modules.values();
    }
}
