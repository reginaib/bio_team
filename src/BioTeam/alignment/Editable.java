package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

/**
 * An interface for editing alignments.
 */
public interface Editable {
    void addGenome(Genome genome);

    void deleteGenome(int idx);

    void replaceGenome(int idx, Genome genome);

    public boolean replaceGenomeSubSequence(String pattern, String replacement);
}
