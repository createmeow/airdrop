package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/LlamaVariant.class */
public class LlamaVariant {
    private final ResourceLocation texture;
    private final boolean trader;
    private final DyeColor swag;

    public LlamaVariant(ResourceLocation texture, boolean trader, DyeColor swag) {
        this.texture = texture;
        this.trader = trader;
        this.swag = swag;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + this.trader + "%" + String.valueOf(this.swag);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LlamaVariant that = (LlamaVariant) o;
        return this.trader == that.trader && Objects.equals(this.texture, that.texture) && this.swag == that.swag;
    }

    public int hashCode() {
        return Objects.hash(this.texture, Boolean.valueOf(this.trader), this.swag);
    }
}
