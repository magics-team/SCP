package com.magicteam.scp.sc;

import com.magicteam.scp.exceptions.ScFilePackInvalidException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScPackFile extends ScFile {
    private final ScPackHeader scPackHeader;
    private final List<ScPackEntry> entries;

    public ScPackFile(String path) throws IOException {
        super(path);

        this.entries = new ArrayList<>();
        this.scPackHeader = new ScPackHeader();
    }

    public ScPackEntry readEntry() {
        ScPackEntry scPackEntry = new ScPackEntry();
        scPackEntry.read(this.stream);

        return scPackEntry;
    }

    public void readEntries() {
        this.stream.setOffset(this.scPackHeader.getEntriesOffset());

        for (int i = 0; i < this.scPackHeader.getEntriesCount(); i++) {
            entries.add(this.readEntry());
        }
    }

    public void readHeader() {
        this.scPackHeader.read(this.stream);
    }

    public void saveEntries() throws IOException {
        String[] s = this.path.split(".scp")[0].split("/");
        String directory = s[s.length - 1];

        for (ScPackEntry scPackEntry : this.entries) {
            this.stream.setOffset((int) scPackEntry.getFileOffset());

            File directoryFile = new File(String.format("./resources/packs/%s", directory));
            directoryFile.mkdirs();

            FileOutputStream out = new FileOutputStream(String.format("./resources/packs/%s/%s", directory, scPackEntry.getFileName()));
            out.write(this.stream.read((int) scPackEntry.getFileSize()));
            out.close();

            System.out.printf("Successfully saved file: %s/%s%n", directory, scPackEntry.getFileName());
        }
    }

    public ScPackHeader getScPackHeader() {
        return scPackHeader;
    }

    public static void unpack(String path) throws IOException, ScFilePackInvalidException {
        ScPackFile scPackFile = new ScPackFile(path);
        scPackFile.readHeader();

        ScPackHeader scPackHeader = scPackFile.getScPackHeader();

        if (scPackHeader.isEncryptedPack()) throw new ScFilePackInvalidException("Encrypted packs not supported.");
        if (scPackHeader.getVersion() != 1) throw new ScFilePackInvalidException(String.format("Version %s not supported.", scPackHeader.getVersion()));
        if (scPackHeader.getMagicNumbers() != 558908243) throw new ScFilePackInvalidException("Invalid magic numbers.");

        scPackFile.readEntries();
        scPackFile.saveEntries();
    }
}
