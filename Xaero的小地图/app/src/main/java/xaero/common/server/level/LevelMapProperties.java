package xaero.common.server.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/level/LevelMapProperties.class */
public class LevelMapProperties {
    private int id = new Random().nextInt();
    private boolean usable = true;

    public void write(PrintWriter writer) {
        writer.print("id:" + this.id);
    }

    public void read(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                String[] args = line.split(":");
                if (args[0].equals("id")) {
                    try {
                        this.id = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                    }
                }
            } else {
                return;
            }
        }
    }

    public boolean isUsable() {
        return this.usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public int getId() {
        return this.id;
    }

    public static LevelMapProperties read(FriendlyByteBuf input) {
        LevelMapProperties result = new LevelMapProperties();
        result.id = input.readInt();
        return result;
    }

    public void write(FriendlyByteBuf u) {
        u.writeInt(this.id);
    }
}
