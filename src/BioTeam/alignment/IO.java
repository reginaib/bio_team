package BioTeam.alignment;

import java.io.File;
import java.io.IOException;

// An interface for handling file input and output related to alignments
public interface IO {
    // A method that reads alignment data from a file and returns an Alignment object
    // Throws an 'IOException' if there are any issues while reading from the file
    public static IO read(File file) throws IOException {
        return null;
    }
    // A method for writing alignment data to a file
    // Throws an 'IOException' if there are any issues while writing to the file
    public void write(File file) throws IOException;

    // A method for writing the alignment along with meta information to a file
    // Throws an 'IOException' if there are any issues while writing to the file
    public void write(File file, String meta) throws IOException;
}
