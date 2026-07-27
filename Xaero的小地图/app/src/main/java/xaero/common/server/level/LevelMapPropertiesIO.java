package xaero.common.server.level;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/level/LevelMapPropertiesIO.class */
public class LevelMapPropertiesIO {
    public static final String FILE_NAME = "xaeromap.txt";

    public void load(Path file, LevelMapProperties dest) throws IOException {
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), "UTF8"));
                dest.read(reader);
                if (reader != null) {
                    reader.close();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } catch (Throwable th) {
            if (reader != null) {
                reader.close();
            }
            throw th;
        }
    }

    public void save(Path file, LevelMapProperties dest) throws IOException {
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(file.toFile()));
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8));
            try {
                dest.write(writer);
                writer.close();
                bufferedOutput.close();
            } finally {
            }
        } catch (Throwable th) {
            try {
                bufferedOutput.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
