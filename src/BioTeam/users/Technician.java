package BioTeam.users;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Technician extends User {
    private final List<String> backups;

    public Technician(String firstName, String lastName) {
        super(firstName, lastName);
        this.backups = new ArrayList<>();
    }

    public Technician(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
        this.backups = new ArrayList<>();
    }

    public void readBackUps() throws IOException {
        try (FileReader fileReader = new FileReader("backups.txt")) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String id = bufferedReader.readLine();
                while (id != null) {
                    backups.add(id);
                    id = bufferedReader.readLine();
                }
            }
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

    public void clean() {

    }
}
