package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.io.*;

// The SNPAlignment extends the Alignment class and represents a SNP (Single Nucleotide Polymorphisms) alignment
public class SNPAlignment extends Alignment {
    // A constructor for creating an empty SNPAlignment
    public SNPAlignment(){
        super();
    }

    // A constructor for creating a SNPAlignment with an array of genomes
    public SNPAlignment(Genome[] genomes) {
        super(genomes);
    }

    // A constructor for creating a SNPAlignment based on an existing SNPAlignment
    protected SNPAlignment(Alignment alignment) {
        super(alignment);
    }

    // Overridden method from the superclass to return a copy of the SNPAlignment
    @Override
    public SNPAlignment copy() {
        return new SNPAlignment(this);
    }

    public StandardAlignment getStandardAlignment() {
        return new StandardAlignment(this);
    }

    // A method to read a SNPAlignment from a file and return the created alignment
    // Throws an 'IOException' if there are any issues while reading from the file
    public static SNPAlignment read(File file) throws IOException {
        SNPAlignment alignment = new SNPAlignment(); // Creating a new instance of SNPAlignment

        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String header = bufferedReader.readLine();
                if (header != null) {
                    String seq = bufferedReader.readLine();
                    if (seq != null) {
                        Genome first = new Genome(header, seq);
                        alignment.addGenome(first); // Adding the first genome to the alignment

                        header = bufferedReader.readLine();
                        while (header != null) {
                            seq = bufferedReader.readLine(); // sequence with dots
                            if (seq == null) break;
                            Genome genome = new Genome(header, first.getDifference(seq)); // Creating a new Genome with the header and SNP differences
                            alignment.addGenome(genome);
                            header = bufferedReader.readLine();
                        }
                    }
                }
            }
        }
        return alignment;
    }

    // An overridden method for writing the SNPAlignment data to a file
    // Throws an 'IOException' if there are any issues while writing to the file
    @Override
    public void write(File file) throws IOException {
        this.write(file, false);
    }

    // An overridden method for writing the SNPAlignment data with meta information to a file
    // Throws an 'IOException' if there are any issues while writing to the fil
    @Override
    public void write(File file, String meta) throws IOException {
        this.write(file, meta, false);
    }

    @Override
    public void write(File file, boolean append) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file, append)) {
            Genome first = this.genomes.get(0);
            fileWriter.write(first.getId());
            fileWriter.write('\n');
            fileWriter.write(first.getSequence());
            fileWriter.write('\n');

            for (int i = 1; i < this.genomes.size(); i++) {
                Genome current = this.genomes.get(i);
                fileWriter.write(current.getId());
                fileWriter.write('\n');
                fileWriter.write(first.getDifference(current)); // write dotted sequence
                fileWriter.write('\n');
            }
        }
    }

    @Override
    public void write(File file, String meta, boolean append) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file, append)) {
            Genome first = this.genomes.get(0);
            fileWriter.write(first.getId());
            fileWriter.write(meta);
            fileWriter.write('\n');
            fileWriter.write(first.getSequence());
            fileWriter.write('\n');

            for (int i = 1; i < this.genomes.size(); i++) {
                Genome current = this.genomes.get(i);
                fileWriter.write(current.getId());
                fileWriter.write(meta);
                fileWriter.write('\n');
                fileWriter.write(first.getDifference(current));
                fileWriter.write('\n');
            }
        }
    }
}
