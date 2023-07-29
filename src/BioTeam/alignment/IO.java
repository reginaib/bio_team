package BioTeam.alignment;

import java.io.File;
import java.io.IOException;

public interface IO {
    public static IO read(File file) throws IOException {
        return null;
    }

    public void write(File file) throws IOException;
    public void write(File file, String meta) throws IOException;
}
