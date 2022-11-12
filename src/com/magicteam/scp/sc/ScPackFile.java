package com.magicteam.scp.sc;

import com.magicteam.scp.exceptions.ScFileInvalidException;
import com.magicteam.scp.sc.entries.ScPackEntry;
import com.magicteam.scp.sc.headers.ScPackHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScPackFile extends ScFile {
    private final List<ScPackEntry> entries;

    public ScPackFile(String path) throws IOException {
        super(path);

        this.entries = new ArrayList<>();
        this.scFileHeader = new ScPackHeader();
    }

    @Override
    public void unpack() throws IOException, ScFileInvalidException {
        super.unpack();

        this.readEntries();
        this.saveEntries();
    }

    private void readEntries() {
        ScPackHeader scPackHeader = (ScPackHeader) this.scFileHeader;

        this.stream.setOffset(scPackHeader.getEntriesOffset());

        for (int i = 0; i < scPackHeader.getEntriesCount(); i++) {
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
