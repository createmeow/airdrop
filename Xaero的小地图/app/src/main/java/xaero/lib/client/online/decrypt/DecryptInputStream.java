package xaero.lib.client.online.decrypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import javax.crypto.Cipher;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/online/decrypt/DecryptInputStream.class */
public class DecryptInputStream extends InputStream {
    private InputStream src;
    private Cipher cipher;
    private byte[] currentBlock;
    private int blockCount;
    private int blockOffset;
    private boolean endReached;
    private byte[] encryptedBuffer = new byte[256];
    private long prevExpirationTime = -1;

    public DecryptInputStream(InputStream src, Cipher cipher) {
        this.src = src;
        this.cipher = cipher;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.endReached) {
            return -1;
        }
        if (this.currentBlock == null || this.currentBlock.length == this.blockOffset) {
            int i = 0;
            while (true) {
                int offset = i;
                if (offset < 256) {
                    int read = this.src.read(this.encryptedBuffer, offset, 256 - offset);
                    if (read == -1) {
                        this.endReached = true;
                        if (offset == 0) {
                            throw new IOException("Online mod data missing confirmation block!");
                        }
                        throw new IOException("Encrypted block too short!");
                    }
                    i = offset + read;
                } else {
                    try {
                        this.currentBlock = this.cipher.doFinal(this.encryptedBuffer);
                        long expirationTime = 0;
                        int blockIndex = 0;
                        this.blockOffset = 0;
                        while (this.blockOffset < 8) {
                            expirationTime |= (this.currentBlock[this.blockOffset] & 255) << (8 * this.blockOffset);
                            this.blockOffset++;
                        }
                        for (int i2 = 0; i2 < 2; i2++) {
                            blockIndex |= (this.currentBlock[this.blockOffset] & 255) << (8 * i2);
                            this.blockOffset++;
                        }
                        if (System.currentTimeMillis() > expirationTime) {
                            this.endReached = true;
                            throw new IOException("Online mod data expired! Date: " + String.valueOf(new Date(expirationTime)));
                        }
                        if (this.prevExpirationTime != -1 && expirationTime != this.prevExpirationTime) {
                            this.endReached = true;
                            throw new IOException("Online mod data expiration date mismatch! Dates: " + String.valueOf(new Date(expirationTime)) + " VS " + String.valueOf(new Date(this.prevExpirationTime)));
                        }
                        if (blockIndex != this.blockCount) {
                            this.endReached = true;
                            throw new IOException("Online mod data block index mismatch! " + blockIndex + " VS " + this.blockCount);
                        }
                        this.prevExpirationTime = expirationTime;
                        this.blockCount++;
                        if (this.blockOffset == this.currentBlock.length) {
                            this.endReached = true;
                            return -1;
                        }
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        byte[] bArr = this.currentBlock;
        int i3 = this.blockOffset;
        this.blockOffset = i3 + 1;
        return bArr[i3];
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.src.close();
        this.encryptedBuffer = null;
        this.currentBlock = null;
    }
}
