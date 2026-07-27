package xaero.hud.minimap.radar.icon.cache.id.variant;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Markings;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/HorseVariant.class */
public class HorseVariant {
    public static final Map<Markings, ResourceLocation> HORSE_MARKINGS = (Map) Util.make(Maps.newEnumMap(Markings.class), map -> {
        map.put((EnumMap) Markings.NONE, (Markings) null);
        map.put((EnumMap) Markings.WHITE, (Markings) ResourceLocation.parse("textures/entity/horse/horse_markings_white.png"));
        map.put((EnumMap) Markings.WHITE_FIELD, (Markings) ResourceLocation.parse("textures/entity/horse/horse_markings_whitefield.png"));
        map.put((EnumMap) Markings.WHITE_DOTS, (Markings) ResourceLocation.parse("textures/entity/horse/horse_markings_whitedots.png"));
        map.put((EnumMap) Markings.BLACK_DOTS, (Markings) ResourceLocation.parse("textures/entity/horse/horse_markings_blackdots.png"));
    });
    private final ResourceLocation texture;
    private final Markings markings;

    public HorseVariant(ResourceLocation texture, Markings markings) {
        this.texture = texture;
        this.markings = markings;
    }

    public String toString() {
        return String.valueOf(this.texture) + "%" + String.valueOf(HORSE_MARKINGS.get(this.markings));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HorseVariant that = (HorseVariant) o;
        return Objects.equals(this.texture, that.texture) && this.markings == that.markings;
    }

    public int hashCode() {
        return Objects.hash(this.texture, this.markings);
    }
}
