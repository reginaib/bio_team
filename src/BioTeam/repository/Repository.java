package BioTeam.repository;

import BioTeam.users.Bioinformatician;
import BioTeam.users.TeamLead;
import BioTeam.users.TechnicalSupport;
import BioTeam.users.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

// A class representing the repository
public class Repository {
    private final HashMap<User, Integer> experience;
    private final Properties properties;

    public Repository(Properties properties) {
        this.experience = new HashMap<>();
        this.properties = properties;
    }

    public void loadRepo() throws IOException {
        try (FileReader fileReader = new FileReader(properties.getProperty("fastafilename"))) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String user = bufferedReader.readLine();
                while (user != null) {
                    String[] meta = user.split(" ");

                    String firstName = meta[1];
                    String secondName = meta[2];
                    int expirience = Integer.parseInt(meta[3]);

                    switch (meta[0]) {
                        case "TeamLead" ->
                                this.experience.put(new TeamLead(firstName, secondName, expirience), expirience);
                        case "Bioinformatician" ->
                                this.experience.put(new Bioinformatician(firstName, secondName, expirience), expirience);
                        case "TechnicalSupport" ->
                                this.experience.put(new TechnicalSupport(firstName, secondName, expirience), expirience);
                    }
                    user = bufferedReader.readLine();
                }
            }
        }
    }

    public File getOptimalAlignment() {
        return new File(this.properties.getProperty("fastafilename"));
    }

    public File getOptimalSNPAlignment() {
        return new File(this.properties.getProperty("snipfilename"));
    }

    public File getOptimalScore() {
        return new File(this.properties.getProperty("scorefilename"));
    }

    public File getBackUpList() {
        return new File(this.properties.getProperty("backupfilename"));
    }

    public Bioinformatician[] getBioinformaticians() {
        List<Bioinformatician> users = new ArrayList<>();

        for (Entry<User, Integer> user: this.experience.entrySet()) {
            if (user instanceof Bioinformatician) {
                users.add((Bioinformatician) user.getKey());
            }
        }
        return users.toArray(new Bioinformatician[0]);
    }

    public User loadUser(User user) {
        Integer exp = this.experience.get(user);
        if (exp == null) return null;
        user.setExperience(exp);
        return user;
    }
}
