package xaero.lib.client.gui.widget.dropdown;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/dropdown/DropDownWidget.class */
public class DropDownWidget extends AbstractWidget {
    public static final int DEFAULT_BACKGROUND = -939524096;
    public static final int SELECTED_DEFAULT_BACKGROUND = -922757376;
    public static final int SELECTED_DEFAULT_HOVERED_BACKGROUND = -10496;
    public static final int TRIM = -6250336;
    public static final int TRIM_OPEN = -1;
    public static final int TRIM_INSIDE = -13487566;
    public static final int LINE_HEIGHT = 11;
    private int xOffset;
    private int yOffset;
    private String[] realOptions;
    private String[] options;
    private int selected;
    private boolean closed;
    private int scroll;
    private long scrollTime;
    private int autoScrolling;
    protected boolean openingUp;
    private final IDropDownWidgetCallback callback;
    private final IDropDownContainer container;
    private final boolean hasEmptyOption;
    protected int selectedBackground;
    protected int selectedHoveredBackground;
    protected boolean shortenFromTheRight;
    private boolean wasHovered;

    protected DropDownWidget(String[] options, int x, int y, int w, Integer selected, boolean openingUp, IDropDownWidgetCallback callback, IDropDownContainer container, boolean hasEmptyOption, Component narrationTitle) {
        super(x, y, w, 11, narrationTitle);
        this.xOffset = 0;
        this.yOffset = 0;
        this.selected = 0;
        this.closed = true;
        this.realOptions = options;
        this.callback = callback;
        this.container = container;
        int emptyOptionCount = hasEmptyOption ? 1 : 0;
        this.options = new String[this.realOptions.length + emptyOptionCount];
        System.arraycopy(this.realOptions, 0, this.options, emptyOptionCount, this.realOptions.length);
        selectId(selected.intValue(), false);
        this.openingUp = openingUp;
        this.hasEmptyOption = hasEmptyOption;
        this.selectedBackground = SELECTED_DEFAULT_BACKGROUND;
        this.selectedHoveredBackground = SELECTED_DEFAULT_HOVERED_BACKGROUND;
        this.active = true;
    }

    public int size() {
        return this.realOptions.length;
    }

    public int getXWithOffset() {
        return getX() + this.xOffset;
    }

    public int getRenderY() {
        return getY() + (this.openingUp ? 11 : 0);
    }

    public int getRenderYWithOffset() {
        return getRenderY() + this.yOffset;
    }

    private void drawSlot(GuiGraphics guiGraphics, String text, int slotIndex, int pos, int mouseX, int mouseY, boolean scrolling, int optionLimit, int xWithOffset, int yWithOffset) {
        int slotBackground;
        boolean shortened;
        int emptyOptionCount = this.hasEmptyOption ? 1 : 0;
        if ((this.closed && isHoveredOrFocused()) || (!this.closed && onDropDownSlot(mouseX, mouseY, slotIndex, scrolling, optionLimit))) {
            slotBackground = slotIndex - emptyOptionCount == this.selected ? this.selectedHoveredBackground : TRIM_INSIDE;
        } else {
            slotBackground = slotIndex - emptyOptionCount == this.selected ? this.selectedBackground : DEFAULT_BACKGROUND;
        }
        if (this.openingUp) {
            pos = (-pos) - 1;
        }
        guiGraphics.fill(xWithOffset, yWithOffset + (11 * pos), xWithOffset + this.width, yWithOffset + 11 + (11 * pos), slotBackground);
        guiGraphics.hLine(xWithOffset + 1, (xWithOffset + this.width) - 1, yWithOffset + (11 * pos), TRIM_INSIDE);
        int textWidth = Minecraft.getInstance().font.width(text);
        boolean z = false;
        while (true) {
            shortened = z;
            if (textWidth <= this.width - 2) {
                break;
            }
            text = this.shortenFromTheRight ? text.substring(0, text.length() - 1) : text.substring(1);
            textWidth = Minecraft.getInstance().font.width("..." + text);
            z = true;
        }
        if (shortened) {
            if (this.shortenFromTheRight) {
                text = text + "...";
            } else {
                text = "..." + text;
            }
        }
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, text, xWithOffset + 1 + (this.width / 2), yWithOffset + 2 + (11 * pos), 16777215);
    }

    private void drawMenu(GuiGraphics guiGraphics, int amount, int mouseX, int mouseY, int scaledHeight, int optionLimit) {
        String slotText;
        boolean scrolling = scrolling(optionLimit);
        int totalH = 11 * (amount + (scrolling ? 2 : 0));
        int renderY = getRenderY();
        if (!this.openingUp && renderY + totalH + 1 > scaledHeight) {
            this.yOffset = ((scaledHeight - renderY) - totalH) - 1;
        } else if (this.openingUp && renderY - totalH < 0) {
            this.yOffset = totalH - renderY;
        } else {
            this.yOffset = 0;
        }
        int xWithOffset = getXWithOffset();
        int yWithOffset = getRenderYWithOffset();
        int first = this.closed ? 0 : this.scroll;
        if (scrolling) {
            drawSlot(guiGraphics, (this.scroll == 0 ? "§8" : "§7") + I18n.get(this.openingUp ? "gui.xaero_down" : "gui.xaero_up", new Object[0]), -1, 0, mouseX, mouseY, scrolling, optionLimit, xWithOffset, yWithOffset);
            drawSlot(guiGraphics, (this.scroll + optionLimit >= this.options.length ? "§8" : "§7") + I18n.get(this.openingUp ? "gui.xaero_up" : "gui.xaero_down", new Object[0]), -2, amount + 1, mouseX, mouseY, scrolling, optionLimit, xWithOffset, yWithOffset);
        }
        for (int i = first; i < first + amount; i++) {
            if (this.hasEmptyOption && i == 0) {
                slotText = !this.closed ? "-" : I18n.get(this.realOptions[this.selected], new Object[0]).replace("§§", ":");
            } else {
                slotText = I18n.get(this.options[i], new Object[0]).replace("§§", ":");
            }
            drawSlot(guiGraphics, slotText, i, (i - first) + (scrolling ? 1 : 0), mouseX, mouseY, scrolling, optionLimit, xWithOffset, yWithOffset);
        }
        int trimPosY = yWithOffset - (this.openingUp ? totalH : 0);
        int trim = this.closed ? TRIM : -1;
        guiGraphics.vLine(xWithOffset, trimPosY, trimPosY + totalH, trim);
        guiGraphics.vLine(xWithOffset + this.width, trimPosY, trimPosY + totalH, trim);
        guiGraphics.hLine(xWithOffset, xWithOffset + this.width, trimPosY, trim);
        guiGraphics.hLine(xWithOffset, xWithOffset + this.width, trimPosY + totalH, trim);
    }

    private boolean scrolling(int optionLimit) {
        return this.options.length > optionLimit && !this.closed;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
        if (mouseButton != 0) {
            return false;
        }
        if (this.closed) {
            if (this.options.length > 1 && this.active) {
                setClosed(false);
                this.scroll = 0;
                return true;
            }
            return true;
        }
        int optionLimit = optionLimit(scaledHeight);
        int clickedId = getHoveredId(mouseX, mouseY, scrolling(optionLimit), optionLimit);
        if (clickedId >= 0) {
            selectId(clickedId - (this.hasEmptyOption ? 1 : 0), true);
        } else {
            this.autoScrolling = clickedId == -1 ? 1 : -1;
            this.scrollTime = System.currentTimeMillis();
            mouseScrolledInternal(this.autoScrolling, mouseX, mouseY, optionLimit);
        }
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        return true;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
        this.autoScrolling = 0;
    }

    private int getHoveredId(int mouseX, int mouseY, boolean scrolling, int optionLimit) {
        int yOnMenu = mouseY - getRenderYWithOffset();
        int visibleSlotIndex = (this.openingUp ? (-yOnMenu) - 1 : yOnMenu) / 11;
        if (scrolling && visibleSlotIndex == 0) {
            return -1;
        }
        if (visibleSlotIndex >= optionLimit + (scrolling ? 1 : 0)) {
            return -2;
        }
        int slot = (this.scroll + visibleSlotIndex) - (scrolling ? 1 : 0);
        if (slot >= this.options.length) {
            slot = this.options.length - 1;
        }
        return slot;
    }

    public boolean onDropDown(int mouseX, int mouseY, int scaledHeight) {
        int optionLimit = optionLimit(scaledHeight);
        return onDropDown(mouseX, mouseY, scrolling(optionLimit), optionLimit);
    }

    public boolean onDropDown(int mouseX, int mouseY, boolean scrolling, int optionLimit) {
        int iMin;
        int menuTop = getRenderYWithOffset();
        if (this.closed) {
            iMin = 11;
        } else {
            iMin = (Math.min(this.options.length, optionLimit) + (scrolling ? 2 : 0)) * 11;
        }
        int menuHeight = iMin;
        if (this.openingUp) {
            menuTop -= menuHeight;
        }
        int xOnMenu = mouseX - getXWithOffset();
        int yOnMenu = mouseY - menuTop;
        if (xOnMenu < 0 || yOnMenu < 0 || xOnMenu > this.width || yOnMenu >= menuHeight) {
            return false;
        }
        return true;
    }

    private boolean onDropDownSlot(int mouseX, int mouseY, int id, boolean scrolling, int optionLimit) {
        if (!onDropDown(mouseX, mouseY, scrolling, optionLimit)) {
            return false;
        }
        int hoveredSlot = getHoveredId(mouseX, mouseY, scrolling, optionLimit);
        return hoveredSlot == id;
    }

    public void selectId(int id, boolean callCallback) {
        if (id == -1) {
            setClosed(true);
            return;
        }
        if (id < 0 || id >= this.realOptions.length) {
            id = 0;
        }
        boolean newId = id != this.selected;
        if (newId && (!callCallback || this.callback.onSelected(this, id))) {
            this.selected = id;
        }
        setClosed(true);
    }

    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        int scaledHeight = Minecraft.getInstance().screen.height;
        this.isHovered = this.visible && onDropDown(mouseX, mouseY, scaledHeight);
        if (!this.visible) {
            return;
        }
        render(guiGraphics, mouseX, mouseY, Minecraft.getInstance().screen.height, true);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, int scaledHeight, boolean closedOnly) {
        if (!this.closed && closedOnly) {
            return;
        }
        int optionLimit = optionLimit(scaledHeight);
        if (this.autoScrolling != 0 && System.currentTimeMillis() - this.scrollTime > 100) {
            this.scrollTime = System.currentTimeMillis();
            mouseScrolledInternal(this.autoScrolling, mouseX, mouseY, optionLimit);
        }
        drawMenu(guiGraphics, this.closed ? 1 : Math.min(optionLimit, this.options.length), mouseX, mouseY, scaledHeight, optionLimit);
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        if (this.closed != closed) {
            if (!closed) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                this.container.onDropdownOpen(this);
            } else {
                this.container.onDropdownClosed(this);
            }
        }
        this.closed = closed;
    }

    public void mouseScrolled(int wheel, int mouseXScaled, int mouseYScaled, int scaledHeight) {
        mouseScrolledInternal(wheel * (this.openingUp ? -1 : 1), mouseXScaled, mouseYScaled, optionLimit(scaledHeight));
    }

    private void mouseScrolledInternal(int wheel, int mouseXScaled, int mouseYScaled, int optionLimit) {
        int newScroll = this.scroll - wheel;
        if (newScroll + optionLimit > this.options.length) {
            newScroll = this.options.length - optionLimit;
        }
        if (newScroll < 0) {
            newScroll = 0;
        }
        this.scroll = newScroll;
    }

    private int optionLimit(int scaledHeight) {
        return Math.max(1, (scaledHeight / 11) - 2);
    }

    public int getSelected() {
        return this.selected;
    }

    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        if (getMessage() != null) {
            narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
        }
    }

    protected MutableComponent createNarrationMessage() {
        MutableComponent narrationMessage = Component.translatable("");
        narrationMessage.getSiblings().add(getMessage());
        narrationMessage.getSiblings().add(Component.literal(". "));
        narrationMessage.getSiblings().add(Component.translatable("gui.xaero_dropdown_selected_narration", new Object[]{this.realOptions[this.selected].replaceAll("(§[0-9a-gr])+", "")}));
        return narrationMessage;
    }

    public void mouseMoved(double $$0, double $$1) {
        super.mouseMoved($$0, $$1);
    }

    public boolean mouseScrolled(double mouseXScaled, double mouseYScaled, double g, double wheel) {
        if (!isClosed()) {
            mouseScrolled((int) wheel, (int) mouseXScaled, (int) mouseYScaled, Minecraft.getInstance().screen.height);
            return true;
        }
        return super.mouseScrolled(mouseXScaled, mouseYScaled, g, wheel);
    }

    public boolean keyPressed(int $$0, int $$1, int $$2) {
        int nextSelection;
        if ($$0 == 257 || $$0 == 32) {
            if (Screen.hasShiftDown()) {
                nextSelection = getSelected() - 1;
                if (nextSelection < 0) {
                    nextSelection = this.realOptions.length - 1;
                }
            } else {
                nextSelection = (getSelected() + 1) % this.realOptions.length;
            }
            selectId(nextSelection, true);
            return true;
        }
        return super.keyPressed($$0, $$1, $$2);
    }

    public boolean keyReleased(int $$0, int $$1, int $$2) {
        return super.keyReleased($$0, $$1, $$2);
    }

    public boolean charTyped(char $$0, int $$1) {
        return super.charTyped($$0, $$1);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered) {
            int scaledHeight = Minecraft.getInstance().screen.height;
            return mouseClicked((int) mouseX, (int) mouseY, button, scaledHeight);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        int scaledHeight = Minecraft.getInstance().screen.height;
        mouseReleased((int) mouseX, (int) mouseY, button, scaledHeight);
        return false;
    }

    public void setActive(boolean b) {
        this.active = b;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/dropdown/DropDownWidget$Builder.class */
    public static final class Builder {
        private String[] options;
        private int x;
        private int y;
        private int w;
        private Integer selected;
        private boolean openingUp;
        private IDropDownWidgetCallback callback;
        private IDropDownContainer container;
        private boolean hasEmptyOption;
        private Component narrationTitle;

        private Builder() {
        }

        public Builder setDefault() {
            setOptions(null);
            setX(0);
            setY(0);
            setW(0);
            setSelected(null);
            setOpeningUp(false);
            setCallback(null);
            setHasEmptyOption(true);
            setNarrationTitle(null);
            return this;
        }

        public Builder setOptions(String[] options) {
            this.options = options;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setW(int w) {
            this.w = w;
            return this;
        }

        public Builder setSelected(Integer selected) {
            this.selected = selected;
            return this;
        }

        public Builder setOpeningUp(boolean openingUp) {
            this.openingUp = openingUp;
            return this;
        }

        public Builder setCallback(IDropDownWidgetCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setContainer(IDropDownContainer container) {
            this.container = container;
            return this;
        }

        public Builder setHasEmptyOption(boolean hasEmptyOption) {
            this.hasEmptyOption = hasEmptyOption;
            return this;
        }

        public Builder setNarrationTitle(Component narrationTitle) {
            this.narrationTitle = narrationTitle;
            return this;
        }

        public DropDownWidget build() {
            if (this.options == null || this.w == 0 || this.selected == null || this.callback == null || this.narrationTitle == null || this.container == null) {
                throw new IllegalStateException();
            }
            return new DropDownWidget(this.options, this.x, this.y, this.w, this.selected, this.openingUp, this.callback, this.container, this.hasEmptyOption, this.narrationTitle);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
