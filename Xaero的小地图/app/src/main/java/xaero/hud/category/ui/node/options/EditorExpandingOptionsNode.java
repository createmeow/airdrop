package xaero.hud.category.ui.node.options;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListEntryExpandingOptions;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButton;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorExpandingOptionsNode.class */
public class EditorExpandingOptionsNode<V> extends EditorOptionsNode<V> {
    protected final List<EditorOptionNode<V>> options;

    protected EditorExpandingOptionsNode(@Nonnull Component displayName, @Nonnull EditorOptionNode<V> currentValue, @Nonnull List<EditorOptionNode<V>> options, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.options = options;
        this.currentValue = currentValue;
    }

    public boolean onSelected(EditorOptionNode<V> option) {
        setCurrentValue(option);
        setExpanded(false);
        return true;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        return this.options;
    }

    @Override // xaero.hud.category.ui.node.options.EditorOptionsNode, xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.literal("");
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorExpandingOptionsNode$Builder.class */
    public static abstract class Builder<V, B extends Builder<V, B>> extends EditorOptionsNode.Builder<V, B> {
        protected final List<EditorOptionNode.Builder<V>> optionBuilders;
        protected final ListFactory listFactory;

        /* renamed from: buildInternally */
        protected abstract EditorOptionsNode<V> buildInternally2(EditorOptionNode<V> editorOptionNode, List<EditorOptionNode<V>> list);

        protected Builder(ListFactory listFactory) {
            this.optionBuilders = listFactory.get();
            this.listFactory = listFactory;
        }

        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            super.setDefault();
            this.optionBuilders.clear();
            return (B) this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            return (x, y, width, height, root) -> {
                EditorExpandingOptionsNode<?> eoData = (EditorExpandingOptionsNode) data;
                boolean isActive = eoData.getIsActiveSupplier().get(parent, eoData);
                EditorButton button = new EditorButton(parent, () -> {
                    return Component.literal("");
                }, isActive, 216, 20, b -> {
                    data.getExpandAction(rowList).run();
                }, rowList);
                if (rowList.readOnly) {
                    button.active = false;
                }
                return new EditorListEntryExpandingOptions(x, y, width, height, index, rowList, root, button, eoData.getMessageSupplier(), data.getTooltipSupplier(parent));
            };
        }

        public B addOptionBuilderFor(V option) {
            this.optionBuilders.add(EditorOptionNode.Builder.begin().setValue(option));
            return (B) this.self;
        }

        public B addOptionBuilder(EditorOptionNode.Builder<V> optionBuilder) {
            this.optionBuilders.add(optionBuilder);
            return (B) this.self;
        }

        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorExpandingOptionsNode<V> build() {
            if (this.listFactory == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (EditorExpandingOptionsNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorOptionsNode<V> buildInternally() {
            Stream<R> map = this.optionBuilders.stream().map((v0) -> {
                return v0.build();
            });
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            List<EditorOptionNode<V>> options = (List) map.collect(listFactory::get, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
            EditorOptionNode<V> currentValueData = null;
            Iterator<EditorOptionNode<V>> it = options.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                EditorOptionNode<V> optionData = it.next();
                if (optionData.getValue() == this.currentValue) {
                    currentValueData = optionData;
                    break;
                }
            }
            if (currentValueData == null) {
                throw new IllegalStateException("current value is not one of the options! " + String.valueOf(this.currentValue));
            }
            return buildInternally2(currentValueData, options);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorExpandingOptionsNode$FinalBuilder.class */
    public static final class FinalBuilder<V> extends Builder<V, FinalBuilder<V>> {
        private FinalBuilder(ListFactory listFactory) {
            super(listFactory);
        }

        public static <V> FinalBuilder<V> begin(ListFactory listFactory) {
            return new FinalBuilder(listFactory).setDefault();
        }

        @Override // xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder
        /* renamed from: buildInternally */
        protected EditorOptionsNode<V> buildInternally2(EditorOptionNode<V> currentValueData, List<EditorOptionNode<V>> options) {
            return new EditorExpandingOptionsNode(this.displayName, currentValueData, options, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }
    }
}
