package xaero.hud.minimap.radar.util;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import xaero.common.minimap.MinimapProcessor;
import xaero.hud.minimap.MinimapLogs;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/util/RadarUtils.class */
public class RadarUtils {
    private static EntityDataAccessor<Optional<UUID>> FOX_TRUSTED_UUID_SECONDARY;
    private static EntityDataAccessor<Optional<UUID>> FOX_TRUSTED_UUID_MAIN;

    static {
        Field foxTrustSecondaryField = null;
        Field foxTrustMainField = null;
        try {
            foxTrustSecondaryField = ReflectionUtils.getFieldReflection(Fox.class, "DATA_TRUSTED_ID_0", "field_17951", "Lnet/minecraft/class_2940;", "f_28439_");
        } catch (Exception e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        try {
            foxTrustMainField = ReflectionUtils.getFieldReflection(Fox.class, "DATA_TRUSTED_ID_1", "field_17952", "Lnet/minecraft/class_2940;", "f_28440_");
        } catch (Exception e2) {
            MinimapLogs.LOGGER.error("suppressed exception", e2);
        }
        if (foxTrustSecondaryField != null) {
            FOX_TRUSTED_UUID_SECONDARY = (EntityDataAccessor) ReflectionUtils.getReflectFieldValue(0, foxTrustSecondaryField);
        }
        if (foxTrustMainField != null) {
            FOX_TRUSTED_UUID_MAIN = (EntityDataAccessor) ReflectionUtils.getReflectFieldValue(0, foxTrustMainField);
        }
    }

    public static double getMaxDistance(MinimapProcessor minimap, boolean circle) {
        int cullingSize = (minimap.getMinimapSize() / 2) + 48;
        if (!circle) {
            cullingSize = (int) (cullingSize * Math.sqrt(2.0d));
        }
        return (cullingSize * cullingSize) / (minimap.getMinimapZoom() * minimap.getMinimapZoom());
    }

    public static boolean isHostileException(Entity e) {
        if (e instanceof Piglin) {
            return ((Piglin) e).isBaby();
        }
        return false;
    }

    public static boolean isTamed(Entity e, Player p) {
        if (e instanceof TamableAnimal) {
            TamableAnimal tameable = (TamableAnimal) e;
            return tameable.isTame() && p.getUUID().equals(tameable.getOwnerUUID());
        }
        if (e instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) e;
            return horse.isTamed() && (horse.getOwnerUUID() == null || p.getUUID().equals(horse.getOwnerUUID()));
        }
        if (e instanceof Fox) {
            Fox fox = (Fox) e;
            if (FOX_TRUSTED_UUID_SECONDARY == null || !p.getUUID().equals(((Optional) fox.getEntityData().get(FOX_TRUSTED_UUID_SECONDARY)).orElse(null))) {
                return FOX_TRUSTED_UUID_MAIN != null && p.getUUID().equals(((Optional) fox.getEntityData().get(FOX_TRUSTED_UUID_MAIN)).orElse(null));
            }
            return true;
        }
        return false;
    }

    public static boolean isHostile(Entity e) {
        if (Minecraft.getInstance().level.getDifficulty() == Difficulty.PEACEFUL || isHostileException(e)) {
            return false;
        }
        return (e instanceof Monster) || (e instanceof Enemy) || e.getSoundSource() == SoundSource.HOSTILE;
    }

    public static String getCustomName(Entity e, boolean nullable) {
        Component c = e.getCustomName();
        if (c != null && (c.getContents() instanceof PlainTextContents)) {
            return c.getContents().text();
        }
        if (nullable) {
            return null;
        }
        return "{non-plain}";
    }
}
