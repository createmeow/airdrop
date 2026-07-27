package xaero.lib.client.gui.widget.online;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import xaero.lib.client.gui.widget.online.init.WidgetInitializer;
import xaero.lib.client.gui.widget.online.render.WidgetRenderer;
import xaero.lib.patreon.Patreon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/WidgetScreenHandler.class */
public class WidgetScreenHandler {
    private List<Widget> widgets = new ArrayList();

    void addWidget(Widget widget) {
        if (widget != null) {
            this.widgets.add(widget);
        }
    }

    public void initialize(WidgetScreen screen, int width, int height) {
        WidgetInitializer widgetInit;
        int widgetLevel = Patreon.getOnlineWidgetLevel();
        for (Widget w : this.widgets) {
            if (w.getLocation().isAssignableFrom(screen.getClass()) && widgetLevel >= w.getMinWidgetLevel() && widgetLevel <= w.getMaxWidgetLevel() && (widgetInit = w.getType().widgetInit) != null) {
                widgetInit.init(screen, width, height, w);
            }
        }
    }

    public void render(GuiGraphics guiGraphics, WidgetScreen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
        WidgetRenderer renderer;
        int widgetLevel = Patreon.getOnlineWidgetLevel();
        for (Widget w : this.widgets) {
            if (w.getLocation().isAssignableFrom(screen.getClass()) && widgetLevel >= w.getMinWidgetLevel() && widgetLevel <= w.getMaxWidgetLevel() && (renderer = w.getType().widgetRenderer) != null) {
                renderer.render(guiGraphics, width, height, mouseX, mouseY, guiScale, w);
            }
        }
    }

    public boolean renderTooltips(GuiGraphics guiGraphics, Screen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
        int widgetLevel = Patreon.getOnlineWidgetLevel();
        boolean result = false;
        for (Widget w : this.widgets) {
            if (w.getLocation().isAssignableFrom(screen.getClass()) && widgetLevel >= w.getMinWidgetLevel() && widgetLevel <= w.getMaxWidgetLevel() && renderTooltip(guiGraphics, width, height, mouseX, mouseY, guiScale, w)) {
                result = true;
            }
        }
        return result;
    }

    private boolean renderTooltip(GuiGraphics guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
        if (widget.getOnHover() != HoverAction.TOOLTIP || widget.getTooltip() == null) {
            return false;
        }
        int x = widget.getBoxX(width, guiScale);
        int y = widget.getBoxY(height, guiScale);
        int w = widget.getBoxW(guiScale);
        int h = widget.getBoxH(guiScale);
        if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
            widget.getCursorBox().drawBox(guiGraphics, mouseX, mouseY, width, height);
            return true;
        }
        return false;
    }

    public void handleClick(Screen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
        int widgetLevel = Patreon.getOnlineWidgetLevel();
        for (Widget w : this.widgets) {
            if (w.getLocation().isAssignableFrom(screen.getClass()) && widgetLevel >= w.getMinWidgetLevel() && widgetLevel <= w.getMaxWidgetLevel()) {
                handleWidgetClick(screen, width, height, mouseX, mouseY, guiScale, w);
            }
        }
    }

    private void handleWidgetClick(Screen screen, int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
        WidgetClickHandler clickHandler;
        if (widget.getOnClick() == ClickAction.NOTHING || widget.getType() == WidgetType.BUTTON) {
            return;
        }
        int x = widget.getBoxX(width, guiScale);
        int y = widget.getBoxY(height, guiScale);
        int w = widget.getBoxW(guiScale);
        int h = widget.getBoxH(guiScale);
        if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h && (clickHandler = widget.getOnClick().clickHandler) != null) {
            clickHandler.onClick(screen, widget);
        }
    }
}
