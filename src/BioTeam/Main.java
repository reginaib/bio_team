package BioTeam;

import BioTeam.repository.Repository;
import BioTeam.repository.UserNotFound;
import BioTeam.users.Bioinformatician;
import BioTeam.users.TeamLead;
import BioTeam.users.TechnicalSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        // Loading repo with given setup
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (FileNotFoundException fnf) {
            System.out.println("File not found: " + fnf);
        } catch (IOException io) {
            System.out.println("An error occurred: " + io);
        }
        // Initializing the repository and loading data
        Repository repository = new Repository(properties);
        repository.loadRepo();

        // Loading bioinformatician
        Bioinformatician bioinformatician = new Bioinformatician("Marc", "Janssens");
        try {
            // Loading and configuring the bioinformatician
            bioinformatician = repository.loadUser(bioinformatician);

            // Replacing a specific genome substring
            if (bioinformatician.replaceGenomeSubSequence("TTTCCTGCGGACAGACC", "REPLACED")) {
                System.out.println("Genome substring replaced");
            }

            // Deleting the gene 1 from alignment
            bioinformatician.deleteGenome(1);

            // Adding new gene to the alignment
            bioinformatician.addGenome(new File("gene1.fasta"));

            // Replacing gene number 3
            bioinformatician.replaceGenome(3, new File("gene2.fasta"));

            // Printing genes with the give fragment
            bioinformatician.containsGenomeSubSequence("TTTTCCCCAAAAGGGG");

            // Saving to disc
            bioinformatician.saveAlignment();
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Bioinformatician api tested");
        }

        TeamLead teamLead = new TeamLead("Jozef", "Groenewegen");
        try {
            // Loading and configuring the team lead
            teamLead = repository.loadUser(teamLead);

            // Write users' alignments into single file
            teamLead.writeAlignments();
            teamLead.writeSNPAlignments();

            // Write scores to a file
            teamLead.writeScores();

            // Reset the bioinformatician's alignment to the current optimal state
            teamLead.resetBioinformaticianAlignment(bioinformatician);

            // Promote the bioinformatician's alignment to the optimal state
            teamLead.updateOptimalAlignment(bioinformatician);
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("TeamLead api tested");
        }

        TechnicalSupport technicalSupport = new TechnicalSupport("Jeff", "Stevenson");
        try {
            // Loading and configuring the technical support user
            technicalSupport = repository.loadUser(technicalSupport);

            // Create a backup and get its version
            String version = technicalSupport.backUp();
            System.out.println(version);

            // Perform a cleanup operation
            technicalSupport.clean();

            // Restore from a specific backup version
            technicalSupport.restore(version);
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("TechnicalSupport api tested");
        }
    }
}
