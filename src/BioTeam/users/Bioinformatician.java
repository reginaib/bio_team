package BioTeam.users;

import BioTeam.alignment.SNPAlignment;
import BioTeam.alignment.StandardAlignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// A class extends User and represents a bioinformatician
public class Bioinformatician extends User {
    // A private variable for storing the alignment of the bioinformatician
    private StandardAlignment alignment;

    // A constructor to create a bioinformatician with first and last name
    public Bioinformatician(String firstName, String lastName) {
        super(firstName, lastName);
    }

    // A constructor to create a bioinformatician with first name, last name and years of experience
    public Bioinformatician(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    // A method to get the alignment of the bioinformatician (encapsulation)
    public StandardAlignment getAlignment() throws IOException {
        if (this.alignment == null) {
            this.readAlignment();
        }
        return this.alignment;
    }

    // A method to set the standard alignment of the bioinformatician
    public void setAlignment(StandardAlignment alignment) throws IOException {
        this.alignment = alignment;
        // Writing the alignment and related data to files
        this.saveAlignment();
    }

    // A method to set the SNP alignment of the bioinformatician
    public void setAlignment(SNPAlignment alignment) throws IOException {
        this.alignment = alignment.getStandardAlignment();
        this.saveAlignment();
    }

    public void saveAlignment() throws IOException {
        this.writeAlignment();
        this.writeSNPAlignment();
        this.writeScore();
    }

    // A method for writing the alignment score of the bioinformatician to a file
    public void writeScore() throws IOException {
        try (FileWriter file = new FileWriter(this.getScoreName())) {
            file.write(Integer.toString(this.getAlignment().getScore()));
        }
    }

    //  A method for writing the alignment of the bioinformatician to a file
    public void writeAlignment() throws IOException {
        this.getAlignment().write(this.getAlignmentName());
    }

    // A method for writing the SNP alignment of the bioinformatician to a file
    public void writeSNPAlignment() throws IOException {
        this.getAlignment().getSNPAlignment().write(this.getSNPAlignmentName());
    }

    // A method to read the alignment of the bioinformatician from a file
    public void readAlignment() throws IOException {
        this.alignment = StandardAlignment.read(this.getAlignmentName());
    }

    // A method for adding a genome to the bioinformatician's alignment from a file
    public void addGenome(File file) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(file);
        this.getAlignment().addGenome(alignment.getGenome(0));
    }

    // A method for replacing a genome in the bioinformatician's alignment with another one from a file
    public void replaceGenome(int id, File file) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(file);
        this.getAlignment().replaceGenome(id, alignment.getGenome(0));
    }

    // A method for deleting a genome from the bioinformatician's alignment
    public void deleteGenome(int id) throws IOException {
        this.getAlignment().deleteGenome(id);
    }

    // A method for printing genome indices containing given subsequence
    public void containsGenomeSubSequence(String sequence) throws IOException {
        for (int id: this.getAlignment().containsGenomeSubSequence(sequence)) {
            System.out.println(id);
        }
    }

    // A method for replacing a subsequence in the bioinformatician's alignment with another one
    public boolean replaceGenomeSubSequence(String pattern, String replacement) throws IOException {
        return this.getAlignment().replaceGenomeSubSequence(pattern, replacement);
    }

    public File getAlignmentName() {
        return new File(this.getFullName() + ".alignment.txt");
    }

    public File getSNPAlignmentName() {
        return new File(this.getFullName() + ".snp_alignment.txt");
    }

    public File getScoreName() {
        return new File(this.getFullName() + ".score.txt");
    }
}
