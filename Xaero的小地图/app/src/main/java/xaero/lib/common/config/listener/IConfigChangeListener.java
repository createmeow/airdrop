package xaero.lib.common.config.listener;

import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/listener/IConfigChangeListener.class */
public interface IConfigChangeListener {
    void onChange(Config config, ConfigOption<?> configOption);

    void onFullChange(Config config);

    void onRemoved(Config config);
}
