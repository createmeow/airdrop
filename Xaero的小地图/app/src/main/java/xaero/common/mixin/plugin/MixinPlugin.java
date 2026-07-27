package xaero.common.mixin.plugin;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import xaero.common.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/plugin/MixinPlugin.class */
public class MixinPlugin implements IMixinConfigPlugin {
    private static final Map<String, String> MIXIN_MOD_ID_MAP = ImmutableMap.of("xaero.common.mixin.MixinBatchableBufferSource", "immediatelyfast");

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String modId = MIXIN_MOD_ID_MAP.get(mixinClassName);
        if (modId == null) {
            return true;
        }
        return Services.PLATFORM.checkModForMixin(modId);
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
