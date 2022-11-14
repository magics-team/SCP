package com.magicteam.scp.sc.scp;

import com.magicteam.scp.exceptions.ScFilePackInvalidException;
import com.magicteam.scp.sc.ScFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScPackFile extends ScFile {
    private final List<ScPackEntry> entries;
    private final ScPackHeader scPackHeader;

    public ScPackFile(String path) throws IOException {
        super(path);

        this.entries = new ArrayList<>();
        this.scPackHeader = new ScPackHeader();
    }

    public void unpack() throws IOException, ScFilePackInvalidException {
        this.scPackHeader.read(this.stream);
        this.checkValidity();

        this.readEntries();
        this.saveEntries();
    }

    private void checkValidity() throws ScFilePackInvalidException {
        if (this.scPackHeader.isEncryptedPack()) throw new ScFilePackInvalidException("Encrypted packs not supported.");
        if (this.scPackHeader.getVersion() != 1) throw new ScFilePackInvalidException(String.format("Version %s not supported.", scPackHeader.getVersion()));
        if (this.scPackHeader.getMagicNumbers() != 558908243) throw new ScFilePackInvalidException("Invalid magic numbers.");
    }

    private void readEntries() {
        this.stream.setOffset(this.scPackHeader.getEntriesOffset());

        for (int i = 0; i < this.scPackHeader.getEntriesCount(); i++) {
            ScPackEntry scPackEntry = new ScPackEntry();
            scPackEntry.read(this.stream);

            this.entries.add(scPackEntry);
        }
    }

    private void saveEntries() throws IOException {
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
}
