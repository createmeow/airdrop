package xaero.lib.client.config.option.ui;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.client.config.option.ui.type.ConfigOptionUIType;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.client.gui.GuiConstants;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.widget.IClickableWidget;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/ConfigOptionScreenEntry.class */
public class ConfigOptionScreenEntry<T> implements ISettingEntry {
    private final ConfigChannel channel;
    private final ConfigOption<T> option;
    private final Supplier<Config> configSupplier;
    private final Supplier<Config> enforcedSupplier;
    private final Runnable onChange;
    private final boolean wrongContext;
    private final boolean clientSide;

    public ConfigOptionScreenEntry(ConfigOption<T> option, Supplier<Config> configSupplier, Supplier<Config> enforcedSupplier, Runnable onChange, ConfigChannel channel, boolean clientSide) {
        this(option, configSupplier, enforcedSupplier, onChange, channel, clientSide, false);
    }

    public ConfigOptionScreenEntry(ConfigOption<T> option, Supplier<Config> configSupplier, Supplier<Config> enforcedSupplier, Runnable onChange, ConfigChannel channel, boolean clientSide, boolean wrongContext) {
        this.option = option;
        this.configSupplier = configSupplier;
        this.enforcedSupplier = enforcedSupplier;
        this.onChange = onChange;
        this.channel = channel;
        this.clientSide = clientSide;
        this.wrongContext = wrongContext;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public String getStringForSearch() {
        Config config = this.configSupplier.get();
        ClientOptionValueRedirectorManager redirectorManager = this.clientSide ? this.channel.getClientConfigManager().getRedirectorManager() : null;
        String result = CommonComponents.optionNameValue(this.option.getDisplayName(), ConfigUtils.getEffectiveValueName(redirectorManager, this.option, config, this.enforcedSupplier.get())).getString();
        if (this.option.getDisplayName().getContents() instanceof TranslatableContents) {
            result = result + " " + this.option.getDisplayName().getContents().getKey().replace("gui.xaero", "");
        }
        Component optionTooltip = this.option.getTooltip();
        if (optionTooltip == null) {
            return result;
        }
        String result2 = result + " " + optionTooltip.getString();
        if (optionTooltip.getContents() instanceof TranslatableContents) {
            result2 = result2 + " " + optionTooltip.getContents().getKey().replace("gui.xaero", "");
        }
        return result2;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public AbstractWidget createWidget(int x, int y, int w) {
        Config config = this.configSupplier.get();
        ClientConfigOptionManager optionManager = (ClientConfigOptionManager) this.channel.getConfigOptionManager();
        ConfigOptionUIType<? super ConfigOption<T>> uiType = optionManager.getUIType(this.option);
        if (uiType == null) {
            throw new IllegalArgumentException("Config screen contains an option without a UI type: " + this.option.getId());
        }
        IClickableWidget iClickableWidgetCreate = uiType.getWidgetFactory().create(this.option, config, this.enforcedSupplier.get(), x, y, w, config.isAllowNullValues(), this.onChange, this.channel, this.clientSide);
        if (this.wrongContext) {
            iClickableWidgetCreate.setXaero_tooltip(new Tooltip(GuiConstants.SETTING_ENTRY_WRONG_CONTEXT_COMPONENT));
            ((AbstractWidget) iClickableWidgetCreate).active = false;
        }
        return iClickableWidgetCreate;
    }
}
