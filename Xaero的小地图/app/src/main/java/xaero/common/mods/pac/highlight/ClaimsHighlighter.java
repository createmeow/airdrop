package xaero.common.mods.pac.highlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.highlight.ChunkHighlighter;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.util.TextSplitter;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientDimensionClaimsManagerAPI;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.server.player.config.PlayerConfig;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/pac/highlight/ClaimsHighlighter.class */
public class ClaimsHighlighter extends ChunkHighlighter {
    private final IClientClaimsManagerAPI claimsManager;
    private final IXaeroMinimap modMain;
    private final ClientConfigManager configManager;
    private List<Component> cachedTooltip;
    private IPlayerChunkClaimAPI cachedTooltipFor;
    private int cachedForWidth;
    private String cachedForCustomName;
    private int cachedForClaimsColor;

    public ClaimsHighlighter(IXaeroMinimap modMain, IClientClaimsManagerAPI claimsManager) {
        super(true);
        this.modMain = modMain;
        this.configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        this.claimsManager = claimsManager;
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public boolean regionHasHighlights(ResourceKey<Level> dimension, int regionX, int regionZ) {
        IClientDimensionClaimsManagerAPI claimsDimension = this.claimsManager.getDimension(dimension.location());
        return (claimsDimension == null || claimsDimension.getRegion(regionX, regionZ) == null) ? false : true;
    }

    @Override // xaero.common.minimap.highlight.ChunkHighlighter
    protected int[] getColors(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        IPlayerChunkClaimAPI currentClaim;
        if (!((Boolean) this.configManager.getEffective(MinimapProfiledConfigOptions.OPAC_CLAIMS)).booleanValue() || (currentClaim = this.claimsManager.get(dimension.location(), chunkX, chunkZ)) == null) {
            return null;
        }
        IPlayerChunkClaimAPI topClaim = this.claimsManager.get(dimension.location(), chunkX, chunkZ - 1);
        IPlayerChunkClaimAPI rightClaim = this.claimsManager.get(dimension.location(), chunkX + 1, chunkZ);
        IPlayerChunkClaimAPI bottomClaim = this.claimsManager.get(dimension.location(), chunkX, chunkZ + 1);
        IPlayerChunkClaimAPI leftClaim = this.claimsManager.get(dimension.location(), chunkX - 1, chunkZ);
        IClientPlayerClaimInfoAPI claimInfo = this.claimsManager.getPlayerInfo(currentClaim.getPlayerId());
        int claimColor = getClaimsColor(currentClaim, claimInfo);
        int claimColorFormatted = ((claimColor & 255) << 24) | (((claimColor >> 8) & 255) << 16) | (((claimColor >> 16) & 255) << 8);
        int borderOpacity = ((Integer) this.configManager.getEffective(MinimapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY)).intValue();
        int fillOpacity = ((Integer) this.configManager.getEffective(MinimapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY)).intValue();
        int centerColor = claimColorFormatted | ((255 * fillOpacity) / 100);
        int sideColor = claimColorFormatted | ((255 * borderOpacity) / 100);
        this.resultStore[0] = centerColor;
        this.resultStore[1] = topClaim != currentClaim ? sideColor : centerColor;
        this.resultStore[2] = rightClaim != currentClaim ? sideColor : centerColor;
        this.resultStore[3] = bottomClaim != currentClaim ? sideColor : centerColor;
        this.resultStore[4] = leftClaim != currentClaim ? sideColor : centerColor;
        return this.resultStore;
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public boolean chunkIsHighlit(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        return this.claimsManager.get(dimension.location(), chunkX, chunkZ) != null;
    }

    @Override // xaero.common.minimap.highlight.ChunkHighlighter
    public void addChunkHighlightTooltips(InfoDisplayCompiler compiler, ResourceKey<Level> dimension, int chunkX, int chunkZ, int width) {
        IPlayerChunkClaimAPI currentClaim;
        if (!((Boolean) this.configManager.getEffective(MinimapProfiledConfigOptions.OPAC_CURRENT_CLAIM)).booleanValue() || (currentClaim = this.claimsManager.get(dimension.location(), chunkX, chunkZ)) == null) {
            return;
        }
        UUID currentClaimId = currentClaim.getPlayerId();
        IClientPlayerClaimInfoAPI claimInfo = this.claimsManager.getPlayerInfo(currentClaimId);
        String customName = getClaimsName(currentClaim, claimInfo);
        int actualClaimsColor = getClaimsColor(currentClaim, claimInfo);
        int claimsColor = actualClaimsColor | (-16777216);
        if (!Objects.equals(currentClaim, this.cachedTooltipFor) || this.cachedForWidth != width || this.cachedForClaimsColor != claimsColor || !Objects.equals(customName, this.cachedForCustomName)) {
            MutableComponent tooltip = Component.literal("□ ").withStyle(s -> {
                return s.withColor(claimsColor);
            });
            if (Objects.equals(currentClaimId, PlayerConfig.SERVER_CLAIM_UUID)) {
                List siblings = tooltip.getSiblings();
                Object[] objArr = new Object[1];
                objArr[0] = currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : "";
                siblings.add(Component.translatable("gui.xaero_pac_server_claim_tooltip", objArr).withStyle(ChatFormatting.WHITE));
            } else if (Objects.equals(currentClaimId, PlayerConfig.EXPIRED_CLAIM_UUID)) {
                List siblings2 = tooltip.getSiblings();
                Object[] objArr2 = new Object[1];
                objArr2[0] = currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : "";
                siblings2.add(Component.translatable("gui.xaero_pac_expired_claim_tooltip", objArr2).withStyle(ChatFormatting.WHITE));
            } else {
                List siblings3 = tooltip.getSiblings();
                Object[] objArr3 = new Object[2];
                objArr3[0] = claimInfo.getPlayerUsername();
                objArr3[1] = currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : "";
                siblings3.add(Component.translatable("gui.xaero_pac_claim_tooltip", objArr3).withStyle(ChatFormatting.WHITE));
            }
            if (!customName.isEmpty()) {
                tooltip.getSiblings().add(0, Component.literal(I18n.get(customName, new Object[0]) + " - ").withStyle(ChatFormatting.WHITE));
            }
            this.cachedTooltip = new ArrayList();
            TextSplitter.splitTextIntoLines(this.cachedTooltip, width, width, tooltip, null);
            this.cachedTooltipFor = currentClaim;
            this.cachedForWidth = width;
            this.cachedForCustomName = customName;
            this.cachedForClaimsColor = claimsColor;
        }
        for (int i = 0; i < this.cachedTooltip.size(); i++) {
            compiler.addLine(this.cachedTooltip.get(i));
        }
    }

    private String getClaimsName(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI claimInfo) {
        int subConfigIndex = currentClaim.getSubConfigIndex();
        String customName = claimInfo.getClaimsName(subConfigIndex);
        if (subConfigIndex != -1 && customName == null) {
            customName = claimInfo.getClaimsName();
        }
        return customName;
    }

    private int getClaimsColor(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI claimInfo) {
        int subConfigIndex = currentClaim.getSubConfigIndex();
        Integer actualClaimsColor = claimInfo.getClaimsColor(subConfigIndex);
        if (subConfigIndex != -1 && actualClaimsColor == null) {
            actualClaimsColor = Integer.valueOf(claimInfo.getClaimsColor());
        }
        return actualClaimsColor.intValue();
    }
}
