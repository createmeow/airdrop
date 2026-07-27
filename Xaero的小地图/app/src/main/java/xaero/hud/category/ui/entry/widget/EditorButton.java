package xaero.hud.category.ui.entry.widget;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.common.gui.IXaeroNarratableWidget;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.node.EditorNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/widget/EditorButton.class */
public class EditorButton extends Button implements IXaeroNarratableWidget {
    protected Supplier<Component> messageSupplier;
    private EditorNode parent;
    private GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList;

    public EditorButton(EditorNode parent, boolean active, int w, int h, EditorNode node, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        this(parent, () -> {
            return node.getDisplayName();
        }, active, w, h, b -> {
            node.getExpandAction(rowList).run();
        }, rowList);
    }

    public EditorButton(EditorNode parent, Supplier<Component> messageSupplier, boolean active, int w, int h, Button.OnPress onPress, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        super(2, 2, w, h, Component.literal(""), onPress, DEFAULT_NARRATION);
        this.active = active;
        this.messageSupplier = messageSupplier;
        this.rowList = rowList;
        this.parent = parent;
        updateMessage();
    }

    protected void updateMessage() {
        setMessage(this.messageSupplier.get());
    }

    @Override // xaero.common.gui.IXaeroNarratableWidget
    public MutableComponent createNarrationMessage() {
        return Component.literal("");
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/widget/EditorButton$PressActionWithContext.class */
    public static abstract class PressActionWithContext implements Button.OnPress {
        public abstract void onPress(EditorButton editorButton, EditorNode editorNode, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList);

        public void onPress(Button button) {
            onPress((EditorButton) button, ((EditorButton) button).parent, ((EditorButton) button).rowList);
        }
    }
}
