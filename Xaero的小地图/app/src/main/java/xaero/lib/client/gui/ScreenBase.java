package xaero.lib.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import xaero.lib.XaeroLib;
import xaero.lib.client.gui.widget.ITooltipHaver;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/ScreenBase.class */
public class ScreenBase extends Screen implements IScreenBase {
    public Screen parent;
    public Screen escape;
    protected boolean canSkipWorldRender;
    protected DropDownWidget openDropdown;
    private List<DropDownWidget> dropdowns;
    protected boolean shouldRefresh;

    protected ScreenBase(Screen parent, Screen escape, Component titleIn) {
        super(titleIn);
        this.parent = parent;
        this.escape = escape;
        this.dropdowns = new ArrayList();
    }

    public void onExit(Screen screen) {
        this.minecraft.setScreen(screen);
    }

    public void goBack() {
        onExit(this.parent);
    }

    public void onClose() {
        onExit(this.escape);
    }

    public void renderEscapeScreen(GuiGraphics guiGraphics, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        if (this.escape != null) {
            this.escape.render(guiGraphics, p_230430_2_, p_230430_3_, p_230430_4_);
            guiGraphics.flush();
            GlStateManager._clear(256, Minecraft.ON_OSX);
        }
    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        handleRefresh();
        super.renderBackground(guiGraphics, mouseX, mouseY, partial);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        handleRefresh();
        PoseStack poseStack = guiGraphics.pose();
        super.render(guiGraphics, mouseX, mouseY, partial);
        renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
        for (DropDownWidget dropdown : this.dropdowns) {
            dropdown.render(guiGraphics, mouseX, mouseY, partial);
        }
        if (this.openDropdown != null) {
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 2.0f);
            this.openDropdown.render(guiGraphics, mouseX, mouseY, this.height, false);
            poseStack.popPose();
        }
    }

    protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
    }

    protected void init() {
        super.init();
        this.dropdowns.clear();
        this.openDropdown = null;
        if (this.escape != null) {
            this.escape.init(this.minecraft, this.width, this.height);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.openDropdown != null) {
            if (!this.openDropdown.onDropDown((int) mouseX, (int) mouseY, this.height)) {
                this.openDropdown.setClosed(true);
                this.openDropdown = null;
            } else {
                this.openDropdown.mouseClicked(mouseX, mouseY, button);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double g, double wheel) {
        if (this.openDropdown != null) {
            if (this.openDropdown.onDropDown((int) mouseX, (int) mouseY, this.height)) {
                return this.openDropdown.mouseScrolled(mouseX, mouseY, g, wheel);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, g, wheel);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.openDropdown != null && this.openDropdown.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override // xaero.lib.client.gui.IScreenBase
    public boolean shouldSkipWorldRender() {
        return this.canSkipWorldRender && (this.escape instanceof IScreenBase) && this.escape.shouldSkipWorldRender();
    }

    protected boolean renderTooltips(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        Tooltip tooltip;
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0.0d, 0.0d, 2.1d);
        boolean result = XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().renderTooltips(guiGraphics, this, this.width, this.height, par1, par2, this.minecraft.getWindow().getGuiScale());
        boolean mousePressed = GLFW.glfwGetMouseButton(this.minecraft.getWindow().getWindow(), 0) == 1;
        Iterator it = children().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ITooltipHaver iTooltipHaver = (GuiEventListener) it.next();
            if (iTooltipHaver instanceof AbstractWidget) {
                ITooltipHaver iTooltipHaver2 = (AbstractWidget) iTooltipHaver;
                if ((iTooltipHaver2 instanceof ITooltipHaver) && (!(iTooltipHaver2 instanceof AbstractSliderButton) || !mousePressed)) {
                    ITooltipHaver optionWidget = iTooltipHaver2;
                    if (par1 >= iTooltipHaver2.getX() && par2 >= iTooltipHaver2.getY() && par1 < iTooltipHaver2.getX() + iTooltipHaver2.getWidth() && par2 < iTooltipHaver2.getY() + iTooltipHaver2.getHeight() && optionWidget.getXaero_tooltip() != null && (tooltip = optionWidget.getXaero_tooltip().get()) != null) {
                        tooltip.drawBox(guiGraphics, par1, par2, this.width, this.height);
                        result = true;
                        break;
                    }
                }
            }
        }
        matrixStack.popPose();
        return result;
    }

    @Override // xaero.lib.client.gui.widget.dropdown.IDropDownContainer
    public void onDropdownOpen(DropDownWidget menu) {
        if (this.openDropdown != null && this.openDropdown != menu) {
            this.openDropdown.setClosed(true);
        }
        this.openDropdown = menu;
    }

    @Override // xaero.lib.client.gui.widget.dropdown.IDropDownContainer
    public void onDropdownClosed(DropDownWidget menu) {
        if (menu != this.openDropdown && this.openDropdown != null) {
            this.openDropdown.setClosed(true);
        }
        this.openDropdown = null;
    }

    protected <T extends GuiEventListener & NarratableEntry> T addWidget(T t) {
        if (t instanceof DropDownWidget) {
            this.dropdowns.add((DropDownWidget) t);
        }
        return (T) super.addWidget(t);
    }

    private void handleDropdownReplacement(AbstractWidget current, AbstractWidget replacement) {
        int dropdownIndex = this.dropdowns.indexOf(current);
        if (dropdownIndex != -1) {
            this.dropdowns.set(dropdownIndex, (DropDownWidget) replacement);
        }
        if (getFocused() == current) {
            setFocused(replacement);
        }
    }

    private void replaceWidget(AbstractWidget current, AbstractWidget replacement, boolean renderable) {
        int childIndex = children().indexOf(current);
        if (childIndex != -1) {
            super.removeWidget(current);
            if (renderable) {
                super.addRenderableWidget(replacement);
            } else {
                super.addWidget(replacement);
            }
            children().remove(replacement);
            children().add(childIndex, replacement);
        }
        handleDropdownReplacement(current, replacement);
    }

    public void replaceWidget(AbstractWidget current, AbstractWidget replacement) {
        replaceWidget(current, replacement, false);
    }

    public void replaceRenderableWidget(AbstractWidget current, AbstractWidget replacement) {
        replaceWidget(current, replacement, true);
    }

    protected void removeWidget(GuiEventListener current) {
        this.dropdowns.remove(current);
        super.removeWidget(current);
    }

    @Override // xaero.lib.client.gui.IScreenBase
    public Screen getEscape() {
        return this.escape;
    }

    public static Screen tryToGetEscape(Screen screen) {
        if (screen instanceof IScreenBase) {
            IScreenBase screenBase = (IScreenBase) screen;
            return screenBase.getEscape();
        }
        return null;
    }

    protected void rebuildWidgets() {
        super.rebuildWidgets();
        handleRefresh();
    }

    public void refresh() {
        this.shouldRefresh = true;
    }

    protected final void handleRefresh() {
        if (!this.shouldRefresh) {
            return;
        }
        this.shouldRefresh = false;
        int focusedIndex = getIndex(getFocused());
        rebuildWidgets();
        restoreFocus(focusedIndex);
    }

    public void restoreFocus(int index) {
        if (index == -1) {
            return;
        }
        try {
            GuiEventListener child = (GuiEventListener) children().get(index);
            setFocused(child);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public int getIndex(GuiEventListener child) {
        List<? extends GuiEventListener> children = children();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == child) {
                return i;
            }
        }
        return -1;
    }

    public boolean canSkipWorldRender() {
        return this.canSkipWorldRender;
    }
}
