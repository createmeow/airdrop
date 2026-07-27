package xaero.hud.minimap.radar.icon.cache.id.armor;

import java.util.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/armor/RadarIconArmor.class */
public class RadarIconArmor {
    private final Item armor;
    private final TrimMaterial trimMaterial;
    private final TrimPattern trimPattern;

    public RadarIconArmor(Item armor, TrimMaterial trimMaterial, TrimPattern trimPattern) {
        this.armor = armor;
        this.trimMaterial = trimMaterial;
        this.trimPattern = trimPattern;
    }

    public String toString() {
        return "RadarIconArmor{" + String.valueOf(this.armor) + ", " + String.valueOf(this.trimMaterial) + ", " + String.valueOf(this.trimPattern) + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RadarIconArmor that = (RadarIconArmor) o;
        return this.armor.equals(that.armor) && Objects.equals(this.trimMaterial, that.trimMaterial) && Objects.equals(this.trimPattern, that.trimPattern);
    }

    public int hashCode() {
        return Objects.hash(this.armor, this.trimMaterial, this.trimPattern);
    }
}
