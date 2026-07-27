package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Crackiness;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/IronGolemVariant.class */
public class IronGolemVariant {
    private final ResourceLocation texture;
    private final Crackiness.Level cracks;

    public IronGolemVariant(ResourceLocation texture, Crackiness.Level cracks) {
        this.texture = texture;
        this.cracks = cracks;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + String.valueOf(this.cracks);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IronGolemVariant that = (IronGolemVariant) o;
        return Objects.equals(this.texture, that.texture) && this.cracks == that.cracks;
    }

    public int hashCode() {
        return Objects.hash(this.texture, this.cracks);
    }
}
