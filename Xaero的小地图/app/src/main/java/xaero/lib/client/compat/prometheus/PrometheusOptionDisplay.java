package xaero.lib.client.compat.prometheus;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.prometheus.api.roles.client.OptionDisplay;
import earth.terrarium.prometheus.client.screens.roles.options.entries.NumberBoxListEntry;
import earth.terrarium.prometheus.client.screens.roles.options.entries.TextBoxListEntry;
import earth.terrarium.prometheus.client.screens.roles.options.entries.TextListEntry;
import earth.terrarium.prometheus.client.screens.roles.options.entries.TriStateListEntry;
import earth.terrarium.prometheus.common.handlers.role.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import net.minecraft.network.chat.Component;
import xaero.lib.common.compat.prometheus.ModPrometheus;
import xaero.lib.common.compat.prometheus.PrometheusOptions;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.PermissionRegistry;
import xaero.lib.common.util.JsonUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/compat/prometheus/PrometheusOptionDisplay.class */
public class PrometheusOptionDisplay implements OptionDisplay {
    private final ModPrometheus mod;
    private final List<ListEntry> entries;
    private final Map<PermissionNode<?>, ListEntry> mappedEntries;

    private PrometheusOptionDisplay(ModPrometheus mod, List<ListEntry> entries, Map<PermissionNode<?>, ListEntry> mappedEntries) {
        this.mod = mod;
        this.entries = entries;
        this.mappedEntries = mappedEntries;
    }

    public static PrometheusOptionDisplay create(ModPrometheus mod, Role role, SelectionList<ListEntry> ignored) {
        List<ListEntry> entries = new ArrayList<>();
        Map<PermissionNode<?>, ListEntry> mappedEntries = new HashMap<>();
        PrometheusOptions options = (PrometheusOptions) role.getOption(mod.getSerializer());
        entries.add(new TextListEntry(Component.literal(mod.getModId())));
        entries.add(new TextListEntry(Component.translatable("gui.xaero_prometheus_option_hint")));
        for (PermissionNode<?> node : PermissionRegistry.INSTANCE.getModNodes(mod.getModId())) {
            TriStateListEntry textBoxListEntry = null;
            if (node.getType() == Boolean.class) {
                textBoxListEntry = createBooleanEntry(node, options);
            } else if (Number.class.isAssignableFrom(node.getType())) {
                textBoxListEntry = createNumberEntry(node, options);
            } else if (node.getType() == String.class || node.getType() == Component.class) {
                String nodeValueString = null;
                if (node.getType() == String.class) {
                    nodeValueString = (String) options.get(node);
                } else {
                    Component componentValue = (Component) options.get(node);
                    if (componentValue != null) {
                        nodeValueString = JsonUtils.toJson(componentValue);
                    }
                }
                textBoxListEntry = new TextBoxListEntry(nodeValueString == null ? "" : nodeValueString, 200, node.getDisplayName(), node.getComment(), s -> {
                    return true;
                });
            }
            if (textBoxListEntry == null) {
                throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(node.getType()));
            }
            entries.add(textBoxListEntry);
            mappedEntries.put(node, textBoxListEntry);
        }
        return new PrometheusOptionDisplay(mod, entries, mappedEntries);
    }

    private static TriStateListEntry createBooleanEntry(PermissionNode<Boolean> node, PrometheusOptions options) {
        return new TriStateListEntry(node.getDisplayName(), TriState.of((Boolean) options.get(node)), e -> {
            options.put(node, e.state().isDefined() ? Boolean.valueOf(e.state().isTrue()) : null);
        });
    }

    private static <T extends Number> NumberBoxListEntry createNumberEntry(PermissionNode<T> node, PrometheusOptions options) {
        return new FixedNumberBoxListEntry((Number) options.get(node), node.getDisplayName(), node.getComment());
    }

    public List<ListEntry> getDisplayEntries() {
        return this.entries;
    }

    public boolean save(Role role) {
        PrometheusOptions options = PrometheusOptions.Builder.begin().setModId(this.mod.getModId()).build();
        for (PermissionNode<?> node : this.mappedEntries.keySet()) {
            TextBoxListEntry textBoxListEntry = (ListEntry) this.mappedEntries.get(node);
            if (node.getType() == Boolean.class) {
                TriStateListEntry triStateListEntry = (TriStateListEntry) textBoxListEntry;
                TriState state = triStateListEntry.state();
                options.put(node, state.isUndefined() ? null : Boolean.valueOf(state.isTrue()));
            } else if (Number.class.isAssignableFrom(node.getType())) {
                saveNumberEntry(node, textBoxListEntry, options);
            } else if (node.getType() == String.class) {
                TextBoxListEntry textBoxListEntry2 = textBoxListEntry;
                String entryText = textBoxListEntry2.getText();
                options.put(node, entryText.isEmpty() ? null : entryText);
            } else if (node.getType() == Component.class) {
                TextBoxListEntry textBoxListEntry3 = textBoxListEntry;
                String entryText2 = textBoxListEntry3.getText();
                options.put(node, entryText2.isEmpty() ? null : JsonUtils.fromJson(entryText2));
            } else {
                throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(node.getType()));
            }
        }
        role.setData(options);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.Double] */
    /* JADX WARN: Type inference failed for: r0v37, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r0v48, types: [java.lang.Integer] */
    private <T extends Number> void saveNumberEntry(PermissionNode<T> permissionNode, ListEntry listEntry, PrometheusOptions prometheusOptions) {
        Byte bValueOf;
        FixedNumberBoxListEntry fixedNumberBoxListEntry = (FixedNumberBoxListEntry) listEntry;
        if (permissionNode.getType() == Integer.class) {
            OptionalInt intValue = fixedNumberBoxListEntry.getIntValue();
            bValueOf = intValue.isEmpty() ? null : Integer.valueOf(intValue.getAsInt());
        } else if (permissionNode.getType() == Long.class) {
            OptionalLong longValue = fixedNumberBoxListEntry.getLongValue();
            bValueOf = longValue.isEmpty() ? null : Long.valueOf(longValue.getAsLong());
        } else if (permissionNode.getType() == Double.class) {
            OptionalDouble doubleValue = fixedNumberBoxListEntry.getDoubleValue();
            bValueOf = doubleValue.isEmpty() ? null : Double.valueOf(doubleValue.getAsDouble());
        } else if (permissionNode.getType() == Byte.class) {
            OptionalInt byteValue = fixedNumberBoxListEntry.getByteValue();
            bValueOf = byteValue.isEmpty() ? null : Byte.valueOf((byte) byteValue.getAsInt());
        } else {
            throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(permissionNode.getType()));
        }
        prometheusOptions.put(permissionNode, bValueOf);
    }
}
