package xaero.hud.category.ui.entry.widget;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.common.gui.IXaeroNarratableWidget;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.lib.client.gui.util.GuiUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/widget/EditorTextField.class */
public class EditorTextField extends EditBox implements IXaeroNarratableWidget {
    private final UpdatedValueConsumer updatedValueConsumer;
    private int highlightPos;
    private final GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList;
    private boolean pauseCallback;
    private final Predicate<String> validator;

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/widget/EditorTextField$UpdatedValueConsumer.class */
    public interface UpdatedValueConsumer {
        void accept(String str, int i, int i2, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList);
    }

    public EditorTextField(UpdatedValueConsumer updatedValueConsumer, String startValue, int startCursorPos, int startHighlighPos, int maxLength, Font font, int w, int h, Component message, Predicate<String> validator, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        super(font, 3, 3, w, h, message);
        setMaxLength(maxLength);
        setValue(startValue);
        setCursorPosition(startCursorPos);
        setHighlightPos(startHighlighPos);
        this.updatedValueConsumer = updatedValueConsumer;
        this.rowList = rowList;
        this.validator = validator;
        updateColorForValue(startValue);
        setResponder(s -> {
            if (this.pauseCallback) {
                return;
            }
            updateColorForValue(s);
            this.updatedValueConsumer.accept(s, getCursorPosition(), this.highlightPos, this.rowList);
        });
    }

    private void updateColorForValue(String value) {
        setTextColor(this.validator.test(value) ? 14737632 : -43691);
    }

    public void setCursorPosition(int i) {
        super.setCursorPosition(i);
    }

    public void setHighlightPos(int i) {
        super.setHighlightPos(i);
        this.highlightPos = i;
    }

    @Override // xaero.common.gui.IXaeroNarratableWidget
    public MutableComponent createNarrationMessage() {
        return super.createNarrationMessage();
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        int o = getX() + 4;
        int p = getY() + ((this.height - 8) / 2);
        if (getValue().isEmpty() && !isFocused()) {
            guiGraphics.drawString(Minecraft.getInstance().font, getMessage(), o, p, -11184811, true);
            GuiUtils.setFieldText(this, "");
            this.pauseCallback = false;
        }
    }
}
