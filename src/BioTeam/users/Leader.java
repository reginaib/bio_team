package BioTeam.users;

import BioTeam.alignment.StandardAlignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Leader extends User{
    public Leader(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Leader(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    public void promoteBioInformaticianAlignment(BioInformatician user) throws IOException {
        user.getAlignment().write(new File("optimal.alignment.txt"));
        user.getAlignment().getSNPAlignment().write(new File("optimal.snip_alignment.txt"));

        try (FileWriter file = new FileWriter("optimal.score.txt")) {
            file.write(user.getAlignment().getScore());
        }
    }

    public void resetBioInformaticianAlignment(BioInformatician user) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(new File("optimal.alignment.txt"));
        user.setAlignment(alignment);
        // save to disc
        user.writeAlignment();
        user.writeSNPAlignment();
        user.writeScore();
    }

    public void writeScores(BioInformatician[] users) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(new File("optimal.alignment.txt"));

        try (FileWriter file = new FileWriter(this.getFullName() + ".score.txt")) {
            file.write("optimal\t" + alignment.getScore() + '\n');
            for (BioInformatician user: users) {
                file.write(user.getFullName() + '\t' + user.getAlignment().getScore() + '\n');
            }
        }
    }

    public void writeAlignments(BioInformatician[] users) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(new File("optimal.alignment.txt"));

        File file = new File(this.getFullName() + ".alignment.txt");
        alignment.write(file, " (optimal)");

        for (BioInformatician user: users) {
            user.getAlignment().write(file, " (" + user.getFullName() + ")", true);
        }
    }

    public void writeSNPAlignments(BioInformatician[] users) throws IOException {
        StandardAlignment alignment = StandardAlignment.read(new File("optimal.alignment.txt"));

        File file = new File(this.getFullName() + ".snp_alignment.txt");
        alignment.getSNPAlignment().write(file, " (optimal)");

        for (BioInformatician user: users) {
            user.getAlignment().getSNPAlignment().write(file, " (" + user.getFullName() + ")", true);
        }
    }
}
