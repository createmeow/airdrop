package xaero.hud.minimap.radar.icon.cache.id;

import java.util.Objects;
import xaero.hud.minimap.radar.icon.cache.id.armor.RadarIconArmor;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/RadarIconKey.class */
public class RadarIconKey {
    private final Object variant;
    private final RadarIconArmor armor;

    public RadarIconKey(Object variant, RadarIconArmor armor) {
        this.variant = variant;
        this.armor = armor;
    }

    public Object getVariant() {
        return this.variant;
    }

    public String toString() {
        return "RadarIconKey{" + String.valueOf(this.variant) + ", " + String.valueOf(this.armor) + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RadarIconKey that = (RadarIconKey) o;
        return this.variant.equals(that.variant) && Objects.equals(this.armor, that.armor);
    }

    public int hashCode() {
        return Objects.hash(this.variant, this.armor);
    }
}
