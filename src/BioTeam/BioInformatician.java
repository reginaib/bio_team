package BioTeam;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BioInformatician {
    private final String firstName;
    private final String lastName;
    private Alignment alignment;
    private int experience;

    public BioInformatician(@NotNull String firstName, @NotNull String lastName) {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new EmptyName();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = 0;
        this.alignment = new Alignment();
    }

    public BioInformatician(@NotNull String firstName, @NotNull String lastName, int experience) {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new EmptyName();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        if (experience < 0) {
            throw new NegativeExperience();
        }
        this.experience = experience;
        this.alignment = new Alignment();
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(@NotNull Alignment alignment) {
        this.alignment = alignment;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        if (experience < 0) {
            throw new NegativeExperience();
        }
        this.experience = experience;
    }

    /**
     * Write user alignment to the given file.
     */
    public void writeFASTA(FileWriter file) throws EmptyAlignment, IOException {
        this.alignment.writeFASTA(file);
    }

    /**
     * Write user alignment to the given file with or not <full name> added to the FASTA headers.
     */
    public void writeFASTA(FileWriter file, boolean add_name) throws EmptyAlignment, IOException {
        if (add_name) {
            this.alignment.writeFASTA(file, this.getName());
        }
        else {
            this.alignment.writeFASTA(file);
        }
    }

    public void writeSNIP(FileWriter file) throws IOException, EmptyAlignment {
        this.alignment.writeSNIP(file);
    }

    public void writeSNIP(FileWriter file, boolean add_name) throws EmptyAlignment, IOException {
        if (add_name) {
            this.alignment.writeSNIP(file, this.getName());
        }
        else {
            this.alignment.writeSNIP(file);
        }
    }

    public void writeScore(FileWriter file) throws IOException, EmptyAlignment {
        file.write(this.alignment.getScore() + "\n");
    }

    public void writeScore(FileWriter file, boolean add_name) throws EmptyAlignment, IOException {
        if (add_name) {
            file.write(this.alignment.getScore() + " (" + this.getName() + ")\n");
        }
        else {
            this.writeScore(file);
        }
    }

    public void readFASTA(FileReader file) throws IOException {
        this.alignment = Alignment.readFASTA(file);
    }

    public void readSNIP(FileReader file) throws IOException {
        this.alignment = Alignment.readSNIP(file);
    }

    public void addGenome(FileReader file) throws IOException {
        Alignment alignment = Alignment.readFASTA(file);
        this.alignment.add(alignment.get(0));
    }

    public void replaceGenome(@NotNull String id, FileReader file) throws IOException {
        Alignment alignment = Alignment.readFASTA(file);
        this.alignment.replace(id, alignment.get(0));
    }

    public void replaceGenome(FileReader file) throws IOException {
        Alignment alignment = Alignment.readFASTA(file);
        this.alignment.replace(alignment.get(0));
    }

    public void findSubSequence(String sequence) {
        for (String id: this.alignment.containsSubSequence(sequence)) {
            System.out.println(id);
        }
    }
}
