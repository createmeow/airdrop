package xaero.hud.preset;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/preset/HudPreset.class */
public final class HudPreset {
    private final ResourceLocation id;
    private final Component name;
    private final Set<ModulePreset<?>> modulePresets;
    private boolean applied;

    private HudPreset(ResourceLocation id, Component name, Set<ModulePreset<?>> modulePresets) {
        this.id = id;
        this.name = name;
        this.modulePresets = modulePresets;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Component getName() {
        return this.name;
    }

    public void apply() {
        this.applied = true;
        for (ModulePreset<?> modulePreset : this.modulePresets) {
            modulePreset.apply();
        }
    }

    public void confirm() {
        if (!this.applied) {
            return;
        }
        this.applied = false;
        for (ModulePreset<?> modulePreset : this.modulePresets) {
            modulePreset.confirm();
        }
    }

    public void cancel() {
        if (!this.applied) {
            return;
        }
        this.applied = false;
        for (ModulePreset<?> modulePreset : this.modulePresets) {
            modulePreset.cancel();
        }
    }

    public void applyAndConfirm() {
        apply();
        confirm();
        for (ModulePreset<?> modulePreset : this.modulePresets) {
            modulePreset.getModule().confirmTransform();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/preset/HudPreset$Builder.class */
    public static final class Builder {
        private ResourceLocation id;
        private Component name;
        private final Set<ModulePreset<?>> modulePresets = new HashSet();

        private Builder() {
        }

        public Builder setDefault() {
            this.modulePresets.clear();
            return this;
        }

        public Builder setId(ResourceLocation id) {
            this.id = id;
            return this;
        }

        public Builder setName(Component name) {
            this.name = name;
            return this;
        }

        public Builder addModulePreset(ModulePreset<?> modulePreset) {
            this.modulePresets.add(modulePreset);
            return this;
        }

        public HudPreset build() {
            if (this.id == null || this.name == null) {
                throw new IllegalStateException();
            }
            return new HudPreset(this.id, this.name, this.modulePresets);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
