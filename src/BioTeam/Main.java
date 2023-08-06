package BioTeam;

import BioTeam.repository.Repository;
import BioTeam.repository.UserNotFound;
import BioTeam.users.Bioinformatician;
import BioTeam.users.TeamLead;
import BioTeam.users.TechnicalSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        // Loading repo with given setup
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        }
        Repository repository = new Repository(properties);
        repository.loadRepo();

        // Loading bioinformatician
        Bioinformatician bioinformatician = new Bioinformatician("Marc", "Janssens");
        try {
            bioinformatician = (Bioinformatician) repository.loadUser(bioinformatician);

            if (bioinformatician.replaceGenomeSubSequence("TTTCCTGCGGACAGACC", "REPLACED")) {
                System.out.println("Genome substring replaced");
            }

            // delete gene 1 from alignment
            bioinformatician.deleteGenome(1);

            // add new gene to the alignment
            bioinformatician.addGenome(new File("gene1.fasta"));

            // replace gene number 3
            bioinformatician.replaceGenome(3, new File("gene2.fasta"));

            // Print genes with the give fragment
            bioinformatician.containsGenomeSubSequence("TTTTCCCCAAAAGGGG");

            // save to disc
            bioinformatician.saveAlignment();
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("Bioinformatician api tested");
        }

        TeamLead teamLead = new TeamLead("Jozef", "Groenewegen");
        try {
            teamLead = (TeamLead) repository.loadUser(teamLead);

            // Write users' alignments into single file
            teamLead.writeAlignments();
            teamLead.writeSNPAlignments();
            // Write scores
            teamLead.writeScores();

            // Reset to current optimal
            teamLead.resetBioinformaticianAlignment(bioinformatician);

            // Promote user alignment to optimal
            teamLead.updateOptimalAlignment(bioinformatician);
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        }

        TechnicalSupport technicalSupport = new TechnicalSupport("Jeff", "Stevenson");
        try {
            technicalSupport = (TechnicalSupport) repository.loadUser(technicalSupport);

            String version = technicalSupport.backUp();
            System.out.println(version);

            technicalSupport.clean();

            technicalSupport.restore(version);
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        }
    }
}
