package xaero.lib.common.config.server.sync;

import java.util.Iterator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.register.ConfigChannelRegistry;
import xaero.lib.common.packet.ClientboundServerHandshakePacket;
import xaero.lib.common.packet.config.AbstractConfigPacket;
import xaero.lib.common.packet.config.ConfigChannelPacket;
import xaero.lib.common.packet.config.profile.ConfigProfileInfoPacket;
import xaero.lib.common.player.ServerPlayerData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/server/sync/ServerPlayerConfigSynchronizer.class */
public class ServerPlayerConfigSynchronizer {
    private final ServerPlayerData playerData;
    private ConfigChannel currentOutputChannel;
    private ConfigChannel currentInputChannel;

    public ServerPlayerConfigSynchronizer(ServerPlayerData playerData) {
        this.playerData = playerData;
    }

    public void send(ConfigChannel channel, Object packet) {
        ensureOutputChannel(channel);
        XaeroLib.INSTANCE.getPacketHandler().sendToPlayer(this.playerData, (ServerPlayerData) packet);
    }

    private void ensureOutputChannel(ConfigChannel channel) {
        if (channel == this.currentOutputChannel) {
            return;
        }
        XaeroLib.INSTANCE.getPacketHandler().sendToPlayer(this.playerData, (ServerPlayerData) new ConfigChannelPacket(channel.getId()));
        this.currentOutputChannel = channel;
    }

    public void receiveChannelId(ResourceLocation channelId) {
        if (channelId == null) {
            this.currentInputChannel = null;
        } else {
            this.currentInputChannel = ConfigChannelRegistry.INSTANCE.get(channelId);
        }
    }

    public void onProfileRequestPacket(String profileId) {
        if (this.currentInputChannel == null) {
            return;
        }
        this.currentInputChannel.getServerConfigSynchronizer().onProfileRequestPacket(this, profileId);
    }

    public void onEditProfilePacket(String profileId, Iterable<AbstractConfigPacket.OptionEntry> entries) {
        if (this.currentInputChannel == null) {
            return;
        }
        this.currentInputChannel.getServerConfigSynchronizer().onEditProfilePacket(profileId, entries, this.playerData);
    }

    public void onConfigProfileInfoPacket(List<ConfigProfileInfoPacket.Entry> entries, String defaultEnforcedProfileId) {
        if (this.currentInputChannel == null) {
            return;
        }
        this.currentInputChannel.getServerConfigSynchronizer().onConfigProfileInfoPacket(entries, defaultEnforcedProfileId, this.playerData);
    }

    public void onDeleteConfigProfilePacket(String profileId) {
        if (this.currentInputChannel == null) {
            return;
        }
        this.currentInputChannel.getServerConfigSynchronizer().onDeleteConfigProfilePacket(profileId, this.playerData);
    }

    public void onCreateConfigProfilePacket(String id, String name, String profileToCopy) {
        if (this.currentInputChannel == null) {
            return;
        }
        this.currentInputChannel.getServerConfigSynchronizer().onCreateConfigProfilePacket(id, name, profileToCopy);
    }

    public void handleLogin() {
        XaeroLib.INSTANCE.getPacketHandler().sendToPlayer(this.playerData, (ServerPlayerData) new ClientboundServerHandshakePacket());
        Iterator<ConfigChannel> it = ConfigChannelRegistry.INSTANCE.iterator();
        while (it.hasNext()) {
            ConfigChannel channel = it.next();
            channel.getServerConfigSynchronizer().handleLogin(this);
        }
    }

    public ConfigChannel getCurrentInputChannel() {
        return this.currentInputChannel;
    }

    public ServerPlayerData getPlayerData() {
        return this.playerData;
    }
}
