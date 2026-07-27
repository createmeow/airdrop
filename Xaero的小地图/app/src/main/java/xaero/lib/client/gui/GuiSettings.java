package xaero.lib.client.gui;

import com.google.common.base.Objects;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.lib.XaeroLib;
import xaero.lib.client.gui.util.GuiUtils;
import xaero.lib.client.gui.widget.MyTinyButton;
import xaero.lib.client.gui.widget.online.WidgetScreen;
import xaero.lib.common.util.KeySortableByOther;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/GuiSettings.class */
public abstract class GuiSettings extends ScreenBase implements WidgetScreen {
    protected int entriesPerPage;
    protected ISettingEntry[] entries;
    protected String entryFilter;
    private boolean foundSomething;
    protected Component screenTitle;
    protected int page;
    protected int maxPage;
    private MyTinyButton nextButton;
    private MyTinyButton prevButton;
    protected EditBox searchField;
    protected boolean canSearch;
    private boolean shouldRefocusSearch;
    protected boolean confirmButton;

    public GuiSettings(Component title, Screen backScreen, Screen escScreen) {
        this(title, backScreen, escScreen, false);
    }

    public GuiSettings(Component title, Screen backScreen, Screen escScreen, boolean confirmButton) {
        super(backScreen, escScreen, title);
        this.entriesPerPage = 12;
        this.entryFilter = "";
        this.canSearch = true;
        this.confirmButton = confirmButton;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        int positionInEntryString;
        super.init();
        this.screenTitle = this.title;
        if (this.confirmButton) {
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_confirm"), b -> {
                confirm();
            }).bounds((this.width / 2) - 105, (this.height / 6) + 168, 100, 20).build());
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_back"), b2 -> {
                goBack();
            }).bounds((this.width / 2) + 5, (this.height / 6) + 168, 100, 20).build());
        } else {
            addRenderableWidget(Button.builder(Component.translatable("gui.xaero_back"), b3 -> {
                goBack();
            }).bounds((this.width / 2) - 100, (this.height / 6) + 168, 200, 20).build());
        }
        int verticalOffset = getVerticalOffset();
        if (this.entries != null) {
            ArrayList<KeySortableByOther<ISettingEntry>> sortingList = new ArrayList<>();
            String comparisonFilter = this.entryFilter.toLowerCase();
            for (int i = 0; i < this.entries.length; i++) {
                ISettingEntry entry = this.entries[i];
                String entrySearchString = entry.getStringForSearch().toLowerCase();
                if (entrySearchString != null && (positionInEntryString = entrySearchString.indexOf(comparisonFilter)) != -1) {
                    KeySortableByOther<ISettingEntry> sortableEntry = new KeySortableByOther<>(entry, Integer.valueOf(positionInEntryString));
                    sortingList.add(sortableEntry);
                }
            }
            ArrayList<ISettingEntry> filteredEntries = (ArrayList) sortingList.stream().sorted().map((v0) -> {
                return v0.getKey();
            }).collect(ArrayList::new, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
            if (!filteredEntries.isEmpty()) {
                this.foundSomething = true;
                this.maxPage = ((int) Math.ceil(filteredEntries.size() / this.entriesPerPage)) - 1;
                if (this.page > this.maxPage) {
                    this.page = this.maxPage;
                }
                int firstEntry = this.entriesPerPage * this.page;
                int entryCount = Math.min(filteredEntries.size() - firstEntry, this.entriesPerPage);
                for (int i2 = 0; i2 < entryCount; i2++) {
                    AbstractWidget optionWidget = filteredEntries.get(firstEntry + i2).createWidget(((this.width / 2) - 205) + ((i2 % 2) * 210), (this.height / 7) + 5 + verticalOffset + (24 * (i2 >> 1)), 200);
                    addRenderableWidget(optionWidget);
                }
            } else {
                this.foundSomething = false;
                this.page = 0;
                this.maxPage = 0;
            }
        }
        this.screenTitle = this.screenTitle.plainCopy().append(" (" + (this.page + 1) + "/" + (this.maxPage + 1) + ")");
        this.nextButton = new MyTinyButton((this.width / 2) + 131, (this.height / 7) + 149 + verticalOffset, Component.translatable("gui.xaero_next", new Object[0]), b4 -> {
            onNextButton();
        });
        this.prevButton = new MyTinyButton((this.width / 2) - 205, (this.height / 7) + 149 + verticalOffset, Component.translatable("gui.xaero_previous", new Object[0]), b5 -> {
            onPrevButton();
        });
        if (this.maxPage > 0) {
            addRenderableWidget(this.nextButton);
            addRenderableWidget(this.prevButton);
            this.nextButton.active = this.page < this.maxPage;
            this.prevButton.active = this.page > 0;
        }
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().initialize(this, this.width, this.height);
        boolean shouldFocusSearch = this.shouldRefocusSearch;
        this.shouldRefocusSearch = false;
        if (this.canSearch && this.entries != null) {
            int cursorPos = 0;
            if (shouldFocusSearch) {
                cursorPos = this.searchField.getCursorPosition();
            }
            this.searchField = new EditBox(this.font, (this.width / 2) - 100, (((this.height / 7) + 5) + verticalOffset) - 24, 200, 20, Component.translatable("gui.xaero_settings_search"));
            this.searchField.setValue(this.entryFilter);
            if (shouldFocusSearch) {
                setFocused(this.searchField);
                this.searchField.setFocused(true);
                this.searchField.setCursorPosition(cursorPos);
                this.searchField.setHighlightPos(cursorPos);
            }
            this.searchField.setResponder(s -> {
                if (this.canSearch) {
                    updateSearch();
                }
            });
            addWidget(this.searchField);
            return;
        }
        this.searchField = null;
    }

    protected void confirm() {
        onExit(this.escape);
    }

    protected int getVerticalOffset() {
        return this.canSearch ? 24 : 0;
    }

    public void tick() {
        super.tick();
        if (this.canSearch) {
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.renderBackground(guiGraphics, par1, par2, par3);
        getVerticalOffset();
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().render(guiGraphics, this, this.width, this.height, par1, par2, this.minecraft.getWindow().getGuiScale());
        guiGraphics.drawCenteredString(this.minecraft.font, this.screenTitle, this.width / 2, ((this.height / 7) + 29) - 42, 16777215);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
        if (this.searchField != null) {
            if (!this.foundSomething && this.entries != null) {
                guiGraphics.drawCenteredString(this.minecraft.font, I18n.get("gui.xaero_settings_not_found", new Object[0]), this.width / 2, (this.height / 7) + 34, 16777215);
            }
            if (!this.searchField.isFocused() && this.searchField.getValue().isEmpty()) {
                GuiUtils.setFieldText(this.searchField, I18n.get("gui.xaero_settings_search_placeholder", new Object[0]), -11184811);
                this.searchField.moveCursorTo(0, false);
            }
            this.searchField.render(guiGraphics, mouseX, mouseY, partial);
            if (!this.searchField.isFocused()) {
                GuiUtils.setFieldText(this.searchField, this.entryFilter);
            }
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.render(guiGraphics, par1, par2, par3);
        if (this.openDropdown == null) {
            renderTooltips(guiGraphics, par1, par2, par3);
        }
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetScreen
    public void addButtonVisible(AbstractWidget button) {
        addRenderableWidget(button);
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetScreen
    public <S extends Screen & WidgetScreen> S getScreen() {
        return this;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void onExit(Screen screen) {
        super.onExit(screen);
    }

    public boolean keyPressed(int par1, int par2, int par3) {
        return super.keyPressed(par1, par2, par3) || ((getFocused() instanceof EditBox) && getFocused().isFocused());
    }

    public boolean charTyped(char c, int i) {
        boolean result = super.charTyped(c, i);
        return result;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().handleClick(this, this.width, this.height, (int) p_mouseClicked_1_, (int) p_mouseClicked_3_, this.minecraft.getWindow().getGuiScale());
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    protected void onNextButton() {
        this.page++;
        init(this.minecraft, this.width, this.height);
    }

    protected void onPrevButton() {
        this.page--;
        init(this.minecraft, this.width, this.height);
    }

    public ISettingEntry[] getEntriesCopy() {
        if (this.entries == null) {
            return null;
        }
        ISettingEntry[] result = new ISettingEntry[this.entries.length];
        System.arraycopy(this.entries, 0, result, 0, this.entries.length);
        return result;
    }

    private void updateSearch() {
        if (this.searchField.isFocused()) {
            String newValue = this.searchField.getValue();
            if (!Objects.equal(this.entryFilter, newValue)) {
                this.entryFilter = this.searchField.getValue();
                this.shouldRefocusSearch = true;
                this.page = 0;
                init(this.minecraft, this.width, this.height);
            }
        }
    }
}
