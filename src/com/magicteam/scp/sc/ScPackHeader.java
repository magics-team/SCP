package com.magicteam.scp.sc;

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

    public int getMagicNumbers() {
        return magicNumbers;
    }

    public int getVersion() {
        return version;
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public boolean isEncryptedPack() {
        return isEncryptedPack;
    }

    public byte[] getHash() {
        return hash;
    }

    public int getEntriesOffset() {
        return entriesOffset;
    }
}
