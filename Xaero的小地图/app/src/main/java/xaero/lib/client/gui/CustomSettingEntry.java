package xaero.lib.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import xaero.lib.client.gui.widget.CycleButtonOption;
import xaero.lib.client.gui.widget.IClickableWidget;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.client.gui.widget.XaeroSliderWidget;
import xaero.lib.common.config.util.ConfigConstants;
import xaero.lib.common.gui.widget.TooltipInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/CustomSettingEntry.class */
public class CustomSettingEntry<T> implements ISettingEntry {
    private final BooleanSupplier allowNull;
    private final Component name;
    private final TooltipInfo tooltipInfo;
    private final boolean slider;
    private Supplier<T> currentValueSupplier;
    private final int minIndex;
    private final int maxIndex;
    private final IntFunction<T> indexReader;
    private final Function<T, Component> valueNamer;
    private final BiConsumer<T, T> onValueChange;
    private final BooleanSupplier activeSupplier;

    public CustomSettingEntry(BooleanSupplier allowNull, Component name, TooltipInfo tooltipInfo, boolean slider, Supplier<T> currentValueSupplier, int minIndex, int maxIndex, IntFunction<T> indexReader, Function<T, Component> valueNamer, BiConsumer<T, T> onValueChange, BooleanSupplier activeSupplier) {
        this.allowNull = allowNull;
        this.name = name;
        this.tooltipInfo = tooltipInfo;
        this.slider = slider;
        this.currentValueSupplier = currentValueSupplier;
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.indexReader = indexReader;
        this.valueNamer = valueNamer;
        this.onValueChange = onValueChange;
        this.activeSupplier = activeSupplier;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public String getStringForSearch() {
        Component displayName = this.name;
        T currentValue = this.currentValueSupplier.get();
        String result = CommonComponents.optionNameValue(displayName, currentValue == null ? ConfigConstants.UNSPECIFIED : (Component) this.valueNamer.apply(currentValue)).getString();
        if (displayName.getContents() instanceof TranslatableContents) {
            result = result + " " + displayName.getContents().getKey().replace("gui.xaero", "");
        }
        if (this.tooltipInfo != null) {
            result = result + " " + this.tooltipInfo.text.getString();
            if (this.tooltipInfo.text.getContents() instanceof TranslatableContents) {
                result = result + " " + this.tooltipInfo.text.getContents().getKey().replace("gui.xaero", "");
            }
        }
        return result;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public AbstractWidget createWidget(int x, int y, int w) {
        CycleButton xaeroSliderWidget;
        List<CycleButtonOption<T>> values = new ArrayList<>();
        CycleButtonOption<T> initialOption = null;
        int initialOptionIndex = -1;
        if (this.allowNull.getAsBoolean()) {
            values.add(new CycleButtonOption<>(null));
        }
        T currentValue = this.currentValueSupplier.get();
        for (int i = this.minIndex; i <= this.maxIndex; i++) {
            T value = this.indexReader.apply(i);
            CycleButtonOption<T> option = new CycleButtonOption<>(value);
            values.add(option);
            if (Objects.equals(value, currentValue)) {
                initialOption = option;
                initialOptionIndex = values.size() - 1;
            }
        }
        if (initialOption == null) {
            initialOption = values.get(0);
            initialOptionIndex = 0;
        }
        Component displayName = this.name;
        if (!this.slider) {
            xaeroSliderWidget = CycleButton.builder(co -> {
                return getValueName(co.get());
            }).withValues(values).withInitialValue(initialOption).create(x, y, w, 20, displayName, (cycleButton, cycleButtonOption) -> {
                T t = this.currentValueSupplier.get();
                if (this.onValueChange != null) {
                    this.onValueChange.accept(t, cycleButtonOption.get());
                }
            });
        } else {
            Supplier<Component> labelGetter = () -> {
                return CommonComponents.optionNameValue(displayName, getValueName(this.currentValueSupplier.get()));
            };
            double initialSliderValue = initialOptionIndex / (values.size() - 1);
            xaeroSliderWidget = new XaeroSliderWidget(x, y, w, 20, labelGetter.get(), initialSliderValue, d -> {
                CycleButtonOption cycleButtonOption2 = (CycleButtonOption) values.get((int) Math.round(d * (values.size() - 1)));
                T t = this.currentValueSupplier.get();
                if (this.onValueChange != null) {
                    this.onValueChange.accept(t, cycleButtonOption2.get());
                }
            }, labelGetter);
        }
        ((AbstractWidget) xaeroSliderWidget).active = this.activeSupplier.getAsBoolean();
        if (this.tooltipInfo != null) {
            ((IClickableWidget) xaeroSliderWidget).setXaero_tooltip(new Tooltip(this.tooltipInfo));
        }
        return xaeroSliderWidget;
    }

    public Component getValueName(T value) {
        if (value == null) {
            return ConfigConstants.UNSPECIFIED;
        }
        return this.valueNamer.apply(value);
    }
}
