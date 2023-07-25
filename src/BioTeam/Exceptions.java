package BioTeam;

class NegativeExperience extends IllegalArgumentException {
    public NegativeExperience() {
        super("Experience should be positive");
    }
}

class EmptyName extends IllegalArgumentException {
    public EmptyName() {
        super("Null pointer of empty name is not allowed");
    }
}

class UserNotFound extends Exception {
    public UserNotFound() {
        super("User not found");
    }
}

class EmptyAlignment extends Exception {
    public EmptyAlignment() {
        super("Alighment doesn't have genomes");
    }
}

class EmptyId extends IllegalArgumentException {
    public EmptyId() {
        super("Null pointer or Empty Genome ID is not allowed");
    }
}

class EmptySequence extends IllegalArgumentException {
    public EmptySequence() {
        super("Null pointer or Empty Genome sequence is not allowed");
    }
}

class InvalidReplacement extends IllegalArgumentException {
    public InvalidReplacement() {
        super("Null pointer to replacement or pattern and replacement lengths don't match");
    }
}

class InvalidPattern extends IllegalArgumentException {
    public InvalidPattern() {
        super("Null pointer or empty pattern");
    }
}

class DuplicateId extends IllegalArgumentException {
    public DuplicateId() {
        super("All genomes should have unique ids in Alignment");
    }
}

class SequenceLengthMismatch extends IllegalArgumentException {
    public SequenceLengthMismatch() {
        super("All genomes should have equal length of sequences");
    }
}

class InvalidId extends IllegalArgumentException {
    public InvalidId() {
        super("Alignment doesn't have genome with the given ID");
    }
}