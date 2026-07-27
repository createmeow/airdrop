package xaero.lib.client;

import xaero.lib.XaeroLib;
import xaero.lib.client.config.sync.ClientConfigSynchronizer;
import xaero.lib.client.gui.widget.online.WidgetLoadingHandler;
import xaero.lib.client.gui.widget.online.WidgetScreenHandler;
import xaero.lib.patreon.Patreon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/XaeroLibClient.class */
public class XaeroLibClient {
    private Throwable firstStageError;
    private ClientConfigSynchronizer configSynchronizer;
    private WidgetScreenHandler widgetScreenHandler;
    private WidgetLoadingHandler widgetLoader;

    public void load() {
        try {
            XaeroLib.LOGGER.info("Loading XaeroLib client 1/2!");
            this.configSynchronizer = ClientConfigSynchronizer.Builder.begin().build();
            this.widgetScreenHandler = new WidgetScreenHandler();
            this.widgetLoader = new WidgetLoadingHandler(this.widgetScreenHandler);
            Patreon.checkPatreon();
        } catch (Throwable t) {
            this.firstStageError = t;
        }
    }

    public void loadLater() {
        if (this.firstStageError != null) {
            throw new RuntimeException(this.firstStageError);
        }
        XaeroLib.LOGGER.info("Loading XaeroLib client 2/2!");
    }

    public ClientConfigSynchronizer getConfigSynchronizer() {
        return this.configSynchronizer;
    }

    public WidgetScreenHandler getWidgetScreenHandler() {
        return this.widgetScreenHandler;
    }

    public WidgetLoadingHandler getWidgetLoader() {
        return this.widgetLoader;
    }
}
