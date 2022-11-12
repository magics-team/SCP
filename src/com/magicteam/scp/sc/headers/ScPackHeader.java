package com.magicteam.scp.sc.headers;

import com.magicteam.scp.streams.ByteStream;

public class ScPackHeader extends ScFileHeader {
    private byte[] hash;
    private int version, entriesCount, entriesOffset;
    private boolean isEncryptedPack;

    @Override
    public void read(ByteStream stream) {
        super.read(stream);

        this.version = stream.readInt();
        stream.readInt();
        this.entriesCount = stream.readInt();
        this.entriesOffset = stream.readInt();
        stream.skip(52);
        this.hash = stream.read(32);
        this.isEncryptedPack = stream.readBoolean();
    }

    @Override
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
