package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.render.TextureLocations;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryWithIcon.class */
public class EditorListEntryWithIcon extends EditorListEntryWithRootReference {
    private final int iconU;
    private final int iconV;
    private final int iconW;
    private final int iconH;

    public EditorListEntryWithIcon(int entryX, int entryY, int entryW, int entryH, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, int iconU, int iconV, int iconW, int iconH, EditorListRootEntry root, Supplier<Tooltip> tooltipSupplier) {
        super(entryX, entryY, entryW, entryH, index, rowList, root, tooltipSupplier);
        this.iconU = iconU;
        this.iconV = iconV;
        this.iconW = iconW;
        this.iconH = iconH;
    }

    public int getIconX() {
        return this.iconU;
    }

    public int getIconY() {
        return this.iconV;
    }

    public int getIconW() {
        return this.iconW;
    }

    public int getIconH() {
        return this.iconH;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        EditorListEntry result = super.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX, relativeMouseY, isMouseOver, partialTicks, font, globalMouseX, globalMouseY, includesSelected, isRoot);
        guiGraphics.blit(TextureLocations.GUI_TEXTURES, 0, 0, this.iconU, this.iconV, this.iconW, this.iconH);
        return result;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    protected boolean selectAction() {
        return false;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return Component.literal("unnamed");
    }
}
