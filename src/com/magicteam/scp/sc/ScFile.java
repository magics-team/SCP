package com.magicteam.scp.sc;

import com.magicteam.scp.exceptions.ScFileInvalidException;
import com.magicteam.scp.sc.headers.ScFileHeader;
import com.magicteam.scp.streams.ByteStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScFile {
    protected ByteStream stream;
    protected String path;
    protected ScFileHeader scFileHeader;

    public ScFile(String path) throws IOException {
        this.stream = new ByteStream(Files.readAllBytes(Path.of(path)));
        this.path = path;
    }

    private void checkValidity() throws ScFileInvalidException {
        if (!this.scFileHeader.isValid()) throw new ScFileInvalidException("The file is not supported!");
    }

    private void readHeader() {
        this.scFileHeader.read(this.stream);
    }

    public void unpack() throws ScFileInvalidException, IOException {
        this.readHeader();
        this.checkValidity();
    }
}
