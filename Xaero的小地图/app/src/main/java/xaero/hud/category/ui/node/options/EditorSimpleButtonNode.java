package xaero.hud.category.ui.node.options;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListEntryExpandingOptions;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButton;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorSimpleButtonNode.class */
public final class EditorSimpleButtonNode extends EditorNode {
    protected final Component displayName;
    private ISimpleButtonCallback callback;
    private EditorButton.PressActionWithContext pressAction;
    private ISimpleButtonMessageSupplier messageSupplier;
    private final ISimpleButtonIsActiveSupplier isActiveSupplier;

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorSimpleButtonNode$ISimpleButtonCallback.class */
    public interface ISimpleButtonCallback {
        void onButtonPress(EditorNode editorNode, EditorSimpleButtonNode editorSimpleButtonNode, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList);
    }

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorSimpleButtonNode$ISimpleButtonIsActiveSupplier.class */
    public interface ISimpleButtonIsActiveSupplier {
        boolean get(EditorNode editorNode, EditorSimpleButtonNode editorSimpleButtonNode, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList);
    }

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorSimpleButtonNode$ISimpleButtonMessageSupplier.class */
    public interface ISimpleButtonMessageSupplier {
        Supplier<Component> get(EditorNode editorNode, EditorSimpleButtonNode editorSimpleButtonNode);
    }

    private EditorSimpleButtonNode(@Nonnull Component displayName, @Nonnull IEditorDataTooltipSupplier tooltipSupplier, boolean movable, ISimpleButtonCallback callback, @Nonnull EditorListRootEntryFactory listEntryFactory, ISimpleButtonMessageSupplier messageSupplier, ISimpleButtonIsActiveSupplier isActiveSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.displayName = displayName;
        this.callback = callback;
        this.messageSupplier = messageSupplier;
        this.isActiveSupplier = isActiveSupplier;
    }

    public Supplier<Component> getMessageSupplier(EditorNode parent, EditorSimpleButtonNode data) {
        return this.messageSupplier.get(parent, data);
    }

    public boolean getIsActiveSupplier(EditorNode parent, EditorSimpleButtonNode data, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        return this.isActiveSupplier.get(parent, data, rowList);
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return this.displayName;
    }

    public EditorButton.PressActionWithContext getPressAction() {
        if (this.pressAction == null) {
            this.pressAction = new EditorButton.PressActionWithContext() { // from class: xaero.hud.category.ui.node.options.EditorSimpleButtonNode.1
                @Override // xaero.hud.category.ui.entry.widget.EditorButton.PressActionWithContext
                public void onPress(EditorButton button, EditorNode parent, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
                    if (EditorSimpleButtonNode.this.callback != null) {
                        EditorSimpleButtonNode.this.callback.onButtonPress(parent, EditorSimpleButtonNode.this, rowList);
                    }
                }
            };
        }
        return this.pressAction;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        return null;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorSimpleButtonNode$Builder.class */
    public static final class Builder extends EditorNode.Builder<Builder> {
        private Component displayName;
        private ISimpleButtonCallback callback;
        private ISimpleButtonMessageSupplier messageSupplier;
        private ISimpleButtonIsActiveSupplier isActiveSupplier;

        private Builder() {
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public Builder setDefault() {
            super.setDefault();
            setDisplayName(null);
            setCallback(null);
            setMessageSupplier((parent, node) -> {
                Objects.requireNonNull(node);
                return node::getDisplayName;
            });
            setIsActiveSupplier((p, d, rowList) -> {
                return true;
            });
            return this;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode node, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            EditorSimpleButtonNode buttonNode = (EditorSimpleButtonNode) node;
            Supplier<Component> messageSupplier = buttonNode.getMessageSupplier(parent, buttonNode);
            return (x, y, width, height, root) -> {
                boolean isActive = buttonNode.getIsActiveSupplier(parent, buttonNode, rowList);
                EditorButton widget = new EditorButton(parent, messageSupplier, isActive, 216, 20, buttonNode.getPressAction(), rowList);
                return new EditorListEntryExpandingOptions(x, y, width, height, index, rowList, root, widget, messageSupplier, node.getTooltipSupplier(parent));
            };
        }

        public Builder setDisplayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setCallback(ISimpleButtonCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setMessageSupplier(ISimpleButtonMessageSupplier messageSupplier) {
            this.messageSupplier = messageSupplier;
            return this;
        }

        public Builder setIsActiveSupplier(ISimpleButtonIsActiveSupplier isActiveSupplier) {
            this.isActiveSupplier = isActiveSupplier;
            return this;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorSimpleButtonNode build() {
            if (this.displayName == null || this.callback == null) {
                throw new IllegalStateException("required fields not set!");
            }
            EditorSimpleButtonNode result = (EditorSimpleButtonNode) super.build();
            return result;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorNode buildInternally() {
            return new EditorSimpleButtonNode(this.displayName, this.tooltipSupplier, this.movable, this.callback, this.listEntryFactory, this.messageSupplier, this.isActiveSupplier);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
