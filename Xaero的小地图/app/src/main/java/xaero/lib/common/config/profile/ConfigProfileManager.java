package xaero.lib.common.config.profile;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.listener.IConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/ConfigProfileManager.class */
public final class ConfigProfileManager implements Iterable<ConfigProfile> {
    public final Logger logger;
    private final Map<String, ConfigProfile> profiles;
    private final List<String> order;
    private final Object2IntMap<String> orderLookup;
    private final String configType;
    private final ConfigListener configListener = new ConfigListener();
    private IConfigChangeListener changeListener;

    private ConfigProfileManager(Logger logger, Map<String, ConfigProfile> profiles, List<String> order, Object2IntMap<String> orderLookup, String configType) {
        this.logger = logger;
        this.profiles = profiles;
        this.order = order;
        this.orderLookup = orderLookup;
        this.configType = configType;
    }

    public void add(ConfigProfile profile) {
        add(this.order.size(), profile);
    }

    public void add(int index, ConfigProfile profile) {
        if (this.profiles.containsKey(profile.getId())) {
            throw new IllegalArgumentException();
        }
        this.profiles.put(profile.getId(), profile);
        this.order.add(index, profile.getId());
        updateOrderLookup(index);
        profile.setChangeListener(this.configListener);
    }

    public ConfigProfile get(String id) {
        return this.profiles.get(id);
    }

    public void remove(String id) {
        if (!this.profiles.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        int index = this.orderLookup.getInt(id);
        ConfigProfile removed = this.profiles.remove(id);
        this.order.remove(index);
        this.orderLookup.removeInt(id);
        updateOrderLookup(index);
        if (removed.getChangeListener() == this.configListener) {
            removed.setChangeListener(null);
        }
        if (this.changeListener == null) {
            return;
        }
        this.changeListener.onRemoved(removed);
    }

    public int getIndex(String id) {
        return this.orderLookup.getInt(id);
    }

    private void updateOrderLookup(int startIndex) {
        for (int i = startIndex; i < this.order.size(); i++) {
            String otherId = this.order.get(i);
            this.orderLookup.put(otherId, i);
        }
    }

    @Override // java.lang.Iterable
    public Iterator<ConfigProfile> iterator() {
        Stream<String> stream = this.order.stream();
        Map<String, ConfigProfile> map = this.profiles;
        Objects.requireNonNull(map);
        return stream.map((v1) -> {
            return r1.get(v1);
        }).iterator();
    }

    public String getConfigType() {
        return this.configType;
    }

    public void setChangeListener(IConfigChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void reset() {
        this.profiles.clear();
        this.orderLookup.clear();
        this.order.clear();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/ConfigProfileManager$ConfigListener.class */
    private final class ConfigListener implements IConfigChangeListener {
        private ConfigListener() {
        }

        @Override // xaero.lib.common.config.listener.IConfigChangeListener
        public void onChange(Config config, ConfigOption<?> option) {
            if (ConfigProfileManager.this.changeListener == null) {
                return;
            }
            ConfigProfileManager.this.changeListener.onChange(config, option);
        }

        @Override // xaero.lib.common.config.listener.IConfigChangeListener
        public void onFullChange(Config config) {
            if (ConfigProfileManager.this.changeListener == null) {
                return;
            }
            ConfigProfileManager.this.changeListener.onFullChange(config);
        }

        @Override // xaero.lib.common.config.listener.IConfigChangeListener
        public void onRemoved(Config config) {
            if (ConfigProfileManager.this.changeListener == null) {
                return;
            }
            ConfigProfileManager.this.changeListener.onRemoved(config);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/ConfigProfileManager$Builder.class */
    public static final class Builder {
        private String configType;
        private Logger logger;

        private Builder() {
        }

        public Builder setDefault() {
            setConfigType(null);
            setLogger(null);
            return this;
        }

        public Builder setConfigType(String configType) {
            this.configType = configType;
            return this;
        }

        public Builder setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public ConfigProfileManager build() {
            if (this.configType == null || this.logger == null) {
                throw new IllegalStateException();
            }
            return new ConfigProfileManager(this.logger, new HashMap(), new ArrayList(), new Object2IntOpenHashMap(), this.configType);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
