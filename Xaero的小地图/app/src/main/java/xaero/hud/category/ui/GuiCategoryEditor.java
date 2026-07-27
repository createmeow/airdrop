package xaero.hud.category.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.ObjectCategory.Builder;
import xaero.hud.category.ui.entry.ConnectionLineType;
import xaero.hud.category.ui.entry.EditorListEntry;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.hud.category.ui.node.EditorCategoryNode.Builder;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSettingsNode;
import xaero.hud.category.ui.node.EditorSettingsNode.Builder;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/GuiCategoryEditor.class */
public abstract class GuiCategoryEditor<C extends ObjectCategory<?, C>, ED extends EditorCategoryNode<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends EditorSettingsNode<?>, SDB extends EditorSettingsNode.Builder<SD, SDB>, EDB extends EditorCategoryNode.Builder<C, ED, SD, SDB, EDB>> extends ScreenBase {
    public static final Component READ_ONLY_COMPONENT = Component.translatable("gui.xaero_category_editor_read_only").withStyle(ChatFormatting.YELLOW);
    private static final int FRAME_TOP_SIZE = 32;
    private static final int FRAME_BOTTOM_SIZE = 48;
    public static final int ROW_HEIGHT = 24;
    public static final int ROW_WIDTH = 220;
    private GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList rowList;
    private final EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> dataConverter;
    private ED editorData;
    protected ED cutCategory;
    protected ED cutCategorySuper;
    protected final IXaeroMinimap modMain;
    protected final boolean readOnly;

    protected abstract ED constructEditorData(EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> editorCategoryNodeConverter);

    protected abstract ED constructDefaultData(EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> editorCategoryNodeConverter);

    protected abstract void onConfigConfirmed(C c);

    protected GuiCategoryEditor(IXaeroMinimap iXaeroMinimap, Screen screen, Screen screen2, Component component, EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> editorCategoryNodeConverter, boolean z) {
        super(screen, screen2, component);
        this.modMain = iXaeroMinimap;
        this.dataConverter = editorCategoryNodeConverter;
        this.readOnly = z;
        this.editorData = (ED) constructEditorData(editorCategoryNodeConverter);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        if (this.readOnly) {
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_back"), b -> {
                cancel(this.parent);
            }).bounds((this.width / 2) - 100, this.height - FRAME_TOP_SIZE, 200, 20).build());
        } else {
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_category_settings_cancel"), b2 -> {
                this.minecraft.setScreen(new ConfirmScreen(result -> {
                    if (result) {
                        cancel(this.parent);
                    } else {
                        this.minecraft.setScreen(this);
                    }
                }, Component.translatable("gui.xaero_category_settings_cancel_confirm"), Component.literal("")));
            }).bounds((this.width / 2) + 5, this.height - FRAME_TOP_SIZE, 150, 20).build());
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_category_settings_confirm"), b3 -> {
                confirm();
            }).bounds((this.width / 2) - 155, this.height - FRAME_TOP_SIZE, 150, 20).build());
        }
        Button resetButton = Button.builder(Component.translatable("gui.xaero_category_settings_reset"), b4 -> {
            if (this.readOnly) {
                return;
            }
            this.minecraft.setScreen(new ConfirmScreen(z -> {
                if (z) {
                    this.editorData = (ED) constructDefaultData(this.dataConverter);
                }
                this.minecraft.setScreen(this);
            }, Component.translatable("gui.xaero_category_settings_reset_confirm1"), Component.translatable("gui.xaero_category_settings_reset_confirm2")));
        }).bounds(6, 6, 120, 20).build();
        resetButton.active = !this.readOnly;
        addRenderableWidget(resetButton);
        this.rowList = new SettingRowList(this.dataConverter);
        addWidget(this.rowList);
    }

    private void confirm() {
        super.onExit(this.parent);
        if (!this.readOnly) {
            onConfigConfirmed(this.dataConverter.getConfiguredBuilder(this.editorData).build());
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void onExit(Screen screen) {
        if (this.readOnly) {
            super.onExit(screen);
        } else {
            this.minecraft.setScreen(new ConfirmScreen(this, result -> {
                if (result) {
                    confirm();
                }
                cancel(screen);
            }, Component.translatable("gui.xaero_category_settings_save_confirm"), Component.literal("")) { // from class: xaero.hud.category.ui.GuiCategoryEditor.1
                public boolean keyPressed(int i, int j, int k) {
                    if (i == 256) {
                        return true;
                    }
                    return super.keyPressed(i, j, k);
                }
            });
        }
    }

    protected void cancel(Screen screen) {
        super.onExit(screen);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        this.rowList.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.minecraft.font, this.title, this.width / 2, 5, 16777215);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        Supplier<Tooltip> tooltipSupplier;
        Tooltip tooltip;
        PoseStack poseStack = guiGraphics.pose();
        super.render(guiGraphics, i, j, f);
        if (this.readOnly) {
            guiGraphics.drawString(this.font, READ_ONLY_COMPONENT, (this.width - 5) - this.font.width(READ_ONLY_COMPONENT), 5, -1);
        }
        if (((SettingRowList) this.rowList).hovered == null || (tooltipSupplier = ((SettingRowList) this.rowList).hovered.getTooltipSupplier()) == null || (tooltip = tooltipSupplier.get()) == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0d, 0.0d, 0.1d);
        tooltip.drawBox(guiGraphics, i, j, this.width, this.height);
        poseStack.popPose();
    }

    public boolean keyPressed(int i, int j, int k) {
        if (this.rowList.isFocused() && i == 257 && this.rowList.confirmSelection()) {
            return true;
        }
        return super.keyPressed(i, j, k);
    }

    public void tick() {
        this.rowList.tick();
        super.tick();
    }

    public GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList getRowList() {
        return this.rowList;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/GuiCategoryEditor$SettingRowList.class */
    public class SettingRowList extends ObjectSelectionList<GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry> {
        private EditorNode lastExpandedData;
        private boolean restoreScrollAfterUpdate;
        private EditorListEntry hovered;
        private final EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> dataConverter;
        public final boolean readOnly;
        private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");
        private static final Component LEFT_RIGHT_USAGE = Component.translatable("narration.xaero_ui_list_left_right_usage");

        public SettingRowList(EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> dataConverter) {
            super(GuiCategoryEditor.this.minecraft, GuiCategoryEditor.this.width, Math.max(4, (GuiCategoryEditor.this.height - GuiCategoryEditor.FRAME_BOTTOM_SIZE) - GuiCategoryEditor.FRAME_TOP_SIZE), GuiCategoryEditor.FRAME_TOP_SIZE, 24);
            this.dataConverter = dataConverter;
            this.readOnly = GuiCategoryEditor.this.readOnly;
            updateEntries();
        }

        protected boolean isSelectedItem(int i) {
            return false;
        }

        public boolean hasCut() {
            if (GuiCategoryEditor.this.cutCategory == null) {
                return false;
            }
            if (GuiCategoryEditor.this.cutCategorySuper.getSubCategories().contains(GuiCategoryEditor.this.cutCategory)) {
                return true;
            }
            setCutCategory(null, null);
            return false;
        }

        public ED getCut() {
            return GuiCategoryEditor.this.cutCategory;
        }

        public boolean isCut(ED category) {
            if (GuiCategoryEditor.this.cutCategory == category) {
                return hasCut();
            }
            return false;
        }

        public void setCutCategory(ED cutCategory, ED cutCategorySuper) {
            GuiCategoryEditor.this.cutCategory = cutCategory;
            GuiCategoryEditor.this.cutCategorySuper = cutCategorySuper;
        }

        public void pasteTo(ED destination) {
            if (GuiCategoryEditor.this.cutCategory == null) {
                return;
            }
            if (destination == GuiCategoryEditor.this.cutCategory || destination == GuiCategoryEditor.this.cutCategorySuper) {
                setCutCategory(null, null);
                updateEntries();
                return;
            }
            destination.getExpandAction(this).run();
            setLastExpandedData(GuiCategoryEditor.this.cutCategory);
            GuiCategoryEditor.this.cutCategorySuper.getSubCategories().remove(GuiCategoryEditor.this.cutCategory);
            destination.getSubCategories().add(0, GuiCategoryEditor.this.cutCategory);
            setCutCategory(null, null);
        }

        public boolean isFocused() {
            return GuiCategoryEditor.this.getFocused() == this;
        }

        public void setLastExpandedData(EditorNode lastExpandedData) {
            this.lastExpandedData = lastExpandedData;
        }

        public void restoreScrollAfterUpdate() {
            this.restoreScrollAfterUpdate = true;
        }

        public void updateEntries() {
            double scrollBackup = getScrollAmount();
            clearEntries();
            GuiCategoryEditor.this.editorData.setExpanded(true);
            addEntriesForExpanded(GuiCategoryEditor.this.editorData, null);
            if (getSelected() != null) {
                centerScrollOn(getSelected());
            }
            if (this.restoreScrollAfterUpdate) {
                setScrollAmount(scrollBackup);
                this.restoreScrollAfterUpdate = false;
            }
        }

        private void addEntriesForExpanded(EditorNode data, EditorNode parent) {
            int nextIndex = children().size();
            List<EditorNode> subExpandables = data.getSubNodes();
            if (subExpandables == null) {
                return;
            }
            EditorNode expandedData = null;
            Iterator<EditorNode> it = subExpandables.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                EditorNode sed = it.next();
                if (sed.isExpanded()) {
                    expandedData = sed;
                    break;
                }
            }
            EditorListRootEntry wrappedEntry = data.getListEntryFactory().get(data, parent, nextIndex, nextIndex == 0 ? ConnectionLineType.NONE : ConnectionLineType.PATH, this, this.width, expandedData == null);
            int nextIndex2 = nextIndex + 1;
            GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry currentEntry = new Entry(wrappedEntry, nextIndex);
            addEntry(currentEntry);
            if (data == this.lastExpandedData) {
                setFocused(currentEntry);
            }
            if (expandedData != null) {
                addEntriesForExpanded(expandedData, data);
                return;
            }
            if (this.lastExpandedData == null && data.isExpanded()) {
                setFocused(currentEntry);
            }
            boolean first = true;
            for (EditorNode sed2 : subExpandables) {
                EditorListRootEntry wrappedEntry2 = sed2.getListEntryFactory().get(sed2, data, nextIndex2, first ? ConnectionLineType.HEAD_LEAF : ConnectionLineType.TAIL_LEAF, this, this.width, false);
                int i = nextIndex2;
                nextIndex2++;
                GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry leafEntry = new Entry(wrappedEntry2, i);
                addEntry(leafEntry);
                if (sed2 == this.lastExpandedData) {
                    setFocused(leafEntry);
                }
                first = false;
            }
        }

        public boolean mouseClicked(double d, double e, int i) {
            if (!isMouseOver(d, e)) {
                setFocused(null);
            }
            return super.mouseClicked(d, e, i);
        }

        public void mouseMoved(double d, double e) {
            if (getSelected() != null) {
                getSelected().mouseMoved(d, e);
            }
            super.mouseMoved(d, e);
        }

        public boolean keyReleased(int i, int j, int k) {
            if (getSelected() != null && getSelected().keyReleased(i, j, k)) {
                return true;
            }
            return super.keyReleased(i, j, k);
        }

        public boolean charTyped(char c, int i) {
            if (getSelected() != null) {
                boolean result = getSelected().charTyped(c, i);
                if (result) {
                    return true;
                }
            }
            return super.charTyped(c, i);
        }

        public void tick() {
            if (getSelected() != null) {
                getSelected().tick();
            }
        }

        public boolean confirmSelection() {
            GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry = getSelected();
            if (entry == null) {
                return false;
            }
            EditorListEntry selectedSubEntry = ((Entry) entry).wrappedEntry.confirmSelection();
            return selectedSubEntry != null;
        }

        public void setFocused(GuiEventListener guiEventListener) {
            if (!children().contains(guiEventListener) || getFocused() == guiEventListener) {
                return;
            }
            if (getSelected() != null) {
                getSelected().wrappedEntry.unfocusRecursively();
            }
            if (getFocused() != null) {
                getFocused().setFocused(false);
            }
            GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry = (Entry) guiEventListener;
            if (entry != null) {
                ((Entry) entry).wrappedEntry.focusFirstRecursively();
            }
            super.setFocused(guiEventListener);
            if (guiEventListener == null) {
                setSelected((Entry) null);
            }
            narrateSelection();
        }

        public void setSelected(GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry) {
            super.setSelected(entry);
        }

        public int getRowWidth() {
            return this.width;
        }

        protected int getScrollbarPosition() {
            return (this.width / 2) + 164;
        }

        public void narrateSelection() {
            GuiCategoryEditor.this.afterKeyboardAction();
        }

        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            super.updateWidgetNarration(narrationElementOutput);
            if (isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE, new Component[]{USAGE_NARRATION, LEFT_RIGHT_USAGE});
            }
        }

        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            this.hovered = null;
            super.renderWidget(guiGraphics, i, j, f);
        }

        public EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> getDataConverter() {
            return this.dataConverter;
        }

        /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/GuiCategoryEditor$SettingRowList$Entry.class */
        public class Entry extends ObjectSelectionList.Entry<GuiCategoryEditor<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry> {
            private EditorListRootEntry wrappedEntry;
            private int index;
            private int lastX;
            private int lastY;

            public Entry(EditorListRootEntry entryInfo, int index) {
                this.wrappedEntry = entryInfo;
                this.index = index;
            }

            public void render(GuiGraphics guiGraphics, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                PoseStack poseStack = guiGraphics.pose();
                this.lastX = x;
                this.lastY = y;
                poseStack.pushPose();
                poseStack.translate(x, y, 0.0f);
                boolean includesSelected = SettingRowList.this.getSelected() == this;
                this.wrappedEntry.preRender(guiGraphics, includesSelected, true);
                EditorListEntry hoveredInRow = this.wrappedEntry.render(guiGraphics, index, rowWidth, rowHeight, (mouseX - x) - this.wrappedEntry.getEntryRelativeX(), (mouseY - y) - this.wrappedEntry.getEntryRelativeY(), isMouseOver, partialTicks, GuiCategoryEditor.this.font, mouseX, mouseY, includesSelected, true);
                this.wrappedEntry.postRender(guiGraphics);
                poseStack.popPose();
                if (hoveredInRow != null) {
                    SettingRowList.this.hovered = hoveredInRow;
                }
            }

            public boolean mouseClicked(double mouseX, double mouseY, int i) {
                SettingRowList.this.setFocused(this);
                double relativeMouseX = (mouseX - this.lastX) - this.wrappedEntry.getEntryRelativeX();
                double relativeMouseY = (mouseY - this.lastY) - this.wrappedEntry.getEntryRelativeY();
                this.wrappedEntry.mouseClicked(this, relativeMouseX, relativeMouseY, i);
                return true;
            }

            public boolean mouseReleased(double mouseX, double mouseY, int i) {
                double relativeMouseX = (mouseX - this.lastX) - this.wrappedEntry.getEntryRelativeX();
                double relativeMouseY = (mouseY - this.lastY) - this.wrappedEntry.getEntryRelativeY();
                this.wrappedEntry.mouseReleased(relativeMouseX, relativeMouseY, i);
                return super.mouseReleased(mouseX, mouseY, i);
            }

            public boolean mouseScrolled(double mouseX, double mouseY, double f, double g) {
                double relativeMouseX = (mouseX - this.lastX) - this.wrappedEntry.getEntryRelativeX();
                double relativeMouseY = (mouseY - this.lastY) - this.wrappedEntry.getEntryRelativeY();
                if (this.wrappedEntry.mouseScrolled(relativeMouseX, relativeMouseY, f, g)) {
                    return true;
                }
                return super.mouseScrolled(mouseX, mouseY, f, g);
            }

            public boolean mouseDragged(double mouseX, double mouseY, int i, double f, double g) {
                double relativeMouseX = (mouseX - this.lastX) - this.wrappedEntry.getEntryRelativeX();
                double relativeMouseY = (mouseY - this.lastY) - this.wrappedEntry.getEntryRelativeY();
                if (this.wrappedEntry.mouseDragged(relativeMouseX, relativeMouseY, i, f, g)) {
                    return true;
                }
                return super.mouseDragged(mouseX, mouseY, i, f, g);
            }

            public boolean keyPressed(int i, int j, int k) {
                if (this.wrappedEntry.keyPressed(i, j, k, true)) {
                    return true;
                }
                return super.keyPressed(i, j, k);
            }

            public boolean keyReleased(int i, int j, int k) {
                if (this.wrappedEntry.keyReleased(i, j, k)) {
                    return true;
                }
                return super.keyReleased(i, j, k);
            }

            public boolean charTyped(char c, int i) {
                if (this.wrappedEntry.charTyped(c, i)) {
                    return true;
                }
                return super.charTyped(c, i);
            }

            public void setFocused(boolean bl) {
                this.wrappedEntry.setFocused(bl);
                super.setFocused(bl);
            }

            public void tick() {
                this.wrappedEntry.tick();
            }

            public Component getNarration() {
                String selectedNarrationString = this.wrappedEntry.getSubNarration();
                if (selectedNarrationString == null) {
                    return Component.literal("");
                }
                return Component.translatable("narrator.select", new Object[]{selectedNarrationString});
            }
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseClicked(double d, double e, int i) {
        if (super.mouseClicked(d, e, i)) {
            return true;
        }
        return this.rowList.mouseClicked(d, e, i);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseReleased(double d, double e, int i) {
        if (super.mouseReleased(d, e, i)) {
            return true;
        }
        return this.rowList.mouseReleased(d, e, i);
    }

    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (super.mouseDragged(d, e, i, f, g)) {
            return true;
        }
        return this.rowList.mouseDragged(d, e, i, f, g);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseScrolled(double d, double e, double f, double g) {
        if (super.mouseScrolled(d, e, f, g)) {
            return true;
        }
        return this.rowList.mouseScrolled(d, e, f, g);
    }
}
