package ana.io.file;


// dep

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// main

public class FileExt {
    // no inst

    private FileExt() {}

    // util

    public static String dirStrip(String dirStr) {
        return dirStr.replaceAll("(^/)|(/$)", "");
    }

    public static String[] listDir(String dirStr) {
        dirStr = dirStrip(dirStr);
        File dir = new File(dirStr);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            ArrayList<String> filenames = new ArrayList<>();

            if (files != null) {
                for (File f : files)
                    if (f.isFile()) filenames.add(f.getName());
                return filenames.toArray(new String[] {});
            }
        }

        return new String[] {};
    }

    public static String[] readLines(String path) throws IOException {
        try (
            FileReader fr = new FileReader(path);
            Scanner fs = new Scanner(fr)
        ) {
            List<String> lines = new ArrayList<>();
            while (fs.hasNext()) lines.add(fs.nextLine());
            return lines.toArray(new String[0]);
        }
    }
}
