package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.io.*;

public class SNPAlignment extends Alignment {
    public SNPAlignment(){
        super();
    }

    public SNPAlignment(Genome[] genomes) {
        super(genomes);
    }

    protected SNPAlignment(SNPAlignment alignment) {
        super(alignment);
    }

    @Override
    public SNPAlignment copy() {
        return new SNPAlignment(this);
    }

    public static SNPAlignment read(File file) throws IOException {
        SNPAlignment alignment = new SNPAlignment();

        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String header = bufferedReader.readLine();
                if (header != null) {
                    String seq = bufferedReader.readLine();
                    if (seq != null) {
                        Genome first = new Genome(header, seq);
                        alignment.addGenome(first);

                        header = bufferedReader.readLine();
                        while (header != null) {
                            seq = bufferedReader.readLine();
                            if (seq == null) break;
                            Genome genome = new Genome(header, first.getDifference(seq));
                            alignment.addGenome(genome);
                            header = bufferedReader.readLine();
                        }
                    }
                }
            }
        }
        return alignment;
    }

    @Override
    public void write(File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            Genome first = this.genomes.get(0);
            fileWriter.write(first.getId());
            fileWriter.write('\n');
            fileWriter.write(first.getSequence());
            fileWriter.write('\n');

            for (int i = 1; i < this.genomes.size(); i++) {
                Genome current = this.genomes.get(i);
                fileWriter.write(current.getId());
                fileWriter.write('\n');
                fileWriter.write(first.getDifference(current));
                fileWriter.write('\n');
            }
        }
    }

    @Override
    public void write(File file, String meta) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
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
