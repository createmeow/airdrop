package xaero.lib.patreon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.Cipher;
import net.minecraft.client.Minecraft;
import xaero.hud.io.HudIO;
import xaero.lib.XaeroLib;
import xaero.lib.client.online.decrypt.DecryptInputStream;
import xaero.lib.common.config.primary.option.LibPrimaryCommonConfigOptions;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/patreon/Patreon.class */
public class Patreon {
    private static boolean hasAutoUpdates;
    private static int onlineWidgetLevel;
    private static boolean notificationDisplayed;
    private static String updateLocation;
    private static Cipher cipher;
    private static File optionsFile;
    private static boolean loaded = false;
    private static HashMap<String, Object> mods = new HashMap<>();
    private static ArrayList<Object> outdatedMods = new ArrayList<>();
    private static int KEY_VERSION = 4;
    private static String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBeELcruvAEIeLF/UsWF/v5rxyRXIpCs+eORLCbDw5cz9jHsnoypQKx0RTk5rcXIeA0HbEfY0eREB25quHjhZKul7MnzotQT+F2Qb1bPfHa6+SPie+pj79GGGAFP3npki6RqoU/wyYkd1tOomuD8v5ytEkOPC4U42kxxvx23A7vH6w46dew/E/HvfbBvZF2KrqdJtwKAunk847C3FgyhVq8/vzQc6mqAW6Mmn4zlwFvyCnTOWjIRw/I93WIM/uvhE3lt6pmtrWA2yIbKIj1z4pgG/K72EqHfYLGkBFTh7fV1wwCbpNTXZX2JnTfmvMGqzHjq7FijwVfCpFB/dWR3wQIDAQAB";

    static {
        try {
            cipher = Cipher.getInstance("RSA");
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.getDecoder().decode(getPublicKeyString2().getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            PublicKey publicKey = factory.generatePublic(X509publicKey);
            cipher.init(2, publicKey);
        } catch (Exception e) {
            cipher = null;
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
        optionsFile = Services.PLATFORM.getGameDir().resolve("config").resolve("xaeropatreon.txt").toFile();
    }

    public static void checkPatreon() {
        URLConnection conn;
        if (((Boolean) XaeroLib.INSTANCE.getLibConfigChannel().getPrimaryCommonConfigManager().getEffective(LibPrimaryCommonConfigOptions.ALLOW_INTERNET)).booleanValue()) {
            synchronized (mods) {
                if (loaded) {
                    return;
                }
                loadSettings();
                String s = "http://data.chocolateminecraft.com/Versions_" + KEY_VERSION + "/Patreon2.dat";
                try {
                    try {
                        URL url = new URL(s.replaceAll(" ", "%20"));
                        conn = url.openConnection();
                        conn.setReadTimeout(900);
                        conn.setConnectTimeout(900);
                    } catch (IOException ioe) {
                        XaeroLib.LOGGER.warn("io exception while checking patreon: {}", ioe.getMessage());
                        mods.clear();
                        loaded = true;
                    } catch (Throwable e) {
                        XaeroLib.LOGGER.error("suppressed exception", e);
                        mods.clear();
                        loaded = true;
                    }
                    if (conn.getContentLengthLong() > 524288) {
                        throw new IOException("Input too long to trust!");
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new DecryptInputStream(conn.getInputStream(), cipher), "UTF8"));
                    boolean parsingWidgets = false;
                    boolean parsingPatrons = false;
                    String localPlayerName = Minecraft.getInstance().getUser().getName();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        if (line.startsWith("PATREON")) {
                            parsingPatrons = true;
                            break;
                        } else if (line.startsWith("WIDGETS")) {
                            parsingWidgets = true;
                        } else if (parsingWidgets && line.startsWith("data_widget") && line.length() > 11) {
                            XaeroLib.INSTANCE.getClient().getWidgetLoader().loadWidget(line.substring(12));
                        }
                    }
                    while (true) {
                        String line2 = reader.readLine();
                        if (line2 == null || line2.equals("LAYOUTS")) {
                            break;
                        }
                        if (parsingPatrons) {
                            String[] rewards = line2.split(HudIO.SEPARATOR);
                            if (rewards.length > 1 && rewards[0].equalsIgnoreCase(localPlayerName)) {
                                for (int i = 1; i < rewards.length; i++) {
                                    String rewardString = rewards[i].trim();
                                    if ("updates".equals(rewardString)) {
                                        hasAutoUpdates = true;
                                    } else {
                                        String[] keyAndValue = rewardString.split(":");
                                        if (keyAndValue.length >= 2 && keyAndValue[0].equals("widget_level")) {
                                            try {
                                                onlineWidgetLevel = Integer.parseInt(keyAndValue[1]);
                                            } catch (NumberFormatException e2) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    updateLocation = reader.readLine();
                    while (true) {
                        String line3 = reader.readLine();
                        if (line3 == null) {
                            break;
                        }
                        String[] args = line3.split("\\t");
                        mods.put(args[0], new PatreonMod(args[0], args[1], args[2], args[3]));
                    }
                    reader.close();
                    loaded = true;
                } catch (Throwable th) {
                    loaded = true;
                    throw th;
                }
            }
        }
    }

    public static void addOutdatedMod(Object mod) {
        synchronized (getOutdatedMods()) {
            getOutdatedMods().add(mod);
        }
    }

    public static void saveSettings() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(optionsFile));
            writer.close();
        } catch (IOException e) {
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
    }

    public static void loadSettings() throws IOException {
        try {
            if (!optionsFile.exists()) {
                saveSettings();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    line.split(":");
                } else {
                    reader.close();
                    return;
                }
            }
        } catch (IOException e) {
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
    }

    public static ArrayList<Object> getOutdatedMods() {
        return outdatedMods;
    }

    public static boolean needsNotification() {
        return (notificationDisplayed || outdatedMods.isEmpty()) ? false : true;
    }

    public static String getPublicKeyString2() {
        return publicKeyString;
    }

    public static boolean isNotificationDisplayed() {
        return notificationDisplayed;
    }

    public static void setNotificationDisplayed(boolean notificationDisplayed2) {
        notificationDisplayed = notificationDisplayed2;
    }

    public static HashMap<String, Object> getMods() {
        return mods;
    }

    public static String getUpdateLocation() {
        return updateLocation;
    }

    public static int getKEY_VERSION2() {
        return KEY_VERSION;
    }

    public static boolean getHasAutoUpdates() {
        return hasAutoUpdates;
    }

    public static int getOnlineWidgetLevel() {
        return onlineWidgetLevel;
    }
}
