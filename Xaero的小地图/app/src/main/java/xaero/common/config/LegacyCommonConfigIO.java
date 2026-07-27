package xaero.common.config;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.common.config.primary.option.MinimapPrimaryCommonConfigOptions;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/config/LegacyCommonConfigIO.class */
public class LegacyCommonConfigIO {
    private final Path configFilePath;
    private boolean allowCaveModeOnServer;
    private boolean allowNetherCaveModeOnServer;
    private boolean shouldEnableEveryoneTracksEveryone;

    public LegacyCommonConfigIO(Path configFilePath) {
        this.configFilePath = configFilePath;
    }

    /* JADX WARN: Finally extract failed */
    public void load() throws IOException {
        ConfigProfile defaultEnforcedProfile = HudMod.INSTANCE.getHudConfigs().getServerConfigManager().getDefaultEnforcedProfile();
        try {
            BufferedInputStream bufferedOutput = new BufferedInputStream(new FileInputStream(this.configFilePath.toFile()));
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedOutput));
                while (true) {
                    try {
                        try {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            } else {
                                readLine(line.split(":"));
                            }
                        } catch (Throwable th) {
                            try {
                                reader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManagerIO().save();
                        HudMod.INSTANCE.getHudConfigs().getServerConfigProfileIO().save(defaultEnforcedProfile);
                        reader.close();
                        IOUtils.tryQuickFileBackupMove(this.configFilePath, 10);
                        throw th3;
                    }
                }
                if (this.allowCaveModeOnServer && this.allowNetherCaveModeOnServer) {
                    HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManagerIO().save();
                    HudMod.INSTANCE.getHudConfigs().getServerConfigProfileIO().save(defaultEnforcedProfile);
                    reader.close();
                    IOUtils.tryQuickFileBackupMove(this.configFilePath, 10);
                    reader.close();
                    bufferedOutput.close();
                    return;
                }
                if (!this.allowCaveModeOnServer && !this.allowNetherCaveModeOnServer) {
                    defaultEnforcedProfile.set(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED, false);
                    HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManagerIO().save();
                    HudMod.INSTANCE.getHudConfigs().getServerConfigProfileIO().save(defaultEnforcedProfile);
                    reader.close();
                    IOUtils.tryQuickFileBackupMove(this.configFilePath, 10);
                    reader.close();
                    bufferedOutput.close();
                    return;
                }
                if (this.allowCaveModeOnServer) {
                    defaultEnforcedProfile.set(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS, Sets.newHashSet(new ResourceLocation[]{Level.OVERWORLD.location(), Level.END.location()}));
                    HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManagerIO().save();
                    HudMod.INSTANCE.getHudConfigs().getServerConfigProfileIO().save(defaultEnforcedProfile);
                    reader.close();
                    IOUtils.tryQuickFileBackupMove(this.configFilePath, 10);
                    reader.close();
                    bufferedOutput.close();
                    return;
                }
                defaultEnforcedProfile.set(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS, Sets.newHashSet(new ResourceLocation[]{Level.NETHER.location()}));
                HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManagerIO().save();
                HudMod.INSTANCE.getHudConfigs().getServerConfigProfileIO().save(defaultEnforcedProfile);
                reader.close();
                IOUtils.tryQuickFileBackupMove(this.configFilePath, 10);
                reader.close();
                bufferedOutput.close();
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean readLine(String[] args) {
        if (args[0].equals("allowCaveModeOnServer")) {
            this.allowCaveModeOnServer = args[1].equals("true");
            return true;
        }
        if (args[0].equals("allowNetherCaveModeOnServer")) {
            this.allowNetherCaveModeOnServer = args[1].equals("true");
            return true;
        }
        if (args[0].equals("allowRadarOnServer")) {
            if (!args[1].equals("true")) {
                HudMod.INSTANCE.getHudConfigs().getServerConfigManager().getDefaultEnforcedProfile().set(MinimapProfiledConfigOptions.DISPLAY_RADAR, false);
                return true;
            }
            return true;
        }
        if (args[0].equals("registerStatusEffects")) {
            HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManager().getConfig().set(MinimapPrimaryCommonConfigOptions.REGISTER_EFFECTS, Boolean.valueOf(args[1].equals("true")));
            return true;
        }
        if (args[0].equals("everyoneTracksEveryone") && args[1].equals("true")) {
            this.shouldEnableEveryoneTracksEveryone = true;
            return true;
        }
        return false;
    }

    public boolean shouldEnableEveryoneTracksEveryone() {
        return this.shouldEnableEveryoneTracksEveryone;
    }
}
