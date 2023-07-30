package BioTeam.repository;

import BioTeam.users.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// A class representing the repository
public class Repository {
    private final List<User> users;

    public Repository() {
        this.users = new ArrayList<>();
    }

    public static Repository readRepository() throws IOException {
        Repository repository = new Repository();

        try (FileReader file = new FileReader(new File(directory, "repository.txt"))) {
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(" ");
                BioInformatician user = new BioInformatician(columns[0], columns[1],
                                                             Integer.parseInt(columns[2]));

                try (FileReader fasta = new FileReader(new File(directory, user.getName() + ".alignment.txt"))) {
                    user.readFASTA(fasta);
                }
                catch (FileNotFoundException ignored) {}

                repository.addUser(user);
                line = reader.readLine();
            }
        }
        catch (FileNotFoundException ignored) {}

        try (FileReader file = new FileReader(new File(directory, "alignment.txt"))) {
            repository.setAlignment(AlignmentBU.readFASTA(file));
        }
        catch (FileNotFoundException ignored) {}

        return repository;
    }

    public String[] listUsers() {
        String[] users = new String[this.users.size()];
        for (int i = 0; i < this.users.size(); i++) {
            users[i] = this.users.get(i).getName();
        }
        return users;
    }
}
