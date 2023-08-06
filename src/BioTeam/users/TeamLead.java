package BioTeam.users;

import BioTeam.alignment.StandardAlignment;
import BioTeam.repository.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// A class extends User and represents a team lead
public class TeamLead extends User{
    Repository repository;

    // A constructor to create a TeamLead with first and last names
    public TeamLead(String firstName, String lastName) {
        super(firstName, lastName); // calls the constructor of the superclass
    }

    // A constructor to create a TeamLead with first name, last name and years of experience
    public TeamLead(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    // A method for updating the optimal alignment and SNP alignment in the repository based on a bioinformatician's alignment
    public void updateOptimalAlignment(Bioinformatician user) throws IOException {
        // Writing the Bioinformatician's alignment and SNP alignment to the optimal alignment and SNP alignment files in the repository
        user.getAlignment().write(this.repository.getOptimalAlignment());
        user.getAlignment().getSNPAlignment().write(this.repository.getOptimalSNPAlignment());

        // Writing the Bioinformatician's alignment score to the optimal score file in the repository
        try (FileWriter file = new FileWriter(this.repository.getOptimalScore())) {
            file.write(Integer.toString(user.getAlignment().getScore()));
        }
    }

    // A method for resetting a bioinformatician's alignment to the optimal alignment in the repository
    public void resetBioinformaticianAlignment(Bioinformatician user) throws IOException {
        // Read the optimal alignment from the repository
        StandardAlignment alignment = StandardAlignment.read(this.repository.getOptimalAlignment());
        // Set the bioinformatician's alignment to the optimal alignment
        user.setAlignment(alignment);
    }

    // A method for writing the scores of all bioinformaticians to a file
    public void writeScores() throws IOException {
        // Reading the optimal alignment from the repository
        StandardAlignment alignment = StandardAlignment.read(this.repository.getOptimalAlignment());

        // Writing the optimal alignment score and the scores of all bioinformaticians to a file
        try (FileWriter file = new FileWriter(this.getFullName() + ".score.txt")) {
            file.write("Optimal\t" + alignment.getScore() + '\n');
            for (Bioinformatician user: this.repository.getBioinformaticians()) {
                file.write(user.getFullName() + '\t' + user.getAlignment().getScore() + '\n');
            }
        }
    }

    // A method for writing all standard alignments to a file
    public void writeAlignments() throws IOException {
        // Reading the optimal alignment from the repository
        StandardAlignment alignment = StandardAlignment.read(this.repository.getOptimalAlignment());

        // Creating a file for writing the alignments
        File file = new File(this.getFullName() + ".alignment.txt");
        // Writing the optimal alignment to the file with a label "(optimal)"
        alignment.write(file, " (optimal)");

        // Writing the alignments of all bioinformaticians to the same file with their names as labels
        for (Bioinformatician user: this.repository.getBioinformaticians()) {
            user.getAlignment().write(file, " (" + user.getFullName() + ")", true);
        }
    }

    // A method for writing all SNP alignments to a file
    public void writeSNPAlignments() throws IOException {
        // Reading the optimal alignment from the repository
        StandardAlignment alignment = StandardAlignment.read(this.repository.getOptimalAlignment());

        // Creating a file for writing the SNP alignments
        File file = new File(this.getFullName() + ".snp_alignment.txt");
        // Writing the optimal SNP alignment to the file with a label "(optimal)"
        alignment.getSNPAlignment().write(file, " (optimal)");

        // Writing the SNP alignments of all bioinformaticians to the same file with their names as labels
        for (Bioinformatician user: this.repository.getBioinformaticians()) {
            user.getAlignment().getSNPAlignment().write(file, " (" + user.getFullName() + ")", true);
        }
    }
}
