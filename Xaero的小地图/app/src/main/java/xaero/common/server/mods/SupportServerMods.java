package xaero.common.server.mods;

import xaero.common.IXaeroMinimap;
import xaero.common.server.mods.argonauts.SupportArgonautsServer;
import xaero.common.server.mods.ftbteams.SupportFTBTeamsServer;
import xaero.common.server.mods.opac.SupportOPACServer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/mods/SupportServerMods.class */
public class SupportServerMods {
    private SupportFTBTeamsServer ftbTeams;
    private SupportArgonautsServer argonauts;
    private SupportOPACServer opac;
    private SupportWorldMapServer worldmap;

    public void check(IXaeroMinimap modMain) throws ClassNotFoundException {
        try {
            Class.forName("dev.ftb.mods.ftbteams.api.FTBTeamsAPI");
            this.ftbTeams = new SupportFTBTeamsServer();
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("earth.terrarium.argonauts.api.ApiHelper");
            this.argonauts = new SupportArgonautsServer();
        } catch (ClassNotFoundException e2) {
        }
        try {
            Class.forName("xaero.pac.common.server.api.OpenPACServerAPI");
            this.opac = new SupportOPACServer();
        } catch (ClassNotFoundException e3) {
        }
        try {
            Class.forName("xaero.map.WorldMap");
            this.worldmap = new SupportWorldMapServer();
        } catch (ClassNotFoundException e4) {
        }
    }

    public boolean hasFtbTeams() {
        return this.ftbTeams != null;
    }

    public SupportFTBTeamsServer getFtbTeams() {
        return this.ftbTeams;
    }

    public boolean hasArgonauts() {
        return this.argonauts != null;
    }

    public SupportArgonautsServer getArgonauts() {
        return this.argonauts;
    }

    public boolean hasOpac() {
        return this.opac != null;
    }

    public SupportOPACServer getOpac() {
        return this.opac;
    }

    public boolean hasWorldmap() {
        return this.worldmap != null;
    }

    public SupportWorldMapServer getWorldmap() {
        return this.worldmap;
    }
}
