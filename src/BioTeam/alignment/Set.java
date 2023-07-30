package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

// An interface for handling operations related to sets of genomes
public interface Set {
    // A method for getting the numbers of genomes in the alignment
    public int getSize();
    // A method for retrieving a genome at a given index in the alignment
    public Genome getGenome(int idx);
    // A method to copy a set of genomes
    public Set copy();
}
