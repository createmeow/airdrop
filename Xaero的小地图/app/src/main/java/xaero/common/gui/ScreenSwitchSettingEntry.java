package xaero.common.gui;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/ScreenSwitchSettingEntry.class */
public class ScreenSwitchSettingEntry implements ISettingEntry {
    private String name;
    private BiFunction<Screen, Screen, Screen> screenFactory;
    private Supplier<Tooltip> tooltipSupplier;
    private BooleanSupplier activeSupplier;
    private final boolean consideredAnExit;

    public ScreenSwitchSettingEntry(String name, BiFunction<Screen, Screen, Screen> screenFactoryFromCurrentAndEscape, Tooltip tooltip, boolean active) {
        this(name, screenFactoryFromCurrentAndEscape, tooltip, active, true);
    }

    public ScreenSwitchSettingEntry(String name, BiFunction<Screen, Screen, Screen> screenFactoryFromCurrentAndEscape, Tooltip tooltip, BooleanSupplier activeSupplier) {
        this(name, screenFactoryFromCurrentAndEscape, tooltip, activeSupplier, true);
    }

    public ScreenSwitchSettingEntry(String name, BiFunction<Screen, Screen, Screen> screenFactoryFromCurrentAndEscape, Tooltip tooltip, boolean active, boolean consideredAnExit) {
        this(name, screenFactoryFromCurrentAndEscape, tooltip, () -> {
            return active;
        }, consideredAnExit);
    }

    public ScreenSwitchSettingEntry(String name, BiFunction<Screen, Screen, Screen> screenFactoryFromCurrentAndEscape, Tooltip tooltip, BooleanSupplier activeSupplier, boolean consideredAnExit) {
        this.name = name;
        this.screenFactory = screenFactoryFromCurrentAndEscape;
        this.tooltipSupplier = () -> {
            return tooltip;
        };
        this.activeSupplier = activeSupplier;
        this.consideredAnExit = consideredAnExit;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public String getStringForSearch() {
        Tooltip entryTooltip = this.tooltipSupplier == null ? null : this.tooltipSupplier.get();
        String tooltipFullCode = entryTooltip == null ? null : entryTooltip.getFullCode();
        return I18n.get(this.name, new Object[0]) + " " + this.name.replace("gui.xaero", "") + (tooltipFullCode != null ? " " + tooltipFullCode.replace("gui.xaero", "") : "") + (entryTooltip != null ? " " + entryTooltip.getPlainText() : "");
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public AbstractWidget createWidget(int x, int y, int w) {
        TooltipButton button = new TooltipButton(x, y, w, 20, Component.translatable(this.name), b -> {
            Minecraft mc = Minecraft.getInstance();
            Screen current = mc.screen;
            Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
            Screen targetScreen = this.screenFactory.apply(current, currentEscScreen);
            if (this.consideredAnExit && (current instanceof ScreenBase)) {
                ((ScreenBase) current).onExit(targetScreen);
            } else {
                mc.setScreen(targetScreen);
            }
        }, this.tooltipSupplier);
        button.active = this.activeSupplier.getAsBoolean();
        return button;
    }

    public BiFunction<Screen, Screen, Screen> getScreenFactory() {
        return this.screenFactory;
    }
}
