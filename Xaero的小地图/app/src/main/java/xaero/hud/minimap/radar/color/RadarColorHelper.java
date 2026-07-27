package xaero.hud.minimap.radar.color;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.state.RadarList;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/color/RadarColorHelper.class */
public class RadarColorHelper {
    public int getTeamColor(Entity e) {
        Integer teamColor = null;
        PlayerTeam team = e.getTeam();
        if (team != null) {
            teamColor = team.getColor().getColor();
        }
        if (teamColor == null) {
            return -1;
        }
        return teamColor.intValue();
    }

    public int getEntityColor(Entity entity, float offY, boolean cave, int heightLimit, int startFadingAt, boolean heightBasedFade, RadarColor radarColor, RadarColor fallbackColor) {
        int color = getRadarColorHex(entity, radarColor, fallbackColor);
        float heightFade = heightBasedFade ? getEntityHeightFade(offY, heightLimit, startFadingAt) : 1.0f;
        if (heightFade >= 1.0f) {
            return color;
        }
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        int alpha = 255;
        if (cave) {
            alpha = (int) (255 * heightFade);
        } else {
            red = (int) (red * heightFade);
            green = (int) (green * heightFade);
            blue = (int) (blue * heightFade);
        }
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private int getRadarColorHex(Entity entity, RadarColor radarColor, RadarColor fallbackColor) {
        if (radarColor != null) {
            return radarColor.getHex();
        }
        int entityTeamColour = getTeamColor(entity);
        if (entityTeamColour != -1) {
            return (-16777216) | entityTeamColour;
        }
        return fallbackColor.getHex();
    }

    public RadarColor getFallbackColor(RadarList radarList) {
        return getFallbackColor(radarList.getClientCategory(), radarList.getSyncedCategory());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public RadarColor getFallbackColor(EntityRadarCategory category, EntityRadarCategory syncedCategory) {
        int colorSetting;
        EntityRadarCategory fallbackCategory = syncedCategory == null ? category : syncedCategory;
        while (true) {
            EntityRadarCategory superCategory = (EntityRadarCategory) fallbackCategory.getSuperCategory();
            if (superCategory == null) {
                if (fallbackCategory == syncedCategory) {
                    superCategory = category;
                } else {
                    return RadarColor.WHITE;
                }
            }
            fallbackCategory = superCategory;
            Double categorySettingValue = (Double) fallbackCategory.getSettingValue(EntityRadarCategorySettings.COLOR);
            if (categorySettingValue != null && (colorSetting = categorySettingValue.intValue()) != -1) {
                return RadarColor.fromIndex(colorSetting);
            }
        }
    }

    public float getEntityHeightFade(float offY, int heightLimit, int startFadingAt) {
        float level = heightLimit - offY;
        if (level < 0.0f) {
            level = 0.0f;
        }
        float brightness = 1.0f;
        int threshold = startFadingAt == 0 ? (heightLimit * 3) / 4 : heightLimit - startFadingAt;
        if (level <= threshold) {
            brightness = 0.25f + ((0.5f * level) / threshold);
        }
        return brightness;
    }
}
