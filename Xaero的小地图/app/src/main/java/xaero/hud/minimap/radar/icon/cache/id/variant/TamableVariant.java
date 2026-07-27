package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/TamableVariant.class */
public class TamableVariant {
    private final ResourceLocation texture;
    private final boolean tame;

    public TamableVariant(ResourceLocation texture, boolean tame) {
        this.texture = texture;
        this.tame = tame;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + this.tame;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TamableVariant that = (TamableVariant) o;
        return this.tame == that.tame && Objects.equals(this.texture, that.texture);
    }

    public int hashCode() {
        return Objects.hash(this.texture, Boolean.valueOf(this.tame));
    }
}
