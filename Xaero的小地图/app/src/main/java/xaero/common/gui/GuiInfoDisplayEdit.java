package xaero.common.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.common.config.info.config.InfoDisplayConfigData;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.InfoDisplayManager;
import xaero.hud.minimap.info.config.InfoDisplayConfigClientUtils;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.common.config.Config;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiInfoDisplayEdit.class */
public class GuiInfoDisplayEdit extends ScreenBase {
    private static final int FRAME_TOP_SIZE = 30;
    private static final int FRAME_BOTTOM_SIZE = 61;
    private static final int SELECTION_ITEM_HEIGHT = 24;
    private static final Component HELP_COMPONENT = Component.translatable("gui.xaero_minimap_info_display_manager_help");
    private static final Component SERVER_ENFORCED_COMPONENT = Component.translatable("gui.xaero_info_display_editor_server_enforced").withStyle(ChatFormatting.YELLOW);
    private SelectionList selectionList;
    private final InfoDisplayManager manager;
    private List<String> currentOrder;
    private int selected;
    private int subSelected;
    private final Map<String, MoveableEntry<?>> moveableEntries;
    private final boolean clientSide;
    private InfoDisplayManagerConfigData inputConfig;
    private final Config config;
    private final Runnable onChange;
    private final boolean viewingEnforced;
    private boolean madeChanges;

    public GuiInfoDisplayEdit(EditConfigScreen parent, Screen escape, Config config, Runnable onChange, boolean viewingEnforced) {
        super(parent, escape, Component.translatable("gui.xaero_minimap_info_display_manager"));
        this.config = config;
        this.onChange = onChange;
        this.clientSide = parent.getContext().isClientSide();
        this.viewingEnforced = viewingEnforced;
        this.manager = HudMod.INSTANCE.getMinimap().getInfoDisplays().getManager();
        this.inputConfig = (InfoDisplayManagerConfigData) config.get(MinimapProfiledConfigOptions.INFO_DISPLAY_CONFIG);
        if (this.inputConfig == null || this.inputConfig == InfoDisplayManagerConfigData.EMPTY) {
            this.inputConfig = InfoDisplayConfigClientUtils.createDefaultConfig(this.manager, (ModSettings) null, this.clientSide);
        }
        this.currentOrder = this.manager.adaptOrder(this.inputConfig.getOrderStream());
        this.moveableEntries = new HashMap();
        this.selected = -1;
        this.subSelected = -1;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    protected void init() {
        super.init();
        this.selectionList = new SelectionList();
        addWidget(this.selectionList);
        addRenderableWidget(Button.builder(Component.translatable("gui.done", new Object[0]), b -> {
            goBack();
        }).bounds((this.width / 2) - 100, this.height - 34, 200, 20).build());
        if (this.moveableEntries.isEmpty()) {
            for (String id : this.currentOrder) {
                InfoDisplay<?> infoDisplay = this.manager.get(id);
                MoveableEntry<?> moveable = createEntryFor(infoDisplay);
                this.moveableEntries.put(id, moveable);
            }
        }
        this.moveableEntries.values().forEach(this::refreshEntry);
    }

    private <T> MoveableEntry<T> createEntryFor(InfoDisplay<T> infoDisplay) {
        MoveableEntry<T> moveable = new MoveableEntry<>(this, infoDisplay);
        InfoDisplayConfigData infoDisplayConfig = this.inputConfig.get(infoDisplay.getId());
        if (infoDisplayConfig == null) {
            infoDisplayConfig = InfoDisplayConfigClientUtils.createDefaultConfig(infoDisplay, (ModSettings) null, this.clientSide);
        }
        String configStateString = infoDisplayConfig.getState();
        ((MoveableEntry) moveable).textColor = infoDisplayConfig.getTextColor();
        ((MoveableEntry) moveable).backgroundColor = infoDisplayConfig.getBackgroundColor();
        ((MoveableEntry) moveable).state = configStateString == null ? null : infoDisplay.getCodec().decode(configStateString, null, null);
        return moveable;
    }

    private <T> void refreshEntry(MoveableEntry<T> moveable) {
        moveable.clearSubElements();
        addSubElements(moveable);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void onExit(Screen screen) {
        super.onExit(screen);
        if (this.madeChanges) {
            this.onChange.run();
        }
    }

    private void saveToConfigProfile() {
        InfoDisplayManagerConfigData.Builder builder = InfoDisplayManagerConfigData.Builder.begin();
        for (String id : this.currentOrder) {
            MoveableEntry<?> editorEntry = this.moveableEntries.get(id);
            String stateString = encodeState(editorEntry);
            builder.add(id, new InfoDisplayConfigData(((MoveableEntry) editorEntry).backgroundColor, ((MoveableEntry) editorEntry).textColor, stateString));
        }
        InfoDisplayManagerConfigData outputConfig = builder.build();
        this.config.set(MinimapProfiledConfigOptions.INFO_DISPLAY_CONFIG, outputConfig);
        this.madeChanges = true;
    }

    private <T> String encodeState(MoveableEntry<T> editorEntry) {
        if (((MoveableEntry) editorEntry).state == null) {
            return null;
        }
        return ((MoveableEntry) editorEntry).infoDisplay.getCodec().encode(((MoveableEntry) editorEntry).state, null, null);
    }

    public String[] createColorOptions(String symbol, boolean noneOption, boolean nullOption) {
        int firstColorIndex = (noneOption ? 1 : 0) + (nullOption ? 1 : 0);
        String[] options = new String[MinimapConfigConstants.COLOR_NAMES.length + firstColorIndex];
        if (nullOption) {
            options[0] = "~";
        }
        if (noneOption) {
            options[firstColorIndex - 1] = "□□";
        }
        for (int i = 0; i < MinimapConfigConstants.COLOR_NAMES.length; i++) {
            options[i + firstColorIndex] = "§" + MinimapConfigConstants.COLOR_CODES[i] + symbol;
        }
        return options;
    }

    private <T> void addSubElements(MoveableEntry<T> moveable) {
        int iIntValue;
        boolean includeNull = !this.clientSide || this.viewingEnforced;
        if (!includeNull && (((MoveableEntry) moveable).state == null || ((MoveableEntry) moveable).textColor == null || ((MoveableEntry) moveable).backgroundColor == null)) {
            throw new IllegalArgumentException();
        }
        AbstractWidget stateWidget = ((MoveableEntry) moveable).infoDisplay.createWidget(((this.width / 2) + 150) - 102, 0, 100, 20, moveable, this::saveToConfigProfile, includeNull);
        if (stateWidget != null) {
            moveable.addSubElement(stateWidget);
        }
        int currentSelectedTextColor = ((MoveableEntry) moveable).textColor == null ? -1 : ((MoveableEntry) moveable).textColor.intValue() % MinimapConfigConstants.COLOR_NAMES.length;
        if (includeNull) {
            currentSelectedTextColor++;
        }
        if (currentSelectedTextColor < 0) {
            currentSelectedTextColor = 0;
            ((MoveableEntry) moveable).textColor = includeNull ? null : 0;
        }
        DropDownWidget textColorWidget = DropDownWidget.Builder.begin().setOptions(createColorOptions("Aa", false, includeNull)).setX((this.width / 2) - 147).setW(20).setSelected(Integer.valueOf(currentSelectedTextColor)).setContainer(this).setCallback((menu, index) -> {
            if (includeNull && index == 0) {
                moveable.textColor = null;
            } else {
                moveable.textColor = Integer.valueOf(index - (includeNull ? 1 : 0));
            }
            saveToConfigProfile();
            return true;
        }).setNarrationTitle(Component.translatable("gui.xaero_dropdown_info_display_text_color")).build();
        moveable.addSubElement(textColorWidget);
        if (((MoveableEntry) moveable).backgroundColor == null) {
            iIntValue = 0;
        } else {
            iIntValue = (((MoveableEntry) moveable).backgroundColor.intValue() < 0 ? ((MoveableEntry) moveable).backgroundColor.intValue() : ((MoveableEntry) moveable).backgroundColor.intValue() % MinimapConfigConstants.COLOR_NAMES.length) + 1 + (includeNull ? 1 : 0);
        }
        int currentSelectedBackground = iIntValue;
        if (currentSelectedBackground < 0) {
            currentSelectedBackground = 0;
            ((MoveableEntry) moveable).backgroundColor = includeNull ? null : -1;
        }
        DropDownWidget backgroundColorWidget = DropDownWidget.Builder.begin().setOptions(createColorOptions("■■", true, includeNull)).setX((this.width / 2) - 124).setW(20).setSelected(Integer.valueOf(currentSelectedBackground)).setContainer(this).setCallback((menu2, index2) -> {
            if (includeNull && index2 == 0) {
                moveable.backgroundColor = null;
            } else {
                moveable.backgroundColor = Integer.valueOf((index2 - 1) - (includeNull ? 1 : 0));
            }
            saveToConfigProfile();
            return true;
        }).setNarrationTitle(Component.translatable("gui.xaero_dropdown_info_display_background_color")).build();
        moveable.addSubElement(backgroundColorWidget);
        if (this.viewingEnforced) {
            if (stateWidget != null) {
                stateWidget.active = false;
            }
            textColorWidget.active = false;
            backgroundColorWidget.active = false;
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseReleased(double d, double e, int i) {
        if (this.selectionList != null) {
            this.selectionList.releaseDrag();
        }
        return super.mouseReleased(d, e, i);
    }

    public void setFocused(GuiEventListener guiEventListener) {
        super.setFocused(guiEventListener);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        this.selectionList.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, -1);
        if (this.clientSide) {
            guiGraphics.drawCenteredString(this.font, HELP_COMPONENT, this.width / 2, this.height - 52, -1);
        }
        if (this.viewingEnforced) {
            guiGraphics.drawCenteredString(this.font, SERVER_ENFORCED_COMPONENT, this.width / 2, 15, -1);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiInfoDisplayEdit$MoveableEntry.class */
    public class MoveableEntry<T> {
        private final List<AbstractWidget> subElements = new ArrayList();
        private final InfoDisplay<T> infoDisplay;
        private T state;
        private Integer textColor;
        private Integer backgroundColor;

        public MoveableEntry(GuiInfoDisplayEdit this$0, InfoDisplay<T> infoDisplay) {
            this.infoDisplay = infoDisplay;
        }

        public void addSubElement(AbstractWidget widget) {
            this.subElements.add(widget);
        }

        public T getState() {
            return this.state;
        }

        public void setState(T state) {
            this.state = state;
        }

        private void clearSubElements() {
            this.subElements.clear();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiInfoDisplayEdit$SelectionList.class */
    class SelectionList extends ObjectSelectionList<Entry> {
        private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");
        private static final Component LEFT_RIGHT_USAGE = Component.translatable("narration.xaero_ui_list_left_right_usage");
        private boolean dragging;
        private int dragStartX;
        private int dragStartY;
        private int dragged;
        private int draggedOffsetX;
        private int draggedOffsetY;

        public SelectionList() {
            super(GuiInfoDisplayEdit.this.minecraft, GuiInfoDisplayEdit.this.width, (GuiInfoDisplayEdit.this.height - GuiInfoDisplayEdit.FRAME_BOTTOM_SIZE) - GuiInfoDisplayEdit.FRAME_TOP_SIZE, GuiInfoDisplayEdit.FRAME_TOP_SIZE, 24);
            createEntries();
            if (GuiInfoDisplayEdit.this.selected != -1) {
                setFocused(getEntry(GuiInfoDisplayEdit.this.selected));
            }
            this.dragged = -1;
        }

        public boolean isFocused() {
            return GuiInfoDisplayEdit.this.getFocused() == this;
        }

        public void setFocused(GuiEventListener guiEventListener) {
            Entry oldSelected;
            if ((guiEventListener instanceof Entry) || guiEventListener == null) {
                Entry entry = (Entry) guiEventListener;
                if (GuiInfoDisplayEdit.this.subSelected != -1 && (oldSelected = getSelected()) != null) {
                    MoveableEntry<?> moveable = oldSelected.getMoveable();
                    ((MoveableEntry) moveable).subElements.get(GuiInfoDisplayEdit.this.subSelected).setFocused(false);
                }
                GuiInfoDisplayEdit.this.selected = entry == null ? -1 : entry.index;
                GuiInfoDisplayEdit.this.subSelected = -1;
            }
            super.setFocused(guiEventListener);
            if (getFocused() == null) {
                setSelected((Entry) null);
            }
        }

        public void setSelected(Entry entry) {
            super.setSelected(entry);
        }

        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            super.updateWidgetNarration(narrationElementOutput);
            if (isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE, new Component[]{USAGE_NARRATION, LEFT_RIGHT_USAGE});
            }
        }

        private void createEntries() {
            for (int i = 0; i < GuiInfoDisplayEdit.this.currentOrder.size(); i++) {
                Entry entry = new Entry(i);
                addEntry(entry);
            }
        }

        private void releaseDrag() {
            this.dragging = false;
            this.dragged = -1;
        }

        protected int getScrollbarPosition() {
            return (this.width / 2) + 164;
        }

        public int getRowWidth() {
            return 300;
        }

        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
            if (!this.dragging) {
                if (this.dragged != -1) {
                    if (Math.abs(mouseX - this.dragStartX) > 5 || Math.abs(mouseY - this.dragStartY) > 5) {
                        this.dragging = true;
                        setFocused(null);
                        return;
                    }
                    return;
                }
                return;
            }
            Entry draggedEntry = getEntry(this.dragged);
            draggedEntry.renderNonInteractable(guiGraphics, mouseX + this.draggedOffsetX, mouseY + this.draggedOffsetY);
            Entry hoveredEntry = getEntryAtPosition(mouseX, mouseY);
            int hoveredIndex = hoveredEntry == null ? -1 : hoveredEntry.index;
            if (hoveredIndex != -1 && hoveredIndex != this.dragged) {
                String draggedId = GuiInfoDisplayEdit.this.currentOrder.get(this.dragged);
                int slideDirection = hoveredIndex < this.dragged ? 1 : -1;
                int i = this.dragged;
                while (true) {
                    int i2 = i;
                    if (i2 != hoveredIndex) {
                        GuiInfoDisplayEdit.this.currentOrder.set(i2, GuiInfoDisplayEdit.this.currentOrder.get(i2 - slideDirection));
                        i = i2 - slideDirection;
                    } else {
                        GuiInfoDisplayEdit.this.currentOrder.set(hoveredIndex, draggedId);
                        this.dragged = hoveredIndex;
                        GuiInfoDisplayEdit.this.saveToConfigProfile();
                        return;
                    }
                }
            }
        }

        /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiInfoDisplayEdit$SelectionList$Entry.class */
        public class Entry extends ObjectSelectionList.Entry<Entry> {
            private final int index;
            private int lastRenderX;
            private int lastRenderY;
            private int lastMouseX;
            private int lastMouseY;

            public Entry(int index) {
                this.index = index;
            }

            private void renderNonInteractable(GuiGraphics guiGraphics, int x, int y) {
                String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
                InfoDisplay<?> infoDisplay = GuiInfoDisplayEdit.this.manager.get(infoDisplayId);
                guiGraphics.drawString(GuiInfoDisplayEdit.this.font, infoDisplay.getName(), x + 48, y + 6, -1);
            }

            private MoveableEntry<?> getMoveable() {
                String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
                return GuiInfoDisplayEdit.this.moveableEntries.get(infoDisplayId);
            }

            public void render(GuiGraphics guiGraphics, int index, int y, int x, int l, int m, int mouseX, int mouseY, boolean bl, float partialTicks) {
                this.lastRenderX = x;
                this.lastRenderY = y;
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
                if (SelectionList.this.dragging && SelectionList.this.dragged == index) {
                    return;
                }
                renderNonInteractable(guiGraphics, x, y);
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    subElement.setY(((y - 2) + 12) - (subElement.getHeight() / 2));
                    if (subElement instanceof DropDownWidget) {
                        subElement.setY(subElement.getY() - 1);
                    }
                    subElement.render(guiGraphics, mouseX, mouseY, partialTicks);
                }
            }

            public boolean mouseClicked(double d, double e, int i) {
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    if (subElement.isMouseOver(d, e) && subElement.mouseClicked(d, e, i)) {
                        return true;
                    }
                }
                if (i == 0) {
                    SelectionList.this.dragging = false;
                    if (!GuiInfoDisplayEdit.this.clientSide || GuiInfoDisplayEdit.this.viewingEnforced) {
                        return true;
                    }
                    SelectionList.this.dragged = this.index;
                    SelectionList.this.draggedOffsetX = (int) (this.lastRenderX - d);
                    SelectionList.this.draggedOffsetY = (int) (this.lastRenderY - e);
                    SelectionList.this.dragStartX = (int) d;
                    SelectionList.this.dragStartY = (int) e;
                    if (SelectionList.this.getSelected() == this) {
                        SelectionList.this.setFocused(null);
                    } else {
                        return true;
                    }
                } else {
                    SelectionList.this.setFocused(null);
                }
                return super.mouseClicked(d, e, i);
            }

            public boolean mouseReleased(double d, double e, int i) {
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    subElement.mouseReleased(d, e, i);
                }
                return super.mouseReleased(d, e, i);
            }

            public void mouseMoved(double d, double e) {
                this.lastMouseX = (int) d;
                this.lastMouseY = (int) e;
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    if (subElement.isMouseOver(d, e)) {
                        subElement.mouseMoved(d, e);
                    }
                }
                super.mouseMoved(d, e);
            }

            public boolean mouseDragged(double d, double e, int i, double f, double g) {
                this.lastMouseX = (int) d;
                this.lastMouseY = (int) e;
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    if (subElement.isMouseOver(d, e) && subElement.mouseDragged(d, e, i, f, g)) {
                        return true;
                    }
                }
                return super.mouseDragged(d, e, i, f, g);
            }

            public boolean mouseScrolled(double d, double e, double f, double g) {
                MoveableEntry<?> moveableEntry = getMoveable();
                for (AbstractWidget subElement : ((MoveableEntry) moveableEntry).subElements) {
                    if (subElement.isMouseOver(d, e) && subElement.mouseScrolled(d, e, f, g)) {
                        return true;
                    }
                }
                return super.mouseScrolled(d, e, f, g);
            }

            public boolean keyPressed(int i, int j, int k) {
                MoveableEntry<?> moveableEntry = getMoveable();
                if (i == 262 || i == 263) {
                    if (GuiInfoDisplayEdit.this.subSelected != -1) {
                        ((MoveableEntry) moveableEntry).subElements.get(GuiInfoDisplayEdit.this.subSelected).setFocused(false);
                    }
                    if (i == 262) {
                        GuiInfoDisplayEdit.this.subSelected++;
                        if (GuiInfoDisplayEdit.this.subSelected == ((MoveableEntry) moveableEntry).subElements.size()) {
                            GuiInfoDisplayEdit.this.subSelected = -1;
                        }
                    } else {
                        GuiInfoDisplayEdit.this.subSelected--;
                        if (GuiInfoDisplayEdit.this.subSelected < -1) {
                            GuiInfoDisplayEdit.this.subSelected = ((MoveableEntry) moveableEntry).subElements.size() - 1;
                        }
                    }
                    if (GuiInfoDisplayEdit.this.subSelected != -1) {
                        ((MoveableEntry) moveableEntry).subElements.get(GuiInfoDisplayEdit.this.subSelected).setFocused(true);
                    }
                } else if (GuiInfoDisplayEdit.this.subSelected != -1 && ((MoveableEntry) moveableEntry).subElements.get(GuiInfoDisplayEdit.this.subSelected).keyPressed(i, j, k)) {
                    return true;
                }
                return super.keyPressed(i, j, k);
            }

            public boolean keyReleased(int i, int j, int k) {
                MoveableEntry<?> moveableEntry = getMoveable();
                if (GuiInfoDisplayEdit.this.subSelected != -1 && ((MoveableEntry) moveableEntry).subElements.get(GuiInfoDisplayEdit.this.subSelected).keyReleased(i, j, k)) {
                    return true;
                }
                return super.keyReleased(i, j, k);
            }

            public boolean charTyped(char c, int i) {
                MoveableEntry<?> moveableEntry = getMoveable();
                if (GuiInfoDisplayEdit.this.subSelected != -1 && ((MoveableEntry) moveableEntry).subElements.get(GuiInfoDisplayEdit.this.subSelected).charTyped(c, i)) {
                    return true;
                }
                return super.charTyped(c, i);
            }

            public void updateNarration(NarrationElementOutput narrationElementOutput) {
                MoveableEntry<?> moveableEntry = getMoveable();
                int sub = -1;
                if (GuiInfoDisplayEdit.this.selected == this.index && GuiInfoDisplayEdit.this.subSelected >= 0) {
                    sub = GuiInfoDisplayEdit.this.subSelected;
                } else {
                    for (int i = 0; i < ((MoveableEntry) moveableEntry).subElements.size(); i++) {
                        if (((MoveableEntry) moveableEntry).subElements.get(i).isMouseOver(this.lastMouseX, this.lastMouseY)) {
                            sub = i;
                        }
                    }
                }
                if (sub >= 0) {
                    ((MoveableEntry) moveableEntry).subElements.get(sub).updateNarration(narrationElementOutput);
                } else {
                    super.updateNarration(narrationElementOutput);
                }
            }

            public Component getNarration() {
                String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
                InfoDisplay<?> infoDisplay = GuiInfoDisplayEdit.this.manager.get(infoDisplayId);
                String narration = infoDisplay.getName().getString();
                return Component.literal(I18n.get("narrator.select", new Object[]{narration}));
            }
        }
    }
}
