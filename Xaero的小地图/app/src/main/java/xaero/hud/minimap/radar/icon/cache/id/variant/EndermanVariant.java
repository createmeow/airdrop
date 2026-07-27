package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/EndermanVariant.class */
public class EndermanVariant {
    private final ResourceLocation texture;
    private final boolean angry;

    public EndermanVariant(ResourceLocation texture, boolean angry) {
        this.texture = texture;
        this.angry = angry;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + this.angry;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndermanVariant that = (EndermanVariant) o;
        return this.angry == that.angry && Objects.equals(this.texture, that.texture);
    }

    public int hashCode() {
        return Objects.hash(this.texture, Boolean.valueOf(this.angry));
    }
}
