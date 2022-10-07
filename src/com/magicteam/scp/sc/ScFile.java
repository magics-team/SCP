package com.magicteam.scp.sc;

import com.magicteam.scp.streams.ByteStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScFile {
    protected ByteStream stream;
    protected String path;

    public ScFile(String path) throws IOException {
        this.stream = new ByteStream(Files.readAllBytes(Path.of(path)));
        this.path = path;
    }
}
