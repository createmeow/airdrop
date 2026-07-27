package xaero.hud.category.ui.node;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListEntryWidget;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButton;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.options.EditorSimpleButtonNode;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorAdderNode.class */
public final class EditorAdderNode extends EditorNode {
    private final Component displayName;
    private final EditorTextFieldOptionsNode nameField;
    private final EditorSimpleButtonNode confirmButton;
    private boolean confirmed;

    private EditorAdderNode(@Nonnull Component displayName, @Nonnull EditorTextFieldOptionsNode nameField, @Nonnull EditorSimpleButtonNode confirmButton, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.displayName = displayName;
        this.confirmButton = confirmButton;
        this.nameField = nameField;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded) {
            reset();
        }
    }

    public void reset() {
        this.confirmed = false;
        this.nameField.resetInput("");
    }

    public EditorTextFieldOptionsNode getNameField() {
        return this.nameField;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        return Lists.newArrayList(new EditorNode[]{this.nameField, this.confirmButton});
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorAdderNode$Builder.class */
    public static final class Builder extends EditorNode.Builder<Builder> {
        private Component displayName;
        private final EditorTextFieldOptionsNode.Builder nameFieldBuilder;
        private final EditorSimpleButtonNode.Builder confirmButtonBuilder = EditorSimpleButtonNode.Builder.begin();

        private Builder(ListFactory listFactory) {
            this.nameFieldBuilder = EditorTextFieldOptionsNode.Builder.begin(listFactory);
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public Builder setDefault() {
            super.setDefault();
            setDisplayName(null);
            this.nameFieldBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_category_name"));
            this.confirmButtonBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_category_confirm"));
            this.confirmButtonBuilder.setCallback((parent, bd, rl) -> {
                EditorAdderNode adder = (EditorAdderNode) parent;
                adder.confirmed = !adder.getNameField().getResult().isEmpty();
                adder.setExpanded(false);
                rl.setLastExpandedData(adder);
                rl.updateEntries();
            });
            return this;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            return (x, y, width, height, root) -> {
                EditorButton button = new EditorButton(parent, true, 216, 20, data, rowList);
                if (rowList.readOnly) {
                    button.active = false;
                }
                return new EditorListEntryWidget(x, y, width, height, index, rowList, root, button, data.getTooltipSupplier(parent));
            };
        }

        public Builder setDisplayName(Component displayName) {
            this.displayName = displayName;
            return (Builder) this.self;
        }

        public EditorTextFieldOptionsNode.Builder getNameFieldBuilder() {
            return this.nameFieldBuilder;
        }

        public EditorSimpleButtonNode.Builder getConfirmButtonBuilder() {
            return this.confirmButtonBuilder;
        }

        public static Builder begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorAdderNode build() {
            if (this.displayName == null) {
                throw new IllegalStateException("required fields not set!");
            }
            EditorAdderNode result = (EditorAdderNode) super.build();
            return result;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorAdderNode buildInternally() {
            if (this.nameFieldBuilder.needsInputStringValidator()) {
                this.nameFieldBuilder.setInputStringValidator(s -> {
                    return true;
                });
            }
            return new EditorAdderNode(this.displayName, this.nameFieldBuilder.build(), this.confirmButtonBuilder.build(), this.movable, this.listEntryFactory, this.tooltipSupplier);
        }
    }
}
