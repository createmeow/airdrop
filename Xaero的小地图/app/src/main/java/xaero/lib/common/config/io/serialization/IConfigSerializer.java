package xaero.lib.common.config.io.serialization;

import java.nio.file.Path;
import javax.annotation.Nullable;
import xaero.lib.common.config.Config;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/serialization/IConfigSerializer.class */
public interface IConfigSerializer<C extends Config> {
    String serialize(C c, @Nullable Path path);

    C deserialize(String str, boolean z, String str2, @Nullable Path path);
}
