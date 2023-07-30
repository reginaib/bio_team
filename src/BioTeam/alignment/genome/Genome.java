package BioTeam.alignment.genome;

public class Genome implements Sequence, Editable {
    private final String id; // The variables are private, they could be accessed through getters shown below
    private String sequence;

    // A constructor to create a genome with a given id and sequence
    public Genome(String id, String sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    // A constructor to create a genome for making copies
    private Genome(Genome genome) {
        this.id = genome.id;
        this.sequence = genome.sequence;
    }

    // Getters to ensure proper encapsulation
    public String getId() {
        return this.id;
    }

    // Two overridden getters of the interface
    @Override
    public int getLength() {
        return this.sequence.length();
    }

    @Override
    public String getSequence() {
        return this.sequence;
    }

    // A method to generate a SNP string of the given genome
    public String getDifference(Genome genome) {
        String sequence = genome.getSequence();
        StringBuilder seq = new StringBuilder(sequence.length());
        int length = Math.min(this.sequence.length(), sequence.length());
        for (int i = 0; i < length; i++) {
            char c = sequence.charAt(i);
            if (c == this.sequence.charAt(i)) {
                seq.append('.');
            }
            else {
                seq.append(c);
            }
        }
        // Fill remain characters
        for (int i = length; i < sequence.length(); i++) {
            seq.append(sequence.charAt(i));
        }
        return seq.toString();
    }

    // A method to restore sequence of the given SNP string
    public String getDifference(String sequence) {
        StringBuilder seq = new StringBuilder(sequence.length());
        int length = Math.min(this.sequence.length(), sequence.length());
        for (int i = 0; i < length; i++) {
            char c = sequence.charAt(i);
            if (c == '.') {
                c = this.sequence.charAt(i);
            }
            seq.append(c);
        }
        // fill remain characters
        for (int i = length; i < sequence.length(); i++) {
            seq.append(sequence.charAt(i));
        }
        return seq.toString();
    }

    // Overridden methods of the interfaces
    @Override
    public boolean containsSubSequence(String pattern) {
        return this.sequence.contains(pattern);
    }

    @Override
    public boolean replaceSubSequence(String pattern, String replacement) {
        if (this.containsSubSequence(pattern)) {
            this.sequence = this.sequence.replaceFirst(pattern, replacement);
            return true;
        }
        return false;
    }

    // A method to make a copy of the genome
    public Genome copy() {
        return new Genome(this);
    }
}
