package BioTeam.alignment.genome;

// An interface for editing genomes
public interface Editable {
    // Returns if the subsequence was replaced
    public boolean replaceSubSequence(String pattern, String replacement);
}
