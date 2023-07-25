package BioTeam;

import java.io.*;

public class Technician {
    private final Repository repository;

    public Technician(Repository repository) {
        this.repository = repository;
    }

    public void listUsers() {
        String[] users = this.repository.listUsers();

        for (int i = 0; i < users.length; i++) {
            System.out.println(i + " " + users[i]);
        }
    }

    public void deleteUser(String name) throws UserNotFound {
        this.repository.deleteUser(name);
    }

    public void addUser(String firstName, String lastName) {
        BioInformatician user = new BioInformatician(firstName, lastName);
        user.setAlignment(this.repository.getAlignment().copy());
        this.repository.addUser(user);
    }

    public void addUser(String firstName, String lastName, int exp) {
        BioInformatician user = new BioInformatician(firstName, lastName, exp);
        user.setAlignment(this.repository.getAlignment().copy());
        this.repository.addUser(user);
    }

    public void addUsers(FileReader file) throws IOException {
        BufferedReader reader = new BufferedReader(file);
        String line = reader.readLine();
        while (line != null) {
            String[] columns = line.split(" ");
            if (columns[0].equals("Bioinformatician")) {
                this.addUser(columns[1], columns[2], Integer.parseInt(columns[3]));
            }
            line = reader.readLine();
        }
    }

    public void setExperience(String name, int exp) throws UserNotFound {
        this.repository.getUser(name).setExperience(exp);
    }

    public void setAlignment(FileReader file) throws IOException, UserNotFound {
        Alignment a = Alignment.readFASTA(file);
        this.repository.setAlignment(a);
        for (int i = 0; i < this.repository.getSize(); i++) {
            this.repository.getUser(i).setAlignment(a.copy());
        }
    }

    public void backUp(File directory) throws IOException {
        this.repository.writeRepository(directory);
    }

    public void restore(File directory) throws IOException, UserNotFound {
        Repository save = Repository.readRepository(directory);

        this.repository.setAlignment(save.getAlignment());
        this.repository.deleteUsers();
        for (int i = 0; i < save.getSize(); i ++) {
            this.repository.addUser(save.getUser(i));
        }
    }

    public void restoreUser(String name, File directory) throws IOException, UserNotFound {
        Repository save = Repository.readRepository(directory);

        BioInformatician saved_user = save.getUser(name);
        BioInformatician user = this.repository.getUser(name);
        user.setAlignment(saved_user.getAlignment());
        user.setExperience(saved_user.getExperience());
    }

    public void restoreAlignment(File directory) throws IOException {
        Repository save = Repository.readRepository(directory);
        this.repository.setAlignment(save.getAlignment());
    }
}
