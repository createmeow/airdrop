package xaero.lib.common.util;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.ResourceKey;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/JsonUtils.class */
public class JsonUtils {
    private static final HolderLookup.Provider EMPTY_REGISTRY_LOOKUP_PROVIDER = new HolderLookup.Provider() { // from class: xaero.lib.common.util.JsonUtils.1
        public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
            return Stream.empty();
        }

        public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> resourceKey) {
            return Optional.empty();
        }
    };

    public static String toJson(Component component) {
        if (component.getContents() instanceof PlainTextContents) {
            return component.getContents().text();
        }
        try {
            return Component.Serializer.toJson(component, EMPTY_REGISTRY_LOOKUP_PROVIDER);
        } catch (Throwable th) {
            return null;
        }
    }

    public static Component fromJson(String json) {
        try {
            return Component.Serializer.fromJson(json, EMPTY_REGISTRY_LOOKUP_PROVIDER);
        } catch (Throwable th) {
            return Component.literal(json);
        }
    }
}
