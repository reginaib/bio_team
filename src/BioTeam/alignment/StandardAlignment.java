package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.io.*;


public class StandardAlignment extends Alignment {
    public StandardAlignment(){
        super();
    }

    public StandardAlignment(Genome[] genomes) {
        super(genomes);
    }

    protected StandardAlignment(StandardAlignment alignment) {
        super(alignment);
    }

    @Override
    public StandardAlignment copy() {
        return new StandardAlignment(this);
    }

    public static StandardAlignment read(File file) throws IOException {
        StandardAlignment alignment = new StandardAlignment();

        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String header = bufferedReader.readLine();
                while (header != null) {
                    String seq = bufferedReader.readLine();
                    if (seq == null) break;
                    Genome genome = new Genome(header, seq);
                    alignment.addGenome(genome);
                    header = bufferedReader.readLine();
                }
            }
        }
        return alignment;
    }

    @Override
    public void write(File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Genome genome: this.genomes) {
                fileWriter.write(genome.getId());
                fileWriter.write('\n');
                fileWriter.write(genome.getSequence());
                fileWriter.write('\n');
            }
        }
    }

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
