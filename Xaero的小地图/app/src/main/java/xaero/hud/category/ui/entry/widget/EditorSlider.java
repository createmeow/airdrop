package xaero.hud.category.ui.entry.widget;

import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import xaero.common.gui.IXaeroNarratableWidget;
import xaero.hud.category.ui.GuiCategoryEditor;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/widget/EditorSlider.class */
public class EditorSlider extends AbstractOptionSliderButton implements IXaeroNarratableWidget {
    protected int currentIndex;
    protected int prevNarrationIndex;
    protected int optionCount;
    protected IntConsumer updatedIndexConsumer;
    protected Supplier<Component> messageSupplier;
    protected final GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList;

    public EditorSlider(IntConsumer updatedIndexConsumer, Supplier<Component> messageSupplier, int currentIndex, int optionCount, int widthIn, int heightIn, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        super((Options) null, 2, 2, widthIn, heightIn, 0.0d);
        this.updatedIndexConsumer = updatedIndexConsumer;
        this.messageSupplier = messageSupplier;
        this.optionCount = optionCount;
        this.prevNarrationIndex = currentIndex;
        this.currentIndex = currentIndex;
        this.value = toSliderValue(currentIndex);
        this.rowList = rowList;
        updateMessage();
    }

    public boolean keyPressed(int i, int j, int k) {
        if (i == 263) {
            manualOptionChange(this.currentIndex - 1);
            return false;
        }
        if (i == 262) {
            manualOptionChange(this.currentIndex + 1);
            return false;
        }
        return super.keyPressed(i, j, k);
    }

    private void manualOptionChange(int index) {
        if (index < 0) {
            index = 0;
        } else if (index >= this.optionCount) {
            index = this.optionCount - 1;
        }
        this.value = toSliderValue(index);
        applyValue();
        updateMessage();
    }

    @Override // xaero.common.gui.IXaeroNarratableWidget
    public MutableComponent createNarrationMessage() {
        return Component.literal("");
    }

    protected void applyValue() {
        this.currentIndex = toValue(this.value);
        this.updatedIndexConsumer.accept(this.currentIndex);
    }

    protected void updateMessage() {
        setMessage(this.messageSupplier.get());
        if (this.currentIndex != this.prevNarrationIndex) {
            this.rowList.narrateSelection();
        }
        this.prevNarrationIndex = this.currentIndex;
    }

    public double toSliderValue(int i) {
        return i / (this.optionCount - 1);
    }

    public int toValue(double d) {
        return (int) clamp(Mth.lerp(Mth.clamp(d, 0.0d, 1.0d), 0.0d, this.optionCount - 1));
    }

    private double clamp(double d) {
        return Mth.clamp(Math.round(d), 0.0d, this.optionCount - 1);
    }
}
