package xaero.lib.client.config.option.value.redirect;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirector;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirector;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/value/redirect/ClientOptionValueRedirectorManager.class */
public class ClientOptionValueRedirectorManager extends OptionValueRedirectorManager {
    protected ClientOptionValueRedirectorManager(Map<ConfigOption<?>, OptionValueRedirector<?>> redirectors, Config cacheConfig) {
        super(redirectors, cacheConfig);
    }

    public <T> void register(ConfigOption<T> option, Supplier<T> redirectSupplier, Predicate<ConfigChannel> condition, Function<Screen, Screen> screenRedirector, Component name, Supplier<Component> tooltip) {
        checkDuplicates(option);
        this.redirectors.put(option, ClientOptionValueRedirector.Builder.begin().setScreenRedirection(screenRedirector).setSupplier(redirectSupplier).setCondition(condition).setName(name).setTooltip(tooltip).build());
    }

    @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager
    public <T> void register(ConfigOption<T> option, Supplier<T> redirectSupplier, Predicate<ConfigChannel> condition) {
        throw new IllegalArgumentException("Use the longer client register() method instead.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager
    public <T> ClientOptionValueRedirector<T> get(ConfigOption<T> option) {
        return (ClientOptionValueRedirector) super.get((ConfigOption) option);
    }

    public Screen redirectScreen(ConfigOption<?> option, Screen currentScreen) {
        ClientOptionValueRedirector<?> redirector = get((ConfigOption) option);
        if (redirector == null) {
            return currentScreen;
        }
        return redirector.getScreenRedirection(currentScreen);
    }

    public Component getName(ConfigOption<?> option) {
        ClientOptionValueRedirector<?> redirector = get((ConfigOption) option);
        if (redirector == null) {
            return null;
        }
        return redirector.getName();
    }

    public Component getTooltip(ConfigOption<?> option) {
        ClientOptionValueRedirector<?> redirector = get((ConfigOption) option);
        if (redirector == null) {
            return null;
        }
        return redirector.getTooltip();
    }

    public boolean redirectScreen(ConfigOption<?> option) {
        Screen currentScreen = Minecraft.getInstance().screen;
        if (!shouldRedirect(option)) {
            return false;
        }
        Screen redirectScreen = redirectScreen(option, currentScreen);
        if (redirectScreen == null) {
            return true;
        }
        if (redirectScreen == currentScreen) {
            return false;
        }
        Minecraft.getInstance().setScreen(redirectScreen);
        return true;
    }

    public boolean shouldDeactivateWidget(ConfigOption<?> option) {
        return shouldRedirect(option) && redirectScreen(option, null) == null;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/value/redirect/ClientOptionValueRedirectorManager$Builder.class */
    public static final class Builder extends OptionValueRedirectorManager.Builder<Builder> {
        @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager.Builder
        protected /* bridge */ /* synthetic */ OptionValueRedirectorManager buildInternally(Map map, Config config) {
            return buildInternally((Map<ConfigOption<?>, OptionValueRedirector<?>>) map, config);
        }

        private Builder() {
        }

        @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager.Builder
        protected ClientOptionValueRedirectorManager buildInternally(Map<ConfigOption<?>, OptionValueRedirector<?>> redirectors, Config cacheConfig) {
            return new ClientOptionValueRedirectorManager(redirectors, cacheConfig);
        }

        @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager.Builder
        public ClientOptionValueRedirectorManager build() {
            return (ClientOptionValueRedirectorManager) super.build();
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
