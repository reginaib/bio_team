package BioTeam.alignment.genome;

public interface Sequence {
    // return the length of sequence
    public int getLength();

    public String getSequence();

    public boolean containsSubSequence(String pattern);
}
