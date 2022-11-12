package com.magicteam.scp.sc;

import com.magicteam.scp.exceptions.ScFileInvalidException;
import com.magicteam.scp.sc.headers.ScFileHeader;
import com.magicteam.scp.streams.ByteStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ScFile {
    protected ByteStream stream;
    protected String path;
    protected ScFileHeader scFileHeader;

    public ScFile(String path) throws IOException {
        this.stream = new ByteStream(Files.readAllBytes(Path.of(path)));
        this.path = path;
    }

    public void checkValidity() throws ScFileInvalidException {
        if (!this.scFileHeader.isValid()) throw new ScFileInvalidException("The file is not supported!");
    }

    public void readHeader() {
        this.scFileHeader.read(this.stream);
    }

    public abstract void unpack() throws ScFileInvalidException, IOException;
}
