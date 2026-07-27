package xaero.hud.minimap.radar.icon.cache;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import xaero.common.icon.XaeroIcon;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.cache.id.RadarIconKey;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/RadarIconEntityCache.class */
public class RadarIconEntityCache {
    private final EntityType<?> entityType;
    private final Map<RadarIconKey, XaeroIcon> storage = new HashMap();
    private final Map<Object, String> variantStringCache = new HashMap();
    private boolean classValidityChecked;
    private boolean invalidVariantClass;
    private Class<?> variantClass;

    public RadarIconEntityCache(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public XaeroIcon get(RadarIconKey key) {
        if (this.invalidVariantClass) {
            return null;
        }
        if (key.getVariant() == null) {
            MinimapLogs.LOGGER.error("One of the variant IDs for entity {} is null!", EntityType.getKey(this.entityType));
            MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
            this.invalidVariantClass = true;
            return null;
        }
        return this.storage.get(key);
    }

    public XaeroIcon add(RadarIconKey key, XaeroIcon icon) throws NoSuchMethodException, SecurityException {
        if (this.invalidVariantClass) {
            return null;
        }
        Class<?> c = key.getVariant().getClass();
        if (this.variantClass == null) {
            this.variantClass = c;
        } else if (c != this.variantClass) {
            MinimapLogs.LOGGER.error("The variant IDs of entity {} don't use the same class! {} is not {}", EntityType.getKey(this.entityType), c, this.variantClass);
            MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
            this.invalidVariantClass = true;
            return null;
        }
        if (!this.classValidityChecked) {
            this.classValidityChecked = true;
            if (c == Object.class) {
                MinimapLogs.LOGGER.error("The class used for variant IDs of entity {} can't be Object!", EntityType.getKey(this.entityType));
                MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
                this.invalidVariantClass = true;
                return null;
            }
            try {
                c.getDeclaredMethod("toString", new Class[0]);
                c.getDeclaredMethod("hashCode", new Class[0]);
                c.getDeclaredMethod("equals", Object.class);
            } catch (NoSuchMethodException e) {
                MinimapLogs.LOGGER.error("The {} used for variant IDs of entity {} doesn't declare toString, hashCode or equals methods!", c, EntityType.getKey(this.entityType));
                MinimapLogs.LOGGER.error("If you're a regular player, this is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
                MinimapLogs.LOGGER.error("If you are the icon resource pack or mod author, please use Java records for variant IDs, if possible. You can also let your IDE generate all 3 methods for you.");
                MinimapLogs.LOGGER.error("Declaring the hashCode or equals methods incorrectly might destroy the game's performance and then crash it.");
                MinimapLogs.LOGGER.error("The simplest way to get this to work is to just use String variant IDs, but it won't perform as well as properly using the new system.");
                this.invalidVariantClass = true;
                return null;
            }
        }
        this.variantStringCache.remove(key.getVariant());
        return this.storage.put(key, icon);
    }

    public String getVariantString(RadarIconKey key) {
        Object variant = key.getVariant();
        String result = this.variantStringCache.get(variant);
        if (result == null) {
            Map<Object, String> map = this.variantStringCache;
            String string = variant.toString();
            result = string;
            map.put(variant, string);
        }
        return result;
    }

    public boolean isInvalidVariantClass() {
        return this.invalidVariantClass;
    }
}
