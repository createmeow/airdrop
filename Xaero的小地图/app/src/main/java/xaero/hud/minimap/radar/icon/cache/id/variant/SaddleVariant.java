package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/SaddleVariant.class */
public class SaddleVariant {
    private final ResourceLocation texture;
    private final boolean saddled;

    public SaddleVariant(ResourceLocation texture, boolean saddled) {
        this.texture = texture;
        this.saddled = saddled;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + this.saddled;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaddleVariant that = (SaddleVariant) o;
        return this.saddled == that.saddled && Objects.equals(this.texture, that.texture);
    }

    public int hashCode() {
        return Objects.hash(this.texture, Boolean.valueOf(this.saddled));
    }
}
