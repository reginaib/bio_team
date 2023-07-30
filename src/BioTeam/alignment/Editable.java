package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

// An interface for handling operations related to editing alignments
public interface Editable {
    // A method for adding a genome to the alignment
    void addGenome(Genome genome);
    // A method for deleting a genome from the alignment
    void deleteGenome(int idx);
    // A method for replacing a genome at a given index with a new genome
    void replaceGenome(int idx, Genome genome);
    // A method that returns if any genome's substring was replaced
    public boolean replaceGenomeSubSequence(String pattern, String replacement);
}
