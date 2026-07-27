package xaero.lib.common.config.option.value.type;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import xaero.common.minimap.write.MinimapWriter;
import xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec;
import xaero.lib.common.config.option.value.sync.serialization.ConfigValueSyncCodec;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/type/CollectionConfigValueType.class */
public class CollectionConfigValueType<T> extends ConfigValueType<Set<T>> {
    private CollectionConfigValueType(ConfigValueIOCodec<Set<T>> ioCodec, ConfigValueSyncCodec<Set<T>, ? extends Tag> syncCodec) {
        super(ioCodec, syncCodec);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/type/CollectionConfigValueType$Builder.class */
    public static final class Builder<T> extends ConfigValueType.Builder<Set<T>, Builder<T>> {
        private ConfigValueType<T> elementValueType;
        private Character ioCodecSeparator;

        private Builder() {
        }

        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public Builder<T> setDefault() {
            super.setDefault();
            setElementValueType(null);
            setIoCodecSeparator(null);
            return (Builder) this.self;
        }

        public Builder<T> setElementValueType(ConfigValueType<T> elementValueType) {
            this.elementValueType = elementValueType;
            return (Builder) this.self;
        }

        public Builder<T> setIoCodecSeparator(Character ioCodecSeparator) {
            this.ioCodecSeparator = ioCodecSeparator;
            return (Builder) this.self;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public Builder<T> setIoCodec(ConfigValueIOCodec<Set<T>> configValueIOCodec) {
            if (configValueIOCodec != 0) {
                throw new IllegalArgumentException("You must use setElementValueType with the list config value types!");
            }
            return (Builder) super.setIoCodec((ConfigValueIOCodec) configValueIOCodec);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public Builder<T> setSyncCodec(ConfigValueSyncCodec<Set<T>, ? extends Tag> configValueSyncCodec) {
            if (configValueSyncCodec != 0) {
                throw new IllegalArgumentException("You must use setElementValueType with the list config value types!");
            }
            return (Builder) super.setSyncCodec((ConfigValueSyncCodec) configValueSyncCodec);
        }

        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public ConfigValueType<Set<T>> build() {
            if (this.elementValueType == null || this.ioCodecSeparator == null) {
                throw new IllegalStateException();
            }
            ConfigValueType<T> finalElementValueType = this.elementValueType;
            String finalIoCodecSeparator = this.ioCodecSeparator;
            String separatorEscapeString = finalIoCodecSeparator.equals((char) 167) ? "$sep$" : "§sep§";
            this.ioCodec = new ConfigValueIOCodec<>(set -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('[');
                boolean first = true;
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String encodedElement = finalElementValueType.getIoCodec().encode(it.next(), null, null);
                    if (!encodedElement.isEmpty()) {
                        if (!first) {
                            stringBuilder.append(finalIoCodecSeparator).append(" ");
                        }
                        stringBuilder.append(encodedElement.replace(finalIoCodecSeparator, separatorEscapeString));
                        first = false;
                    }
                }
                stringBuilder.append(']');
                return stringBuilder.toString();
            }, encodedList -> {
                if (encodedList.startsWith("[") && encodedList.endsWith("]")) {
                    encodedList = encodedList.substring(1, encodedList.length() - 1);
                }
                String[] splitString = encodedList.split(finalIoCodecSeparator);
                Set<T> decodedSet = new LinkedHashSet<>();
                for (String str : splitString) {
                    String elementString = str.replace(separatorEscapeString, finalIoCodecSeparator).trim();
                    if (!elementString.isEmpty()) {
                        T decodedElement = finalElementValueType.getIoCodec().decode(elementString, null, null);
                        if (decodedElement == null) {
                            return null;
                        }
                        decodedSet.add(decodedElement);
                    }
                }
                return Collections.unmodifiableSet(decodedSet);
            }, MinimapWriter.NO_Y_VALUE);
            this.syncCodec = new ConfigValueSyncCodec<>(set2 -> {
                ListTag listTag = new ListTag();
                Iterator it = set2.iterator();
                while (it.hasNext()) {
                    listTag.add(finalElementValueType.getSyncCodec().encode(it.next()));
                }
                return listTag;
            }, listTag -> {
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                Iterator it = listTag.iterator();
                while (it.hasNext()) {
                    Tag tag = (Tag) it.next();
                    Object objDecodeElement = decodeElement(tag, finalElementValueType.getSyncCodec());
                    if (objDecodeElement == null) {
                        return null;
                    }
                    linkedHashSet.add(objDecodeElement);
                }
                return Collections.unmodifiableSet(linkedHashSet);
            });
            return super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public CollectionConfigValueType<T> buildInternally() {
            return new CollectionConfigValueType<>(this.ioCodec, this.syncCodec);
        }

        private static <TAG extends Tag, T> T decodeElement(Tag tag, ConfigValueSyncCodec<T, TAG> syncCodec) {
            return syncCodec.decode(tag);
        }

        public static <T> Builder<T> begin() {
            return new Builder().setDefault();
        }
    }
}
