package BioTeam.alignment.genome;

 // An interface for sequences
public interface Sequence {
     // Returns the length of the sequence
    public int getLength();

     // Returns the sequence
    public String getSequence();

    // Returns if the sequence contains the given fragment
    public boolean containsSubSequence(String pattern);
}
