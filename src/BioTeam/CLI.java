package BioTeam;

import BioTeam.repository.Repository;
import BioTeam.users.BioInformatician;
import BioTeam.users.Leader;
import BioTeam.users.Technician;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CLI {
    public static ArgumentParser parser() {
        ArgumentParser parser = ArgumentParsers.newFor("BioTeam").build().defaultHelp(true)
                .description("Alignments management system");
        parser.addArgument("-r", "--repo").type(File.class).setDefault(new File("repo"))
                .help("path to repository directory");

        Subparsers role = parser.addSubparsers().dest("role");

        Subparser tech = role.addParser("technician").help("repository technical support");
        Subparsers actions = tech.addSubparsers().dest("action");

        actions.addParser("backup").help("save current repository state")
                .addArgument("-o").type(File.class).required(true).help("path to the backup directory");

        Subparser restore = actions.addParser("restore").help("restore from backup");
        restore.addArgument("-i").type(File.class).required(true).help("path to the backup directory");
        restore.addArgument("-f").action(Arguments.storeTrue()).help("full repository restore");
        restore.addArgument("-a").action(Arguments.storeTrue()).help("repository alignment restore");
        restore.addArgument("-u").help("user name to restore");

        actions.addParser("list").help("list users");
        Subparser add_user = actions.addParser("add").help("add new user");
        add_user.addArgument("-f", "--first_name").required(true);
        add_user.addArgument("-s", "--second_name").required(true);
        add_user.addArgument("-e", "--experience").type(Integer.class);

        actions.addParser("del").help("delete user")
                .addArgument("-n", "--name").required(true).help("user full name");

        Subparser set_exp = actions.addParser("set").help("set user experience");
        set_exp.addArgument("value").type(Integer.class);
        set_exp.addArgument("-n", "--name").required(true).help("user full name");

        actions.addParser("populate").help("add multiple users from file")
                .addArgument("-f").type(File.class).help("path to file");
        actions.addParser("alignment").help("set new global and users alignment")
                .addArgument("-f").type(File.class).help("path to fasta file");

        Subparser lead = role.addParser("leader").help("team leader interface");
        actions = lead.addSubparsers().dest("action");

        actions.addParser("promote").help("copy user alignment to the global")
                .addArgument("-n", "--name").required(true).help("user full name");
        actions.addParser("set").help("copy global alignment to the user")
                .addArgument("-n", "--name").required(true).help("user full name");
        actions.addParser("score").help("write user scores")
                .addArgument("-f").type(File.class).setDefault(new File("total.score.txt")).help("path to the score file");
        actions.addParser("fasta").help("write user alignments")
                .addArgument("-f").type(File.class).setDefault(new File("total.alignment.txt")).help("path to the fasta file");
        actions.addParser("snip").help("write user alignments in snip")
                .addArgument("-f").type(File.class).setDefault(new File("total.alignment.txt")).help("path to the fasta file");

        Subparser user = role.addParser("bioinformatician").help("user interface");
        user.addArgument("-n", "--name").required(true).help("user name");
        actions = user.addSubparsers().dest("action");

        actions.addParser("write-fasta").help("write user alignment")
                .addArgument("-f").type(File.class).help("path to the fasta file");
        actions.addParser("write-snip").help("write user alignment in snip")
                .addArgument("-f").type(File.class).help("path to the fasta file");
        actions.addParser("write-score").help("write an alignment score to the file")
                .addArgument("-f").type(File.class).help("path to the report file");
        actions.addParser("print-score").help("print an alignment score");
        Subparser replace_genome = actions.addParser("replace-genome").help("replace the genome with the genome from the file");
        replace_genome.addArgument("-f").type(File.class).required(true).help("path to the fasta file");
        replace_genome.addArgument("-id").help("id of the genome in the alignment to replace. If omitted, use id from the given genome file");
        actions.addParser("add-genome").help("add a new genome to the alignment")
                .addArgument("-f").type(File.class).help("path to the fasta file");
        actions.addParser("delete-genome").help("delete a genome from the alignment")
                .addArgument("-id").required(true).help("genome id");
        actions.addParser("find").help("find a subsequence")
                .addArgument("-s").required(true).help("subsequence");
        Subparser replace = actions.addParser("replace").help("replace all occurrences of a given sequence");
        replace.addArgument("-id").help("replace in a genome with a given id. If omitted, replace in a whole alignment");
        replace.addArgument("-s").required(true).help("sequence to replace");
        replace.addArgument("-r").required(true).help("new sequence");
        return parser;
    }

    public static void main(String[] args) throws IOException, UserNotFound, EmptyAlignment {
        ArgumentParser parser = CLI.parser();
        try {
            Namespace ns = parser.parseArgs(args);
            Repository repository = Repository.readRepository(ns.get("repo"));

            switch ((String) ns.get("role")) {
                case "technician" -> {
                    Technician technician = new Technician(repository);
                    switch ((String) ns.get("action")) {
                        case "backup" -> technician.backUp(ns.get("o"));
                        case "restore" -> {
                            if (ns.get("f")) {
                                technician.restore(ns.get("i"));
                            } else if (ns.get("a")) {
                                technician.restoreAlignment(ns.get("i"));
                            } else {
                                technician.restoreUser(ns.get("u"), ns.get("i"));
                            }
                        }
                        case "list" -> technician.listUsers();
                        case "add" -> {
                            if (ns.get("experience") == null) {
                                technician.addUser(ns.get("first_name"), ns.get("second_name"));
                            } else {
                                technician.addUser(ns.get("first_name"), ns.get("second_name"), ns.get("experience"));
                            }
                        }
                        case "del" -> technician.deleteUser(ns.get("name"));
                        case "set" -> technician.setExperience(ns.get("name"), ns.get("value"));
                        case "populate" -> {
                            try (FileReader file = new FileReader((File) ns.get("f"))) {
                                technician.addUsers(file);
                            }
                        }
                        case "alignment" -> {
                            try (FileReader file = new FileReader((File) ns.get("f"))) {
                                technician.setAlignment(file);
                            }
                        }
                    }
                }
                case "leader" -> {
                    Leader leader = new Leader(repository);
                    switch ((String) ns.get("action")) {
                        case "promote" -> leader.promoteUserAlignment(ns.get("name"));
                        case "set" -> leader.setUserAlignment(ns.get("name"));
                        case "score" -> {
                            try (FileWriter file = new FileWriter((File) ns.get("f"))) {
                                leader.writeScore(file);
                            }
                        }
                        case "fasta" -> {
                            try (FileWriter file = new FileWriter((File) ns.get("f"))) {
                                leader.writeFASTA(file);
                            }
                        }
                        case "snip" -> {
                            try (FileWriter file = new FileWriter((File) ns.get("f"))) {
                                leader.writeSNIP(file);
                            }
                        }
                    }
                }
                case "bioinformatician" -> {
                    BioInformatician bioinformatician = repository.getUser(ns.get("name"));
                    File f;
                    switch ((String) ns.get("action")) {
                        case "write-fasta" -> {
                            f = ns.get("f");
                            if (f == null) {
                                f = new File(bioinformatician.getName() + ".alignment.txt");
                            }
                            try (FileWriter file = new FileWriter(f)) {
                                bioinformatician.writeFASTA(file);
                            }
                        }
                        case "write-snip" -> {
                            f = ns.get("f");
                            if (f == null) {
                                f = new File(bioinformatician.getName() + ".alignment.txt");
                            }
                            try (FileWriter file = new FileWriter(f)) {
                                bioinformatician.writeSNIP(file);
                            }
                        }
                        case "write-score" -> {
                            f = ns.get("f");
                            if (f == null) {
                                f = new File(bioinformatician.getName() + ".score.txt");
                            }
                            try (FileWriter file = new FileWriter(f)) {
                                bioinformatician.writeScore(file);
                            }
                        }
                        case "print-score" -> System.out.println(bioinformatician.getAlignment().getScore());
                        case "replace-genome" -> {
                            try (FileReader file = new FileReader((File) ns.get("f"))) {
                                if (ns.get("id") == null) {
                                    bioinformatician.replaceGenome(file);
                                }
                                else {
                                    bioinformatician.replaceGenome(ns.get("id"), file);
                                }
                            }
                        }
                        case "add-genome" -> {
                            try (FileReader file = new FileReader((File) ns.get("f"))) {
                                bioinformatician.addGenome(file);
                            }
                        }
                        case "delete-genome" -> bioinformatician.getAlignment().delete(ns.get("id"));
                        case "find" -> bioinformatician.findSubSequence(ns.get("s"));
                        case "replace" -> {
                            if (ns.get("id") == null) {
                                bioinformatician.getAlignment().replaceSubSequence(ns.get("s"), ns.get("r"));
                            }
                            else {
                                bioinformatician.getAlignment().get(ns.get("id"))
                                        .replaceSubSequence(ns.get("s"), ns.get("r"));
                            }
                        }
                    }
                }
            }

            repository.writeRepository(ns.get("repo"));
        }
        catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
}
