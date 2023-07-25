package BioTeam;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Alignment {
    private final List<Genome> genomes;
    private final HashMap<String, Integer> genomeIds;  // genomes index
    private int genomeLength;

    public Alignment() {
        this.genomes = new ArrayList<>();
        this.genomeIds = new HashMap<>();
    }

    public Alignment(Genome @NotNull [] genomes) {
        HashMap<String, Integer> found = new HashMap<>();

        if (genomes.length > 0){
            Genome genome = genomes[0];
            this.genomeLength = genome.getLength();
            found.put(genome.getId(), 0);

            for (int i = 1; i < genomes.length; i++) {
                genome = genomes[i];
                if (found.containsKey(genome.getId())){
                    throw new DuplicateId();
                }
                if (genome.getLength() != this.genomeLength) {
                    throw new SequenceLengthMismatch();
                }
                found.put(genome.getId(), i);
            }
            this.genomes = Arrays.asList(genomes);
        }
        else {
            this.genomes = new ArrayList<>();
        }
        this.genomeIds = found;
    }

    public static Alignment readFASTA(FileReader file) throws IOException {
        BufferedReader reader = new BufferedReader(file);
        Alignment alignment = new Alignment();

        String header = reader.readLine();
        while (header != null) {
            String seq = reader.readLine();
            Genome genome = new Genome(header.substring(1), seq);
            alignment.add(genome);
            header = reader.readLine();
        }
        reader.close();
        return alignment;
    }

    public static Alignment readSNIP(FileReader file) throws IOException {
        BufferedReader reader = new BufferedReader(file);
        String header = reader.readLine();
        String seq = reader.readLine();
        Genome genome = new Genome(header.substring(1), seq);

        Alignment alignment = new Alignment();
        alignment.add(genome);

        header = reader.readLine();
        while (header != null) {
            seq = reader.readLine();
            alignment.add(genome.fromSNIP(header, seq));
            header = reader.readLine();
        }
        reader.close();
        return alignment;
    }

    private Alignment(Alignment alignment) {
        this.genomeIds = (HashMap<String, Integer>) alignment.genomeIds.clone();
        this.genomeLength = alignment.genomeLength;
        List<Genome> genomes = new ArrayList<>();
        for (Genome genome: alignment.genomes) {
            genomes.add(genome.copy());
        }
        this.genomes = genomes;
    }

    public Alignment copy() {
        return new Alignment(this);
    }

    public int getSize() {  // number of genomes
        return this.genomes.size();
    }

    public int getLength() {  // length of genomes
        return this.genomeLength;
    }

    public int getScore() throws EmptyAlignment {
        if (this.genomes.isEmpty()) {
            throw new EmptyAlignment();
        }
        int score = 0;
        String first = this.genomes.get(0).getSequence();
        for (int i = 1; i < this.genomes.size(); i++) {
            String next = this.genomes.get(i).getSequence();
            for (int j = 0; j < first.length(); j ++) {
                if (first.charAt(j) != next.charAt(j)) {
                    score++;
                }
            }
        }
        return score;
    }

    public void writeFASTA(FileWriter file) throws IOException, EmptyAlignment {
        if (this.genomes.isEmpty()) {
            throw new EmptyAlignment();
        }
        for (Genome genome: this.genomes) {
            file.write(genome.getFASTA());
        }
    }

    public void writeFASTA(FileWriter file, String meta) throws IOException, EmptyAlignment {
        if (this.genomes.isEmpty()) {
            throw new EmptyAlignment();
        }
        for (Genome genome: this.genomes) {
            file.write(genome.getFASTA(meta));
        }
    }

    public void writeSNIP(FileWriter file) throws IOException, EmptyAlignment {
        if (this.genomes.isEmpty()) {
            throw new EmptyAlignment();
        }
        Genome first = this.genomes.get(0);
        file.write(first.getFASTA());

        for (int i = 1; i < this.genomes.size(); i++) {
            Genome next = this.genomes.get(i);
            file.write(next.getSNIP(first));
        }
    }

    public void writeSNIP(FileWriter file, String meta) throws IOException, EmptyAlignment {
        if (this.genomes.isEmpty()) {
            throw new EmptyAlignment();
        }
        Genome first = this.genomes.get(0);
        file.write(first.getFASTA(meta));

        for (int i = 1; i < this.genomes.size(); i++) {
            Genome next = this.genomes.get(i);
            file.write(next.getSNIP(first, meta));
        }
    }

    public String[] listIds() {
        return this.genomeIds.keySet().toArray(new String[0]);
    }

    public Genome get(@NotNull String id) {
        if (!this.genomeIds.containsKey(id)) {
            throw new InvalidId();
        }
        return this.genomes.get(this.genomeIds.get(id));
    }

    public Genome get(int id) {
        return this.genomes.get(id);
    }

    public void add(@NotNull Genome genome) {
        if (this.genomeIds.containsKey(genome.getId())) {
            throw new DuplicateId();
        }
        else if (this.genomeLength == 0) {  // empty Alignment
            this.genomeLength = genome.getLength();
        }
        else if (genome.getLength() != this.genomeLength) {
            throw new SequenceLengthMismatch();
        }
        this.genomeIds.put(genome.getId(), this.genomes.size());  // get size first
        this.genomes.add(genome);
    }

    public void delete(@NotNull String id) {
        if (!this.genomeIds.containsKey(id)) {
            throw new InvalidId();
        }
        int i = this.genomeIds.get(id);
        this.genomeIds.remove(id);
        this.genomes.remove(i);
    }

    public void replace(@NotNull Genome genome) {
        if (!this.genomeIds.containsKey(genome.getId())) {
            throw new InvalidId();
        }
        else if (genome.getLength() != this.genomeLength) {
            throw new SequenceLengthMismatch();
        }
        int i = this.genomeIds.get(genome.getId());
        this.genomes.set(i, genome);
    }

    public void replace(@NotNull String id, @NotNull Genome genome) {
        if (!this.genomeIds.containsKey(id)) {
            throw new InvalidId();
        }
        else if (genome.getLength() != this.genomeLength) {
            throw new SequenceLengthMismatch();
        }
        else if (id.equals(genome.getId())) {  // replaceSubSequence sequence, but keep id
            int i = this.genomeIds.get(id);
            this.genomes.set(i, genome);
        }
        else if (this.genomeIds.containsKey(genome.getId())) {
            throw new DuplicateId();
        }
        else {
            int i = this.genomeIds.get(id);
            this.genomes.set(i, genome);
            this.genomeIds.remove(id);
            this.genomeIds.put(genome.getId(), i);
        }
    }

    public String[] containsSubSequence(String sequence) {
        List<String> found = new ArrayList<>();
        for (Genome genome: this.genomes) {
            if (genome.containsSubSequence(sequence)) {
                found.add(genome.getId());
            }
        }
        return found.toArray(new String[0]);
    }

    public boolean replaceSubSequence(String sequence, String target) {
        boolean found = false;
        for (Genome genome: this.genomes) {
            if (genome.replaceSubSequence(sequence, target) && !found) {
                found = true;
            }
        }
        return found;
    }
}
