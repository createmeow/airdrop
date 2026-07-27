package xaero.hud.gui.util;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.gui.ScreenBase;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/gui/util/GuiUtils.class */
public class GuiUtils {
    public static float getMinimapScale(ClientConfigManager configManager) {
        int uiScaleConfigValue = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.UI_SCALE)).intValue();
        List<Integer> validValues = MinimapProfiledConfigOptions.UI_SCALE.getValidValues();
        int min = validValues.get(0).intValue();
        int max = validValues.get(validValues.size() - 1).intValue();
        return getUIScale(uiScaleConfigValue, min, max);
    }

    public static float getUIScale(int optionValue, int min, int max) {
        if (optionValue <= min) {
            return getAutoUIScale();
        }
        if (optionValue == max) {
            return (float) Minecraft.getInstance().getWindow().getGuiScale();
        }
        return optionValue;
    }

    public static int getAutoUIScale() {
        int height = Minecraft.getInstance().getWindow().getHeight();
        int width = Minecraft.getInstance().getWindow().getWidth();
        int size = height <= width ? height : width;
        if (size >= 1500) {
            int steps = size / RadarIconCreator.FAR_PLANE;
            return steps;
        }
        return 2;
    }

    public static void refreshScreenBase() {
        Screen currentScreen = Minecraft.getInstance().screen;
        ScreenBase screenBase = currentScreen instanceof ScreenBase ? (ScreenBase) currentScreen : null;
        if (screenBase == null) {
            return;
        }
        screenBase.refresh();
    }
}
