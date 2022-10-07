package com.magicteam.scp.sc;

import com.magicteam.scp.streams.ByteStream;

public class ScPackEntry {
    private byte[] hash;
    private long fileSize, fileOffset;
    private String fileName;

    public void read(ByteStream stream) {
        stream.readShort();
        stream.readShort();

        stream.readLong();
        this.fileOffset = stream.readLong();
        this.fileSize = stream.readLong();
        this.hash = stream.read(32);
        this.fileName = stream.readString();
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getHash() {
        return hash;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getFileOffset() {
        return fileOffset;
    }
}
