package xaero.lib.client.gui.widget.online;

import java.util.HashMap;
import java.util.Map;
import xaero.hud.io.HudIO;
import xaero.lib.XaeroLib;
import xaero.lib.client.gui.widget.online.loader.WidgetLoader;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/WidgetLoadingHandler.class */
public class WidgetLoadingHandler {
    private static int CURRENT_VERSION = 2;
    private WidgetScreenHandler handler;

    public WidgetLoadingHandler(WidgetScreenHandler destination) {
        this.handler = destination;
    }

    public void loadWidget(String serialized) {
        String[] args = serialized.split(HudIO.SEPARATOR);
        Map<String, String> parsedArgs = new HashMap<>();
        for (String arg : args) {
            int splitIndex = arg.indexOf(58);
            if (splitIndex != -1) {
                String parameter = arg.substring(0, splitIndex);
                String value = arg.substring(splitIndex + 1);
                parsedArgs.put(parameter, value);
            }
        }
        try {
            String min_version = parsedArgs.remove("min_version");
            String max_version = parsedArgs.remove("max_version");
            if (min_version == null || CURRENT_VERSION >= Integer.parseInt(min_version)) {
                if (max_version != null && CURRENT_VERSION > Integer.parseInt(max_version)) {
                    return;
                }
                WidgetType type = WidgetType.valueOf(parsedArgs.remove("type"));
                WidgetLoader loader = type.widgetLoader;
                Widget widget = loader.load(parsedArgs);
                this.handler.addWidget(widget);
            }
        } catch (Throwable t) {
            XaeroLib.LOGGER.error("suppressed exception", t);
        }
    }
}
