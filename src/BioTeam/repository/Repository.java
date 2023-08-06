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

// A class representing the repository
public class Repository {
    // A variable for storing users' data
    private final HashMap<User, Integer> usersData;
    // A variable for repository configuration
    private final Properties properties;

    private final List<Bioinformatician> bioinformaticians;

    // A standard constructor
    public Repository(Properties properties) {
        this.usersData = new HashMap<>();
        this.bioinformaticians = new ArrayList<>();
        this.properties = properties;
    }

    // A method for loading repository from files
    public void loadRepo() throws IOException {
        // Loading user data from team file
        try (FileReader fileReader = new FileReader(properties.getProperty("teamfilename"))) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String user = bufferedReader.readLine();
                while (user != null) {
                    String[] meta = user.split(" ");

                    String firstName = meta[1];
                    String secondName = meta[2];
                    int experience = Integer.parseInt(meta[3]);

                    // Creating user instances based on roles
                    switch (meta[0]) {
                        case "TeamLead" ->
                                this.usersData.put(new TeamLead(firstName, secondName, experience), experience);
                        case "Bioinformatician" -> {
                                Bioinformatician bi = new Bioinformatician(firstName, secondName, experience);
                                if (!bi.getAlignmentName().exists()) {
                                    // set default alignment
                                    bi.setAlignment(StandardAlignment.read(this.getDefaultAlignment()));
                                }
                                this.bioinformaticians.add(bi);
                                this.usersData.put(bi, experience);
                        }
                        case "TechnicalSupport" ->
                                this.usersData.put(new TechnicalSupport(firstName, secondName, experience), experience);
                    }
                    user = bufferedReader.readLine();
                }
            }
        }
        // Creating and storing the optimal alignment if not already present
        if (!this.getOptimalAlignment().exists()) {
            StandardAlignment alignment = StandardAlignment.read(this.getDefaultAlignment());
            alignment.write(this.getOptimalAlignment());
            alignment.getSNPAlignment().write(this.getOptimalSNPAlignment());
            try (FileWriter file = new FileWriter(this.getOptimalScore())) {
                file.write(Integer.toString(alignment.getScore()));
            }
        }
    }

    // Methods to retrieve file paths
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

    // A method for retrieving bioinformaticians from usersData
    public Bioinformatician[] getBioinformaticians() {
        return this.bioinformaticians.toArray(new Bioinformatician[0]);
    }

    // Methods for loading user and set their experience and repository
    public Bioinformatician loadUser(Bioinformatician user) throws UserNotFound {
        Integer exp = this.usersData.get(user);
        if (exp == null) throw new UserNotFound("User not found");
        user.setExperience(exp);
        return user;
    }

    public TeamLead loadUser(TeamLead user) throws UserNotFound {
        Integer exp = this.usersData.get(user);
        if (exp == null) throw new UserNotFound("User not found");
        user.setExperience(exp);
        user.setRepository(this);
        return user;
    }

    public TechnicalSupport loadUser(TechnicalSupport user) throws UserNotFound {
        Integer exp = this.usersData.get(user);
        if (exp == null) throw new UserNotFound("User not found");
        user.setExperience(exp);
        user.setRepository(this);
        return user;
    }
}
