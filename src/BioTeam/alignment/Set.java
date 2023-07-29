package BioTeam.alignment;

import BioTeam.alignment.genome.Genome;

public interface Set {
    public int getSize();

    public Genome getGenome(int idx);

    public Set copy();
}
