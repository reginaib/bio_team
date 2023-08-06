package BioTeam.repository;

import BioTeam.alignment.StandardAlignment;
import BioTeam.users.Bioinformatician;
import BioTeam.users.TeamLead;
import BioTeam.users.TechnicalSupport;
import BioTeam.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

// A class representing the repository
public class Repository {
    private final HashMap<User, Integer> usersData;
    private final Properties properties;

    public Repository(Properties properties) {
        this.usersData = new HashMap<>();
        this.properties = properties;
    }

    public void loadRepo() throws IOException {
        try (FileReader fileReader = new FileReader(properties.getProperty("teamfilename"))) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String user = bufferedReader.readLine();
                while (user != null) {
                    String[] meta = user.split(" ");

                    String firstName = meta[1];
                    String secondName = meta[2];
                    int expirience = Integer.parseInt(meta[3]);

                    switch (meta[0]) {
                        case "TeamLead" ->
                                this.usersData.put(new TeamLead(firstName, secondName, expirience), expirience);
                        case "Bioinformatician" -> {
                                Bioinformatician bi = new Bioinformatician(firstName, secondName, expirience);
                                if (!bi.getAlignmentName().exists()) {
                                    // set default alignment
                                    bi.setAlignment(StandardAlignment.read(this.getDefaultAlignment()));
                                }
                                this.usersData.put(bi, expirience);
                        }
                        case "TechnicalSupport" ->
                                this.usersData.put(new TechnicalSupport(firstName, secondName, expirience), expirience);
                    }
                    user = bufferedReader.readLine();
                }
            }
        }

        if (!this.getOptimalAlignment().exists()) {
            StandardAlignment alignment = StandardAlignment.read(this.getDefaultAlignment());
            alignment.write(this.getOptimalAlignment());
            alignment.getSNPAlignment().write(this.getOptimalSNPAlignment());
            try (FileWriter file = new FileWriter(this.getOptimalScore())) {
                file.write(Integer.toString(alignment.getScore()));
            }
        }
    }

    public File getDefaultAlignment() {
        return new File(this.properties.getProperty("fastafilename"));
    }

    public File getOptimalAlignment() {
        return new File(this.properties.getProperty("optimalfilename"));
    }

    public File getOptimalSNPAlignment() {
        return new File(this.properties.getProperty("optimalsnipfilename"));
    }

    public File getOptimalScore() {
        return new File(this.properties.getProperty("scorefilename"));
    }

    public File getBackUpList() {
        return new File(this.properties.getProperty("backupfilename"));
    }

    public Bioinformatician[] getBioinformaticians() {
        List<Bioinformatician> users = new ArrayList<>();

        for (Entry<User, Integer> user: this.usersData.entrySet()) {
            if (user.getKey() instanceof Bioinformatician) {
                users.add((Bioinformatician) user.getKey());
            }
        }
        return users.toArray(new Bioinformatician[0]);
    }

    public User loadUser(User user) throws UserNotFound {
        Integer exp = this.usersData.get(user);
        if (exp == null) throw new UserNotFound("User not found");
        user.setExperience(exp);
        if (user instanceof TeamLead) {
            ((TeamLead) user).setRepository(this);
        }
        if (user instanceof TechnicalSupport) {
            ((TechnicalSupport) user).setRepository(this);
        }
        return user;
    }
}
