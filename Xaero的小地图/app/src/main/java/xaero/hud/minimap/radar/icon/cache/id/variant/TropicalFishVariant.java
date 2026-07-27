package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.DyeColor;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/TropicalFishVariant.class */
public class TropicalFishVariant {
    private final ResourceLocation texture;
    private final TropicalFish.Pattern pattern;
    private final DyeColor baseColor;
    private final DyeColor patternColor;

    public TropicalFishVariant(ResourceLocation texture, TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        this.texture = texture;
        this.pattern = pattern;
        this.baseColor = baseColor;
        this.patternColor = patternColor;
    }

    public String toString() {
        return this.texture + "%" + this.pattern + "%" + this.baseColor + "%" + this.patternColor;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TropicalFishVariant that = (TropicalFishVariant) o;
        return Objects.equals(this.texture, that.texture) && this.pattern == that.pattern && this.baseColor == that.baseColor && this.patternColor == that.patternColor;
    }

    public int hashCode() {
        return Objects.hash(this.texture, this.pattern, this.baseColor, this.patternColor);
    }
}
