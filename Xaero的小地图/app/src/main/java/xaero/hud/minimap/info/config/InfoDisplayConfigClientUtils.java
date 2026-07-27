package xaero.hud.minimap.info.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.common.config.info.config.InfoDisplayConfigData;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.InfoDisplayManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/config/InfoDisplayConfigClientUtils.class */
public class InfoDisplayConfigClientUtils {
    public static <T> InfoDisplayConfigData createDefaultConfig(InfoDisplay<T> infoDisplay, ModSettings legacySettings, boolean clientSide) {
        String strEncode;
        T legacyValue = clientSide ? infoDisplay.getLegacyValue(legacySettings) : null;
        Integer num = !clientSide ? null : -1;
        Integer num2 = !clientSide ? null : 15;
        if (clientSide) {
            strEncode = infoDisplay.getCodec().encode(legacyValue == null ? infoDisplay.getDefaultState() : legacyValue, null, null);
        } else {
            strEncode = null;
        }
        return new InfoDisplayConfigData(num, num2, strEncode);
    }

    public static InfoDisplayManagerConfigData createDefaultConfig(InfoDisplayManager manager, ModSettings legacySettings, boolean clientSide) {
        List<String> order = new ArrayList<>(manager.getDefaultOrder());
        Map<String, InfoDisplayConfigData> configs = new HashMap<>();
        manager.getStream().forEach(infoDisplay -> {
            configs.put(infoDisplay.getId(), createDefaultConfig(infoDisplay, legacySettings, clientSide));
        });
        return new InfoDisplayManagerConfigData(order, configs);
    }
}
