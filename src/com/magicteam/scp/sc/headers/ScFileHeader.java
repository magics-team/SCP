package com.magicteam.scp.sc.headers;

import com.magicteam.scp.streams.ByteStream;

public abstract class ScFileHeader {
    protected int magicNumbers;

    public void read(ByteStream stream) {
        this.magicNumbers = stream.readInt();
    }

    public abstract boolean isValid();
}
