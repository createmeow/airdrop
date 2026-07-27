package xaero.hud.category.ui.node;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.ConnectionLineType;
import xaero.hud.category.ui.entry.EditorListEntryTextWithAction;
import xaero.hud.category.ui.entry.EditorListEntryWrapper;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.common.gui.widget.TooltipInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorNode.class */
public abstract class EditorNode {
    private final boolean movable;
    private boolean expanded;
    protected final EditorListRootEntryFactory listEntryFactory;
    protected final IEditorDataTooltipSupplier tooltipSupplier;

    public abstract Component getDisplayName();

    public abstract List<EditorNode> getSubNodes();

    public EditorNode(boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        this.movable = movable;
        this.listEntryFactory = listEntryFactory;
        this.tooltipSupplier = tooltipSupplier;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        List<EditorNode> subExpandables;
        this.expanded = expanded;
        if (expanded || (subExpandables = getSubNodes()) == null) {
            return;
        }
        for (EditorNode sub : subExpandables) {
            if (sub.isExpanded()) {
                sub.setExpanded(false);
                return;
            }
        }
    }

    public final EditorListRootEntryFactory getListEntryFactory() {
        return this.listEntryFactory;
    }

    public Supplier<Tooltip> getTooltipSupplier(EditorNode parent) {
        if (this.tooltipSupplier == null) {
            return null;
        }
        return this.tooltipSupplier.apply(parent, this);
    }

    public Runnable getExpandAction(GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        return () -> {
            List<?> subExpandables = getSubNodes();
            if (subExpandables == null || subExpandables.isEmpty()) {
                return;
            }
            setExpanded(true);
            rowList.setLastExpandedData(this);
            Iterator<?> it = subExpandables.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object o = it.next();
                EditorNode sed = (EditorNode) o;
                if (sed.isExpanded()) {
                    sed.setExpanded(false);
                    break;
                }
            }
            rowList.updateEntries();
        };
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorNode$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        protected B self = this;
        protected EditorListRootEntryFactory listEntryFactory;
        protected IEditorDataTooltipSupplier tooltipSupplier;
        protected boolean movable;

        protected abstract EditorNode buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setMovable(false);
            setListEntryFactory(this::mainEntryFactory);
            setTooltipSupplier(null);
            return this.self;
        }

        protected EditorListRootEntry mainEntryFactory(EditorNode data, EditorNode parent, int index, ConnectionLineType lineType, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, int screenWidth, boolean isFinalExpanded) {
            return new EditorListEntryWrapper(getCenteredEntryFactory(data, parent, index, rowList), screenWidth, index, rowList, lineType, data);
        }

        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            return (x, y, width, height, root) -> {
                return new EditorListEntryTextWithAction(x, y, width, height, index, rowList, root, data.getExpandAction(rowList), data.getTooltipSupplier(parent));
            };
        }

        public B setMovable(boolean movable) {
            this.movable = movable;
            return this.self;
        }

        public B setListEntryFactory(EditorListRootEntryFactory listEntryFactory) {
            this.listEntryFactory = listEntryFactory;
            return this.self;
        }

        public B setTooltipSupplier(IEditorDataTooltipSupplier tooltipSupplier) {
            this.tooltipSupplier = tooltipSupplier;
            return this.self;
        }

        public B setTooltipInfoSupplier(BiFunction<EditorNode, EditorNode, TooltipInfo> biFunction) {
            return (B) setTooltipSupplier((parent, data) -> {
                TooltipInfo info = (TooltipInfo) biFunction.apply(parent, data);
                if (info == null) {
                    return null;
                }
                return new Tooltip(info);
            });
        }

        public EditorNode build() {
            if (this.listEntryFactory == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return buildInternally();
        }
    }
}
