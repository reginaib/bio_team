package BioTeam;

import org.jetbrains.annotations.NotNull;

public class Genome {
    private final String id;
    private String sequence;

    public Genome(@NotNull String id, @NotNull String sequence) {
        if (id.isBlank()) {
            throw new EmptyId();
        }
        else if (sequence.isBlank()) {
            throw new EmptySequence();
        }

        this.id = id;
        this.sequence = sequence;
    }

    private Genome(Genome genome) {
        this.id = genome.id;
        this.sequence = genome.sequence;
    }

    public Genome copy() {
        return new Genome(this);
    }

    public Genome fromSNIP(String id, @NotNull String sequence) {
        if (this.sequence.length() != sequence.length()) {
            throw new SequenceLengthMismatch();
        }
        StringBuilder seq = new StringBuilder(sequence.length());
        for (int i = 0; i < this.sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (c == '.') {
                c = this.sequence.charAt(i);
            }
            seq.append(c);
        }
        return new Genome(id, seq.toString());
    }

    public String getId() {
        return this.id;
    }

    public String getSequence() {
        return sequence;
    }

    public int getLength() {
        return this.sequence.length();
    }

    public boolean containsSubSequence(@NotNull String pattern) {
        if (pattern.isBlank()) {
            throw new InvalidPattern();
        }
        return this.sequence.contains(pattern);
    }

    public boolean replaceSubSequence(@NotNull String pattern, @NotNull String replacement) {
        if (pattern.isBlank()) {
            throw new InvalidPattern();
        }
        else if (replacement.isBlank() || pattern.length() != replacement.length()) {
            throw new InvalidReplacement();
        }
        else if (!this.sequence.contains(pattern)) {
            return false;
        }
        this.sequence = this.sequence.replaceAll(pattern, replacement);
        return true;
    }

    public String getFASTA() {
        return ">" + this.id + "\n" + this.sequence + "\n";
    }

    public String getFASTA(@NotNull String meta) {
        return ">" + this.id + " (" + meta + ")\n" + this.sequence + "\n";
    }

    public String getSNIP(@NotNull Genome base) {
        return this.getSNIP(base, "");
    }

    public String getSNIP(@NotNull Genome base, @NotNull String meta) {
        if (this.sequence.length() != base.getLength()) {
            throw new SequenceLengthMismatch();
        }
        String base_seq = base.getSequence();
        StringBuilder seq = new StringBuilder(this.sequence.length());
        for (int i = 0; i < this.sequence.length(); i ++) {
            char c = this.sequence.charAt(i);
            if (c == base_seq.charAt(i)) {
                seq.append(".");
            }
            else {
                seq.append(c);
            }
        }
        if (meta.isBlank()) {
            return ">" + this.id + "\n" + seq + "\n";
        }
        else{
            return ">" + this.id + " (" + meta + ")\n" + seq + "\n";
        }
    }
}
