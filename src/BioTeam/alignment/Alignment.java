package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// An abstract class that provides basic functionality for alignments.
abstract class Alignment implements Set, Editable, IO {
    // A list to store the genomes of the alignment
    protected List<Genome> genomes; // protected in order to be accessed by subclasses
    // A HashMap to store the mapping of genome ids to their indices in the list above.
    protected HashMap<String, Integer> genomeIds;

    // A constructor for creating an empty alignment
    public Alignment() {
        this.genomes = new ArrayList<>();
        this.genomeIds = new HashMap<>();
    }

    // A constructor for creating an alignment with a given array of genomes
    public Alignment(Genome[] genomes) {
        // A HashMap to keep track of genome ids and their indices
        HashMap<String, Integer> found = new HashMap<>();
        int i = 0;
        for (Genome genome: genomes) {
            if (found.containsKey(genome.getId())) {
                throw new IllegalArgumentException("duplicated genome ids found");
            }
            found.put(genome.getId(), i++);
        }

        // Convert the array of genomes to a list
        this.genomes = Arrays.asList(genomes);
        // Storing the ids mapping in 'genomeIds'
        this.genomeIds = found;
    }

    // A constructor for creating an alignment copy
    protected Alignment(Alignment alignment) {
        // Creating a shallow copy
        this.genomeIds = (HashMap<String, Integer>) alignment.genomeIds.clone();
        // Creating a deep copy
        List<Genome> genomes = new ArrayList<>();
        for (Genome genome: alignment.genomes) {
            genomes.add(genome.copy());
        }
        this.genomes = genomes;
    }

    // Overriding the methods of the interfaces; getters to maintain encapsulation
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
        // First checking for genome id duplication
        if (this.genomeIds.containsKey(genome.getId())) {
            throw new IllegalArgumentException("Genome with the same id already in alignment");
        }
        // Updating the genome id mapping
        this.genomeIds.put(genome.getId(), this.genomes.size());
        // Adding the genome to the list
        this.genomes.add(genome);
    }

    @Override
    public void deleteGenome(int idx) {
        // Get the genome to be deleted
        Genome genome = this.genomes.get(idx);
        // Removing the genome id from the genomeIds mapping
        this.genomeIds.remove(genome.getId());
        // Removing the genome from the genomes list
        this.genomes.remove(idx);
    }

    @Override
    public void replaceGenome(int idx, Genome genome) {
        // Retrieve the old genome at the given index
        Genome oldGenome = this.genomes.get(idx);
        // Checking for the genome id duplication
        if (this.genomeIds.containsKey(genome.getId()) & !oldGenome.getId().equals(genome.getId())) {
            throw new IllegalArgumentException("Genome with the same id already in Alignment");
        }
        // Updating the genomeIds mapping with the new genome id
        this.genomeIds.remove(oldGenome.getId());
        this.genomeIds.put(genome.getId(), idx);
        // Replacing the genome in genomes
        this.genomes.set(idx, genome);
    }

    @Override
    public boolean replaceGenomeSubSequence(String pattern, String replacement) {
        boolean found = false;
        // Looking for the subsequence in all genomes and replace if found
        for (Genome genome: this.genomes) {
            if (genome.replaceSubSequence(pattern, replacement) && !found) {
                found = true;
            }
        }
        return found;
    }

    // A method for checking if a given subsequence is present in any of the genomes
    public int[] containsSubSequence(String pattern) {
        List<Integer> found = new ArrayList<>();
        int i = 0;
        // Looking for the subsequence in all genomes
        for (Genome genome: this.genomes) {
            if (genome.containsSubSequence(pattern)) {
                found.add(i);
            }
            i++;
        }
        // A list of indices is converted to an array and returned
        int[] output = new int[found.size()];
        i = 0;
        for (Integer e : found)
            output[i++] = e;
        return output;
    }

    // A method for calculating the alignment score based in differences in sequences among genomes
    public int getScore() {
        int score = 0;
        String first = this.genomes.get(0).getSequence();

        // Comparing the first genome's sequence with others
        for (int i = 1; i < this.genomes.size(); i++) {
            String current = this.genomes.get(i).getSequence();

            int length = Math.min(first.length(), current.length());
            for (int j = 0; j < length; j++) {
                if (first.charAt(j) != current.charAt(j)) {
                    score++;
                }
            }
            // Calculating the score
            score += Math.abs(first.length() - current.length());
        }
        return score;
    }
}
