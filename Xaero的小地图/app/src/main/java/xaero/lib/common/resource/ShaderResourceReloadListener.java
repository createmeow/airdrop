package xaero.lib.common.resource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import xaero.lib.client.graphics.shader.LibShaders;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/resource/ShaderResourceReloadListener.class */
public class ShaderResourceReloadListener implements PreparableReloadListener {
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier p_10638_, ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_) {
        return p_10638_.wait(Unit.INSTANCE).thenRunAsync(() -> {
            LibShaders.onResourceReload(p_10639_);
        }, p_10643_);
    }
}
