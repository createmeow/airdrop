package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryCategory.class */
public class EditorListEntryCategory<C extends ObjectCategory<?, C>, ED extends EditorCategoryNode<C, ?, ED>> extends EditorListRootEntry {
    private static final Tooltip HELP_TOOLTIP = new Tooltip("gui.xaero_category_help2", Style.EMPTY, true);
    private static final Tooltip PROTECTED_TOOLTIP = new Tooltip("gui.xaero_category_protected_category", Style.EMPTY, true);
    private static final Tooltip UP_TOOLTIP = new Tooltip("gui.xaero_category_category_move_up", Style.EMPTY, true);
    private static final Tooltip DOWN_TOOLTIP = new Tooltip("gui.xaero_category_category_move_down", Style.EMPTY, true);

    /* JADX WARN: Multi-variable type inference failed */
    public EditorListEntryCategory(int screenWidth, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList, ConnectionLineType lineType, EditorCategoryNode<?, ?, ?> node, EditorCategoryNode<?, ?, ?> parent, Supplier<Tooltip> tooltipSupplier, boolean isFinalExpanded) {
        super(screenWidth, index, settingRowList, lineType, node);
        int subIndex = parent == null ? -1 : parent.getSubCategories().indexOf(node);
        boolean isCut = settingRowList.isCut(node);
        EditorCategoryNode cut = settingRowList.getCut();
        withSubEntry(getCategoryNameEntryFactory(node, settingRowList, isCut, tooltipSupplier));
        EditorListRootEntry.CenteredEntryFactory pasteEntryFactory = getPasteEntryFactory(cut, isCut, node, settingRowList);
        if (node.isExpanded() || !node.isMovable()) {
            if (settingRowList.hasCut()) {
                withSubEntry(pasteEntryFactory);
            }
            if (isFinalExpanded) {
                addHelpElement(HELP_TOOLTIP);
                return;
            }
            return;
        }
        if (!settingRowList.readOnly && !node.getSettingsNode().getProtection()) {
            withSubEntry(getDuplicateEntryFactory(subIndex, parent, settingRowList));
        }
        if (settingRowList.hasCut()) {
            withSubEntry(pasteEntryFactory);
        }
        if (node.getSettingsNode().getProtection()) {
            withSubEntry(getProtectedEntryFactory());
            return;
        }
        if (!settingRowList.readOnly && !settingRowList.hasCut()) {
            withSubEntry(getCutEntryFactory(node, parent, settingRowList));
        }
        if (settingRowList.readOnly || parent.getSubCategories().size() <= 1) {
            return;
        }
        withSubEntry(getPriorityEntryFactory(-1, parent, subIndex));
        withSubEntry(getPriorityEntryFactory(1, parent, subIndex));
    }

    private EditorListRootEntry.CenteredEntryFactory getCategoryNameEntryFactory(ED dataCast, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowListCast, boolean isCut, Supplier<Tooltip> tooltipSupplier) {
        return (x, y, w, h, root) -> {
            Runnable action = isCut ? () -> {
                rowListCast.pasteTo(dataCast);
            } : dataCast.getExpandAction(rowListCast);
            EditorListEntryTextWithAction result = new EditorListEntryTextWithAction(x, y, w, h, this.index, this.rowList, this, action, tooltipSupplier);
            if (isCut) {
                result.setColor(-5636096);
                result.setHoverColor(-43691);
            }
            return result;
        };
    }

    private EditorListRootEntry.CenteredEntryFactory getPasteEntryFactory(ED currentCut, boolean isCut, ED dataCast, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowListCast) {
        Tooltip pasteTooltip = getPasteTooltip(currentCut, isCut);
        if (pasteTooltip != null) {
            pasteTooltip.setAutoLinebreak(false);
        }
        return (x, y, w, h, root) -> {
            return new EditorListTextButtonEntry(x + 248, y + 2, this.index, this.rowList, Component.literal("←"), -5592406, -1, 5, dataCast.getPasteAction(rowListCast), this, pasteTooltip);
        };
    }

    private Tooltip getPasteTooltip(ED currentCut, boolean isCut) {
        if (currentCut == null) {
            return null;
        }
        if (isCut) {
            return new Tooltip("gui.xaero_category_paste_cancel", Style.EMPTY, true);
        }
        return new Tooltip((Component) Component.translatable("gui.xaero_category_paste", new Object[]{currentCut.getDisplayName(), this.node.getDisplayName()}), true);
    }

    private EditorListRootEntry.CenteredEntryFactory getDuplicateEntryFactory(int subIndex, ED parentCast, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowListCast) {
        Tooltip duplicateTooltip = new Tooltip((Component) Component.translatable("gui.xaero_category_duplicate", new Object[]{this.node.getDisplayName()}), true);
        duplicateTooltip.setAutoLinebreak(false);
        return (x, y, w, h, root) -> {
            return new EditorListTextButtonEntry(x + 230, y + 2, this.index, this.rowList, Component.literal("+"), -5592406, -1, 5, parentCast.getDuplicateAction(subIndex, rowListCast), this, duplicateTooltip);
        };
    }

    private EditorListRootEntry.CenteredEntryFactory getProtectedEntryFactory() {
        return (x, y, w, h, root) -> {
            return new EditorListTextButtonEntry(x - 24, y + 2, this.index, this.rowList, Component.literal("!"), -1644980, -171, 5, () -> {
                return false;
            }, this, PROTECTED_TOOLTIP);
        };
    }

    private EditorListRootEntry.CenteredEntryFactory getCutEntryFactory(ED dataCast, ED parentCast, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowListCast) {
        Tooltip cutTooltip = new Tooltip((Component) Component.translatable("gui.xaero_category_cut", new Object[]{this.node.getDisplayName()}), true);
        cutTooltip.setAutoLinebreak(false);
        return (x, y, w, h, root) -> {
            return new EditorListTextButtonEntry(x + 248, y + 2, this.index, this.rowList, Component.literal("↔"), -5592406, -1, 5, dataCast.getCutAction(parentCast, rowListCast), this, cutTooltip);
        };
    }

    private EditorListRootEntry.CenteredEntryFactory getPriorityEntryFactory(int direction, ED parentCast, int subIndex) {
        String label = direction < 0 ? "↑" : "↓";
        Tooltip tooltip = direction < 0 ? UP_TOOLTIP : DOWN_TOOLTIP;
        return (x, y, w, h, root) -> {
            return new EditorListTextButtonEntry((x - 32) + (8 * direction), y + 2, this.index, this.rowList, Component.literal(label), -5592406, -1, 5, parentCast.getMoveAction(subIndex, direction, this.rowList), this, tooltip);
        };
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return Component.literal("");
    }
}
