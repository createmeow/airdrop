package xaero.lib.client.config.listener.handler;

import java.util.Iterator;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.client.config.sync.SyncedConfigManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/listener/handler/BuiltInConfigOptionClientHandlers.class */
public class BuiltInConfigOptionClientHandlers {
    private static boolean IGNORE_SERVER_ENFORCEMENT_RECURSION;

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleIgnoreServerEnforcement(ClientConfigChangeListener listener, Config config) {
        if (IGNORE_SERVER_ENFORCEMENT_RECURSION) {
            return;
        }
        ClientConfigManager manager = listener.getManager();
        ConfigChannel channel = manager.getChannel();
        SyncedConfigManager syncedManager = manager.getServerSynced();
        Iterator<ConfigOption<?>> it = channel.getConfigOptionManager().iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            if (option != BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR && syncedManager.getConfig().get(option) != null) {
                IGNORE_SERVER_ENFORCEMENT_RECURSION = true;
                listener.onChange(syncedManager.getConfig(), option);
                IGNORE_SERVER_ENFORCEMENT_RECURSION = false;
            }
        }
    }

    public static void registerAll(ClientConfigChangeListener listener) {
        listener.register(BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR, config -> {
            handleIgnoreServerEnforcement(listener, config);
        });
    }
}
