package xaero.hud.module;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;
import xaero.common.HudMod;
import xaero.hud.HudSession;
import xaero.hud.module.ModuleSession;
import xaero.hud.pushbox.PushboxHandler;
import xaero.hud.render.module.IModuleRenderer;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.option.BooleanConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/module/HudModule.class */
public final class HudModule<MS extends ModuleSession<MS>> {
    private final ResourceLocation id;
    private final Component displayName;
    private final TriFunction<HudMod, HudModule<MS>, ClientPacketListener, MS> sessionFactory;
    private final Supplier<IModuleRenderer<MS>> rendererFactory;
    private final Function<Screen, Screen> configScreenFactory;
    private IModuleRenderer<MS> renderer;
    private ModuleTransform unconfirmedTransform;
    private BooleanConfigOption activeOption;
    private ModuleTransform transform = new ModuleTransform();
    private PushboxHandler.State pushState = new PushboxHandler.State();

    public HudModule(ResourceLocation id, Component displayName, TriFunction<HudMod, HudModule<MS>, ClientPacketListener, MS> sessionFactory, Supplier<IModuleRenderer<MS>> rendererFactory, Function<Screen, Screen> configScreenFactory, BooleanConfigOption activeOption) {
        this.displayName = displayName;
        this.activeOption = activeOption;
        this.id = id;
        this.sessionFactory = sessionFactory;
        this.rendererFactory = rendererFactory;
        this.configScreenFactory = configScreenFactory;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean isActive(ClientConfigManager configManager) {
        return ((Boolean) configManager.getEffective(this.activeOption)).booleanValue();
    }

    public void setActive(ClientConfigManager configManager, boolean active) {
        configManager.getCurrentProfile().set(this.activeOption, Boolean.valueOf(active));
    }

    public MS getCurrentSession() {
        HudSession currentSession = HudSession.getCurrentSession();
        if (currentSession == null) {
            return null;
        }
        return (MS) currentSession.getSession(this);
    }

    public IModuleRenderer<MS> getRenderer() {
        if (this.renderer == null) {
            this.renderer = this.rendererFactory.get();
        }
        return this.renderer;
    }

    public ModuleTransform getUsedTransform() {
        if (Minecraft.getInstance().screen != null) {
            return getUnconfirmedTransform();
        }
        if (this.unconfirmedTransform != null) {
            cancelTransform();
        }
        return this.transform;
    }

    public ModuleTransform getUnconfirmedTransform() {
        if (this.unconfirmedTransform == null) {
            this.unconfirmedTransform = this.transform.copy();
        }
        return this.unconfirmedTransform;
    }

    public void confirmTransform() {
        if (this.unconfirmedTransform == null) {
            return;
        }
        this.transform = this.unconfirmedTransform;
        this.unconfirmedTransform = null;
    }

    public ModuleTransform getConfirmedTransform() {
        return this.transform;
    }

    public void setTransform(ModuleTransform transform) {
        this.transform = transform;
        this.unconfirmedTransform = null;
    }

    public void cancelTransform() {
        this.unconfirmedTransform = null;
    }

    public PushboxHandler.State getPushState() {
        return this.pushState;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public Function<Screen, Screen> getConfigScreenFactory() {
        return this.configScreenFactory;
    }

    TriFunction<HudMod, HudModule<MS>, ClientPacketListener, MS> getSessionFactory() {
        return this.sessionFactory;
    }

    void setRenderer(IModuleRenderer<MS> renderer) {
        this.renderer = renderer;
    }
}
