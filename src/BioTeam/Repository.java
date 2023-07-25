package BioTeam;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private Alignment alignment;
    private final List<BioInformatician> users;

    public Repository() {
        this.alignment = new Alignment();
        this.users = new ArrayList<>();
    }

    public static Repository readRepository(@NotNull File directory) throws IOException {
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
            repository.setAlignment(Alignment.readFASTA(file));
        }
        catch (FileNotFoundException ignored) {}

        return repository;
    }

    public void writeRepository(@NotNull File directory) throws IOException {
        if (!directory.exists()) {
            directory.mkdir();
        }
        try (FileWriter file = new FileWriter(new File(directory, "repository.txt"))) {
            for (BioInformatician user: this.users) {
                file.write(user.getName() + " " + user.getExperience() + "\n");
                try (FileWriter fasta = new FileWriter(new File(directory, user.getName() + ".alignment.txt"))) {
                    user.writeFASTA(fasta, false);
                }
                catch (EmptyAlignment ignored) {}
            }
        }

        try (FileWriter file = new FileWriter(new File(directory, "alignment.txt"))) {
            this.alignment.writeFASTA(file);
        }
        catch (EmptyAlignment ignored) {}
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(@NotNull Alignment alignment) {
        this.alignment = alignment;
    }

    public String[] listUsers() {
        String[] users = new String[this.users.size()];
        for (int i = 0; i < this.users.size(); i++) {
            users[i] = this.users.get(i).getName();
        }
        return users;
    }

    public int[] listScores() throws EmptyAlignment {
        int[] scores = new int[this.users.size()];
        for (int i = 0; i < this.users.size(); i++) {
            scores[i] = this.users.get(i).getAlignment().getScore();
        }
        return scores;
    }

    public int getSize() {  // number of users
        return this.users.size();
    }

    public BioInformatician getUser(int id) throws UserNotFound {
        try{
            return this.users.get(id);
        }
        catch (IndexOutOfBoundsException e) {
            throw new UserNotFound();
        }
    }

    public BioInformatician getUser(@NotNull String name) throws UserNotFound {
        for (BioInformatician user : this.users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        throw new UserNotFound();
    }

    public void addUser(@NotNull BioInformatician user) {
        this.users.add(user);
    }

    public void deleteUser(@NotNull String name) throws UserNotFound {
        for (BioInformatician user : this.users) {
            if (user.getName().equals(name)) {
                this.users.remove(user);
                return;
            }
        }
        throw new UserNotFound();
    }

    public void deleteUsers() {
        this.users.clear();
    }
}
