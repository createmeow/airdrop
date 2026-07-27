package xaero.common.mods.pac;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.mods.pac.highlight.ClaimsHighlighter;
import xaero.common.mods.pac.party.OPACPlayerTrackerSystem;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/pac/SupportOpenPartiesAndClaims.class */
public class SupportOpenPartiesAndClaims {
    private final IXaeroMinimap modMain;
    private final OpenPACClientAPI api = OpenPACClientAPI.get();
    private final IClientClaimsManagerAPI claimsManager = this.api.getClaimsManager();
    private final IClientPartyStorageAPI partyStorage = this.api.getClientPartyStorage();
    private final IPlayerConfigClientStorageManagerAPI playerConfigs = this.api.getPlayerConfigClientStorageManager();

    public SupportOpenPartiesAndClaims(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public void register() {
        this.claimsManager.getTracker().register(new ClientClaimChangeListener());
        this.modMain.getPlayerTrackerSystemManager().register("openpartiesandclaims", new OPACPlayerTrackerSystem(this));
    }

    public IPlayerChunkClaimAPI claimAt(ResourceLocation dimension, int chunkX, int chunkZ) {
        return this.claimsManager.get(dimension, chunkX, chunkZ);
    }

    public void onMapRender(Minecraft mc, PoseStack matrixStack, int scaledMouseX, int scaledMouseY, float partialTicks, ResourceLocation dimension, int highlightChunkX, int highlightChunkZ) {
    }

    public boolean isFromParty(UUID playerId) {
        IClientPartyAPI party = this.partyStorage.getParty();
        return (party == null || this.partyStorage.getParty().getMemberInfo(playerId) == null) ? false : true;
    }

    public void registerHighlighters(HighlighterRegistry highlightRegistry) {
        highlightRegistry.register(new ClaimsHighlighter(this.modMain, this.claimsManager));
    }

    public IXaeroMinimap getModMain() {
        return this.modMain;
    }

    public Iterator<IPartyMemberDynamicInfoSyncableAPI> getAllyIterator() {
        return this.partyStorage.getPartyMemberDynamicInfoSyncableStorage().getAllStream().iterator();
    }
}
