package xaero.lib.client.config.option.ui.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.client.gui.IScreenBase;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.client.gui.config.EditStringConfigOptionScreen;
import xaero.lib.client.gui.widget.CycleButtonOption;
import xaero.lib.client.gui.widget.IClickableWidget;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.client.gui.widget.XaeroSliderWidget;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.IndexedConfigOption;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/factory/StandardConfigWidgetFactories.class */
public class StandardConfigWidgetFactories {
    public static <T, CT extends IndexedConfigOption<T>> IConfigOptionWidgetFactory<CT> getIndexedCycleButtonFactory() {
        return (option, config, enforced, x, y, w, includeNullValue, onChange, channel, clientSide) -> {
            return createCycleButton(option, config, enforced, x, y, w, includeNullValue, option.getValidValues(), onChange, channel, clientSide);
        };
    }

    public static <T, CT extends IndexedConfigOption<T>> IConfigOptionWidgetFactory<CT> getIndexedSliderFactory() {
        return (option, config, enforced, x, y, w, includeNullValue, onChange, channel, clientSide) -> {
            return createSliderWithValues(option, config, enforced, x, y, w, includeNullValue, option.getValidValues(), onChange, channel, clientSide);
        };
    }

    public static <CT extends ConfigOption<?>> IConfigOptionWidgetFactory<CT> getStringEditFactory() {
        return StandardConfigWidgetFactories::createStringEditButton;
    }

    public static <CT extends ConfigOption<?>> IConfigOptionWidgetFactory<CT> getOpenScreenFactory(ICustomOptionEditScreenFactory<CT> screenFactory, ViewEnforcedCondition viewEnforcedCondition) {
        return (option, config, enforced, x, y, w, includeNullValue, onChange, channel, clientSide) -> {
            return createOpenScreenButton(option, config, enforced, x, y, w, includeNullValue, onChange, channel, clientSide, screenFactory, viewEnforcedCondition);
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T, CT extends ConfigOption<T>> AbstractWidget createCycleButton(CT option, Config config, Config enforced, int x, int y, int w, boolean includeNullValue, List<T> values, Runnable onChange, ConfigChannel channel, boolean clientSide) {
        List<CycleButtonOption<T>> allValues = new ArrayList<>();
        if (includeNullValue) {
            allValues.add(new CycleButtonOption<>(null));
        }
        Stream<R> map = values.stream().map(CycleButtonOption::new);
        Objects.requireNonNull(allValues);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        ClientOptionValueRedirectorManager redirectorManager = clientSide ? channel.getClientConfigManager().getRedirectorManager() : null;
        CycleButton<CycleButtonOption<T>> resultButton = CycleButton.builder(v -> {
            return ConfigUtils.getEffectiveValueName(redirectorManager, option, config, enforced);
        }).withValues(allValues).withInitialValue(new CycleButtonOption(config.get(option))).create(x, y, w, 20, option.getDisplayName(), (button, value) -> {
            if (clientSide && redirectorManager.redirectScreen(option)) {
                return;
            }
            config.set(option, value.get());
            button.setValue(value);
            onChange.run();
        });
        if (clientSide && redirectorManager.shouldDeactivateWidget(option)) {
            resultButton.active = false;
        }
        addTooltipToWidget(resultButton, option, enforced, redirectorManager, null);
        return resultButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T, CT extends ConfigOption<T>> AbstractWidget createSliderWithValues(CT option, Config config, Config enforced, int x, int y, int w, boolean includeNullValue, List<T> values, Runnable onChange, ConfigChannel channel, boolean clientSide) {
        double sliderValue;
        List<T> allValues = values;
        if (includeNullValue) {
            allValues = new ArrayList();
            allValues.add(null);
            allValues.addAll(values);
        }
        List<T> finalValues = allValues;
        Object obj = config.get(option);
        ClientOptionValueRedirectorManager redirectorManager = clientSide ? channel.getClientConfigManager().getRedirectorManager() : null;
        if (finalValues.size() == 1 || (clientSide && redirectorManager.shouldRedirect(option))) {
            sliderValue = 1.0d;
        } else {
            double sliderValue2 = allValues.indexOf(obj) / (finalValues.size() - 1);
            sliderValue = Mth.clamp(sliderValue2, 0.0d, 1.0d);
        }
        XaeroSliderWidget resultSlider = new XaeroSliderWidget(x, y, w, 20, getSliderLabel(option, config, enforced, channel, clientSide), sliderValue, newSliderValue -> {
            if (clientSide && redirectorManager.redirectScreen(option)) {
                return;
            }
            int newIndex = (int) Math.round(newSliderValue * (finalValues.size() - 1));
            Object obj2 = finalValues.get(newIndex);
            if (obj2 != config.get(option)) {
                config.set(option, obj2);
                if (Minecraft.getInstance().screen instanceof EditConfigScreen) {
                    ((EditConfigScreen) Minecraft.getInstance().screen).handleChangesOnExit();
                }
            }
        }, () -> {
            return getSliderLabel(option, config, enforced, channel, clientSide);
        });
        if (clientSide && redirectorManager.shouldDeactivateWidget(option)) {
            resultSlider.active = false;
        }
        addTooltipToWidget(resultSlider, option, enforced, redirectorManager, null);
        return resultSlider;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> Component getSliderLabel(ConfigOption<T> option, Config config, Config enforced, ConfigChannel channel, boolean clientSide) {
        ClientOptionValueRedirectorManager redirectorManager = clientSide ? channel.getClientConfigManager().getRedirectorManager() : null;
        return CommonComponents.optionNameValue(option.getDisplayName(), ConfigUtils.getEffectiveValueName(redirectorManager, option, config, enforced));
    }

    private static AbstractWidget createStringEditButton(ConfigOption<?> option, Config config, Config enforced, int x, int y, int w, boolean includeNullValue, Runnable onChange, ConfigChannel channel, boolean clientSide) {
        return createOpenScreenButton(option, config, enforced, x, y, w, includeNullValue, onChange, channel, clientSide, (parent, escape, config1, enforced1, option1, onChange1, readOnly, includeNullValue1) -> {
            return new EditStringConfigOptionScreen(parent, escape, config1, enforced1, option1, includeNullValue1, includeNullValue1, onChange1);
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <CT extends ConfigOption<?>> AbstractWidget createOpenScreenButton(CT option, Config config, Config enforced, int x, int y, int w, boolean includeNullValue, Runnable onChange, ConfigChannel channel, boolean clientSide, ICustomOptionEditScreenFactory<CT> screenFactory, ViewEnforcedCondition viewEnforcedCondition) {
        ClientOptionValueRedirectorManager redirectorManager = clientSide ? channel.getClientConfigManager().getRedirectorManager() : null;
        Component displayName = option.getDisplayName();
        boolean needsEnforcedIndicator = widgetNeedsEnforcedTooltipPrefix(option, enforced);
        if (needsEnforcedIndicator) {
            displayName = Component.translatable("gui.xaero_config_value_enforced", new Object[]{displayName.copy().withStyle(ChatFormatting.WHITE), Component.translatable("gui.xaero_config_value_enforced_indicator").withStyle(ChatFormatting.YELLOW)}).withStyle(ChatFormatting.YELLOW);
        }
        Button resultButton = Button.builder(displayName, b -> {
            if (clientSide && redirectorManager.redirectScreen(option)) {
                return;
            }
            IScreenBase iScreenBase = Minecraft.getInstance().screen;
            if (!(iScreenBase instanceof EditConfigScreen)) {
                return;
            }
            Screen escape = null;
            if (iScreenBase instanceof IScreenBase) {
                escape = iScreenBase.getEscape();
            }
            boolean readOnly = needsEnforcedIndicator && viewEnforcedCondition != null && viewEnforcedCondition.getCondition().getAsBoolean();
            Config screenConfig = readOnly ? enforced : config;
            Minecraft.getInstance().setScreen(screenFactory.get((EditConfigScreen) iScreenBase, escape, screenConfig, enforced, option, onChange, readOnly, includeNullValue));
        }).bounds(x, y, w, 20).build();
        if (clientSide && redirectorManager.shouldDeactivateWidget(option)) {
            resultButton.active = false;
        }
        addTooltipToWidget(resultButton, option, enforced, redirectorManager, viewEnforcedCondition);
        return resultButton;
    }

    private static <CT extends ConfigOption<?>> void addTooltipToWidget(AbstractWidget widget, CT option, Config enforced, ClientOptionValueRedirectorManager redirectorManager, ViewEnforcedCondition viewEnforcedCondition) {
        MutableComponent tooltipComponent;
        if (!(widget instanceof IClickableWidget)) {
            return;
        }
        if (redirectorManager != null && redirectorManager.shouldRedirect(option)) {
            ((IClickableWidget) widget).setXaero_tooltip(new Tooltip(redirectorManager.getTooltip(option)));
            return;
        }
        MutableComponent tooltipComponent2 = null;
        if (widgetNeedsEnforcedTooltipPrefix(option, enforced)) {
            if (viewEnforcedCondition != null) {
                tooltipComponent = viewEnforcedCondition.getTooltip().copy();
            } else {
                tooltipComponent = Component.translatable("gui.xaero_config_value_enforced_tooltip_prefix");
            }
            tooltipComponent2 = tooltipComponent.withStyle(ChatFormatting.YELLOW);
        }
        if (option.getTooltip() != null) {
            MutableComponent mainTooltip = option.getTooltip().copy().withStyle(ChatFormatting.WHITE);
            if (tooltipComponent2 == null) {
                tooltipComponent2 = mainTooltip.copy();
            } else {
                tooltipComponent2.getSiblings().add(Component.literal(" \n \n "));
                tooltipComponent2.getSiblings().add(mainTooltip);
            }
        }
        if (tooltipComponent2 == null) {
            return;
        }
        ((IClickableWidget) widget).setXaero_tooltip(new Tooltip((Component) tooltipComponent2));
    }

    public static boolean widgetNeedsEnforcedTooltipPrefix(ConfigOption<?> option, Config enforced) {
        return (enforced == null || option == BuiltInProfiledConfigOptions.PROFILE_NAME || enforced.get(option) == null) ? false : true;
    }
}
