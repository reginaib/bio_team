package BioTeam.users;

import BioTeam.alignment.SNPAlignment;
import BioTeam.alignment.StandardAlignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BioInformatician extends User {
    private StandardAlignment alignment;

    public BioInformatician(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public BioInformatician(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    public StandardAlignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(StandardAlignment alignment) {
        this.alignment = alignment;
    }

    public void setAlignment(SNPAlignment alignment) {
        this.alignment = alignment.getStandardAlignment();
    }

    public void writeScore() throws IOException {
        try (FileWriter file = new FileWriter(this.getFullName() + ".score.txt")) {
            file.write(this.alignment.getScore());
        }
    }

    public void readAlignment() throws IOException {
        this.alignment = StandardAlignment.read(new File(this.getFullName() + ".alignment.txt"));
    }

    public void writeAlignment() throws IOException {
        this.alignment.write(new File(this.getFullName() + ".alignment.txt"));
    }

    public void writeSNPAlignment() throws IOException {
        this.alignment.getSNPAlignment().write(new File(this.getFullName() + ".snp_alignment.txt"));
    }

    public void addGenome(File file) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(file);
        this.alignment.addGenome(alignment.getGenome(0));
    }

    public void replaceGenome(int id, File file) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(file);
        this.alignment.replaceGenome(id, alignment.getGenome(0));
    }

    public void deleteGenome(int id) {
        this.alignment.deleteGenome(id);
    }

    // print genome indices containing given subsequence
    public void containsGenomeSubSequence(String sequence) {
        for (int id: this.alignment.containsGenomeSubSequence(sequence)) {
            System.out.println(id);
        }
    }

    public boolean replaceGenomeSubSequence(String pattern, String replacement) {
        return this.alignment.replaceGenomeSubSequence(pattern, replacement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.getFullName().equals(((BioInformatician) obj).getFullName());
    }

    @Override
    public int hashCode() {
        return this.getFullName().hashCode();
    }
}
