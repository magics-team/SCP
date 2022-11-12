package com.magicteam.scp;

import com.magicteam.scp.exceptions.ScFileInvalidException;
import com.magicteam.scp.sc.ScPackFile;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ScFileInvalidException {
        if (args.length >= 1) {
            for (String file : args) {
                String path = "./resources/packs/" + file;
                if (!new File(path).exists()) {
                    System.out.printf("File %s not found%n", path);
                    continue;
                }

                ScPackFile scPackFile = new ScPackFile(path);
                scPackFile.unpack();
            }
            return;
        }

        System.out.println("Usage: executable.jar <files>");
    }
}