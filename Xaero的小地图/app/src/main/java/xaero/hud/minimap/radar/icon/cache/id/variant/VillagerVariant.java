package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/VillagerVariant.class */
public class VillagerVariant {
    private final ResourceLocation texture;
    private final boolean baby;
    private final VillagerType type;
    private final VillagerProfession profession;
    private final int level;

    public VillagerVariant(ResourceLocation texture, boolean baby, VillagerType type, VillagerProfession profession, int level) {
        this.texture = texture;
        this.baby = baby;
        this.type = type;
        this.profession = profession;
        this.level = level;
    }

    public String toString() {
        return this.texture + "%" + this.baby + "%" + this.type + "%" + this.profession + "%" + this.level;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VillagerVariant that = (VillagerVariant) o;
        return this.baby == that.baby && this.level == that.level && Objects.equals(this.texture, that.texture) && this.type.equals(that.type) && this.profession.equals(that.profession);
    }

    public int hashCode() {
        return Objects.hash(this.texture, Boolean.valueOf(this.baby), this.type, this.profession, Integer.valueOf(this.level));
    }
}
