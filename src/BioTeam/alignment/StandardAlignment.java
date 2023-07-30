package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.io.*;

// The StandardAlignment extends the Alignment class and represents a standard genome alignment
public class StandardAlignment extends Alignment {
    // A constructor for creating an empty StandardAlignment
    public StandardAlignment(){
        super(); // calls the constructor of the superclass (Alignment)
    }

    // A constructor for creating a StandardAlignment with an array of genomes
    public StandardAlignment(Genome[] genomes) {
        super(genomes); // Calls the constructor of the superclass for creating an alignment with a given array of genomes
    }

    // A constructor for creating a StandardAlignment based on an existing StandardAlignment
    protected StandardAlignment(StandardAlignment alignment) {
        super(alignment); // Calls the constructor of the superclass
    }

    // Overridden method from the superclass to return a copy of the StandardAlignment
    @Override
    public StandardAlignment copy() {
        return new StandardAlignment(this);
    }

    // A method to read a StandardAlignment from a file and return the created alignment
    // Throws an 'IOException' if there are any issues while reading from the file
    public static StandardAlignment read(File file) throws IOException {
        StandardAlignment alignment = new StandardAlignment(); // Creating a new instance of StandardAlignment

        try (FileReader fileReader = new FileReader(file)) { // Open a FileReader to read from the file
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) { // Open a BufferedReader for efficient reading
                String header = bufferedReader.readLine(); // Reading the first line of the file (the header)
                while (header != null) {
                    String seq = bufferedReader.readLine(); // Reading the next line (the sequence)
                    if (seq == null) break; // if there is no sequence, exit
                    Genome genome = new Genome(header, seq); // Creating a new Genome
                    alignment.addGenome(genome); // Adding it to the alignment
                    header = bufferedReader.readLine(); // Reading the next header or EOF
                }
            }
        }
        return alignment; // Returning the obtained StandardAlignment
    }

    // An overridden method for writing the StandardAlignment data to a file
    // Throws an 'IOException' if there are any issues while writing to the file
    @Override
    public void write(File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) { // try (...) {} ensure the proper file closing
            for (Genome genome: this.genomes) {
                fileWriter.write(genome.getId());
                fileWriter.write('\n'); // new line character to separate rows
                fileWriter.write(genome.getSequence());
                fileWriter.write('\n');
            }
        }
    }

    // An overridden method for writing the StandardAlignment data with meta information to a file
    // Throws an 'IOException' if there are any issues while writing to the file
    @Override
    public void write(File file, String meta) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Genome genome: this.genomes) {
                fileWriter.write(genome.getId());
                fileWriter.write(meta);
                fileWriter.write('\n');
                fileWriter.write(genome.getSequence());
                fileWriter.write('\n');
            }
        }
    }
}
