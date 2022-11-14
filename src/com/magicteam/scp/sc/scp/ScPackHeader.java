package com.magicteam.scp.sc.scp;

import com.magicteam.scp.streams.ByteStream;

public class ScPackHeader {
    private byte[] hash;
    private int version, entriesCount, entriesOffset, magicNumbers;
    private boolean isEncryptedPack;

    public void read(ByteStream stream) {
        this.magicNumbers = stream.readInt();
        this.version = stream.readInt();
        stream.readInt();
        this.entriesCount = stream.readInt();
        this.entriesOffset = stream.readInt();
        stream.skip(52);
        this.hash = stream.read(32);
        this.isEncryptedPack = stream.readBoolean();
    }

    public boolean isEncryptedPack() {
        return isEncryptedPack;
    }

    public int getMagicNumbers() {
        return magicNumbers;
    }

    public int getVersion() {
        return version;
    }

    public boolean isValid() {
        if (this.isEncryptedPack) return false;
        if (this.version != 1) return false;
        return this.magicNumbers == 558908243;
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public int getEntriesOffset() {
        return entriesOffset;
    }
}
