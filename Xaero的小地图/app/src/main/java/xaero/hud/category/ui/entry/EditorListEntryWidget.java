package xaero.hud.category.ui.entry;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.joml.Vector4f;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.widget.EditorTextField;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryWidget.class */
public class EditorListEntryWidget extends EditorListEntryWithRootReference {
    protected AbstractWidget widget;
    private boolean widgetPressed;

    public EditorListEntryWidget(int entryX, int entryY, int entryW, int entryH, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, EditorListRootEntry root, AbstractWidget widget, Supplier<Tooltip> tooltipSupplier) {
        super(entryX, entryY, entryW, entryH, index, rowList, root, tooltipSupplier);
        this.widget = widget;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean mouseClicked(GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList.Entry entry, double relativeMouseX, double relativeMouseY, int i) {
        boolean result = super.mouseClicked(entry, relativeMouseX, relativeMouseY, i);
        if (result) {
            return true;
        }
        if ((this.widget instanceof AbstractButton) || !this.widget.isMouseOver(relativeMouseX, relativeMouseY)) {
            return false;
        }
        this.widgetPressed = true;
        return this.widget.mouseClicked(relativeMouseX, relativeMouseY, i);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean mouseReleased(double relativeMouseX, double relativeMouseY, int i) {
        if (this.widgetPressed) {
            this.widget.mouseReleased(relativeMouseX, relativeMouseY, i);
        }
        this.widgetPressed = false;
        super.mouseReleased(relativeMouseX, relativeMouseY, i);
        return false;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean mouseDragged(double relativeMouseX, double relativeMouseY, int i, double f, double g) {
        if (this.widgetPressed && this.widget.mouseDragged(relativeMouseX, relativeMouseY, i, f, g)) {
            return true;
        }
        return super.mouseDragged(relativeMouseX, relativeMouseY, i, f, g);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean mouseScrolled(double relativeMouseX, double relativeMouseY, double f, double g) {
        if (this.widget.isMouseOver(relativeMouseX, relativeMouseY) && this.widget.mouseScrolled(relativeMouseX, relativeMouseY, f, g)) {
            return true;
        }
        return super.mouseScrolled(relativeMouseX, relativeMouseY, f, g);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public void mouseMoved(double relativeMouseX, double relativeMouseY) {
        this.widget.mouseMoved(relativeMouseX, relativeMouseY);
        super.mouseMoved(relativeMouseX, relativeMouseY);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean keyPressed(int i, int j, int k, boolean isRoot) {
        if (this.widget.keyPressed(i, j, k)) {
            return true;
        }
        return super.keyPressed(i, j, k, isRoot);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean keyReleased(int i, int j, int k) {
        if (this.widget.keyReleased(i, j, k)) {
            return true;
        }
        return super.keyReleased(i, j, k);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean charTyped(char c, int i) {
        if (this.widget.charTyped(c, i)) {
            return true;
        }
        return super.charTyped(c, i);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public void tick() {
        super.tick();
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public String getNarration() {
        return super.getNarration();
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public String getHoverNarration() {
        return getNarration();
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return this.widget.getMessage();
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getNarrationMessage() {
        if (this.widget instanceof EditorTextField) {
            return this.widget.createNarrationMessage();
        }
        if (this.widget instanceof AbstractSliderButton) {
            return Component.translatable("gui.narrate.slider", new Object[]{getMessage()});
        }
        return Component.translatable("gui.narrate.button", new Object[]{getMessage()});
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        PoseStack poseStack = guiGraphics.pose();
        EditorListEntry result = super.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX, relativeMouseY, isMouseOver, partialTicks, font, globalMouseX, globalMouseY, includesSelected, isRoot);
        Vector4f widgetPos = new Vector4f(this.widget.getX(), this.widget.getY(), 0.0f, 1.0f);
        widgetPos.mul(poseStack.last().pose());
        int xBU = this.widget.getX();
        int yBU = this.widget.getY();
        this.widget.setX((int) widgetPos.x());
        this.widget.setY((int) widgetPos.y());
        poseStack.pushPose();
        poseStack.setIdentity();
        this.widget.render(guiGraphics, globalMouseX, globalMouseY, partialTicks);
        poseStack.popPose();
        this.widget.setX(xBU);
        this.widget.setY(yBU);
        if (this.widgetPressed) {
            return null;
        }
        return result;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public void setFocused(boolean bl) {
        if (this.widget.active && this.widget.visible && this.widget.isFocused() != bl) {
            this.widget.setFocused(bl);
        }
        super.setFocused(bl);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    protected boolean selectAction() {
        if (!(this.widget instanceof AbstractButton) || !this.widget.active) {
            return false;
        }
        this.widget.onPress();
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        return false;
    }
}
