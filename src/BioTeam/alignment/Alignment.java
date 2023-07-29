package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

abstract class Alignment implements Set, Editable, IO {
    protected List<Genome> genomes;
    protected HashMap<String, Integer> genomeIds;

    public Alignment() {
        this.genomes = new ArrayList<>();
        this.genomeIds = new HashMap<>();
    }

    public Alignment(Genome[] genomes) {
        HashMap<String, Integer> found = new HashMap<>();
        int i = 0;
        for (Genome genome: genomes) {
            if (found.containsKey(genome.getId())) {
                throw new IllegalArgumentException("duplicated genome ids found");
            }
            found.put(genome.getId(), i++);
        }

        this.genomes = Arrays.asList(genomes);
        this.genomeIds = found;
    }

    protected Alignment(Alignment alignment) {
        this.genomeIds = (HashMap<String, Integer>) alignment.genomeIds.clone();

        List<Genome> genomes = new ArrayList<>();
        for (Genome genome: alignment.genomes) {
            genomes.add(genome.copy());
        }
        this.genomes = genomes;
    }

    @Override
    public int getSize() {
        return this.genomes.size();
    }

    @Override
    public Genome getGenome(int idx) {
        return this.genomes.get(idx);
    }

    @Override
    public void addGenome(Genome genome) {
        if (this.genomeIds.containsKey(genome.getId())) {
            throw new IllegalArgumentException("Genome with the same id already in alignment");
        }
        this.genomeIds.put(genome.getId(), this.genomes.size());
        this.genomes.add(genome);
    }

    @Override
    public void deleteGenome(int idx) {
        Genome genome = this.genomes.get(idx);
        this.genomeIds.remove(genome.getId());
        this.genomes.remove(idx);
    }

    @Override
    public void replaceGenome(int idx, Genome genome) {
        Genome oldGenome = this.genomes.get(idx);
        if (this.genomeIds.containsKey(genome.getId()) & !oldGenome.getId().equals(genome.getId())) {
            throw new IllegalArgumentException("Genome with the same id already in Alignment");
        }
        this.genomeIds.remove(oldGenome.getId());
        this.genomeIds.put(genome.getId(), idx);
        this.genomes.set(idx, genome);
    }

    @Override
    public boolean replaceGenomeSubSequence(String pattern, String replacement) {
        boolean found = false;
        for (Genome genome: this.genomes) {
            if (genome.replaceSubSequence(pattern, replacement) && !found) {
                found = true;
            }
        }
        return found;
    }

    public int[] containsSubSequence(String pattern) {
        List<Integer> found = new ArrayList<>();
        int i = 0;
        for (Genome genome: this.genomes) {
            if (genome.containsSubSequence(pattern)) {
                found.add(i);
            }
            i++;
        }

        int[] output = new int[found.size()];
        i = 0;
        for (Integer e : found)
            output[i++] = e;
        return output;
    }

    public int getScore() {
        int score = 0;
        String first = this.genomes.get(0).getSequence();

        for (Genome genome: this.genomes) {
            String current = genome.getSequence();
            int length = Math.min(first.length(), current.length());
            for (int i = 0; i < length; i++) {
                if (first.charAt(i) != current.charAt(i)) {
                    score++;
                }
            }
            score += Math.abs(first.length() - current.length());
        }
        return score;
    }
}
