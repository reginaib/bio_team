package BioTeam.users;

import BioTeam.repository.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// A class extends User and represents a technical support person
public class TechnicalSupport extends User {
    // A private variable to store the list of repository backups
    private List<String> backups;
    Repository repository;

    // A constructor to create a technical support instance with first and last name
    public TechnicalSupport(String firstName, String lastName) {
        super(firstName, lastName);
    }

    // A constructor to create a technical support instance with first name, last name and years of experience
    public TechnicalSupport(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    List<String> getBackups() throws IOException {
        if (this.backups == null) {
            this.backups = new ArrayList<>();
            this.readBackUps();
        }
        return this.backups;
    }

    // A method for reading the list of backups from a file in the repository
    private void readBackUps() throws IOException {
        if (!this.repository.getBackUpList().exists()) return;
        try (FileReader fileReader = new FileReader(this.repository.getBackUpList())) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                // Reading each version from the file and adding it to the backups list
                String version = bufferedReader.readLine();
                while (version != null) {
                    backups.add(version);
                    version = bufferedReader.readLine();
                }
            }
        }
    }

    // A method for writing the list of backups to a file in the repository
    private void writeBackUps() throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.repository.getBackUpList())) {
            // Writing each version from the backups list to the file
            for (String version : this.getBackups()) {
                fileWriter.write(version + '\n');
            }
        }
    }

    // A method for creating a backup of the repository's data
    public String backUp() throws IOException {
        // Creating a timestamp to be used as the version of the backup
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        String version = dateFormat.format(date);

        File dir = new File(version);
        dir.mkdir();

        // Creating copies of the files for optimal alignment, SNP alignment, and score
        Files.copy(this.repository.getOptimalAlignment().toPath(),
                dir.toPath().resolve(this.repository.getOptimalAlignment().toPath()),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(this.repository.getOptimalSNPAlignment().toPath(),
                dir.toPath().resolve(this.repository.getOptimalSNPAlignment().toPath()),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(this.repository.getOptimalScore().toPath(),
                dir.toPath().resolve(this.repository.getOptimalScore().toPath()),
                StandardCopyOption.REPLACE_EXISTING);

        // Creating copies of the files for each bioinformatician's alignment, SNP alignment, and score
        for (Bioinformatician user : this.repository.getBioinformaticians()) {
            Files.copy(user.getAlignmentName().toPath(),
                    dir.toPath().resolve(user.getAlignmentName().toPath()),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(user.getSNPAlignmentName().toPath(),
                    dir.toPath().resolve(user.getSNPAlignmentName().toPath()),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(user.getScoreName().toPath(),
                    dir.toPath().resolve(user.getScoreName().toPath()),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // Adding the created version to the backups list and writing it to the file
        this.getBackups().add(version);
        this.writeBackUps();
        return version;
    }

    // A method for restoring a specific version of the repository's data
    public void restore(String version) throws IOException {
        // Checking if the given version exists in the backups list
        if (!this.backups.contains(version)) {
            throw new IllegalArgumentException("version doesn't exists");
        }

        File dir = new File(version);

        // Restoring the files for optimal alignment, SNP alignment, and score
        Files.copy(dir.toPath().resolve(this.repository.getOptimalAlignment().toPath()),
                this.repository.getOptimalAlignment().toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(dir.toPath().resolve(this.repository.getOptimalSNPAlignment().toPath()),
                this.repository.getOptimalSNPAlignment().toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(dir.toPath().resolve(this.repository.getOptimalScore().toPath()),
                this.repository.getOptimalScore().toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        // Restoring the files for each bioinformatician's alignment, SNP alignment, and score
        for (Bioinformatician user : this.repository.getBioinformaticians()) {
            Files.copy(dir.toPath().resolve(user.getAlignmentName().toPath()),
                    user.getAlignmentName().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(dir.toPath().resolve(user.getSNPAlignmentName().toPath()),
                    user.getSNPAlignmentName().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(dir.toPath().resolve(user.getScoreName().toPath()),
                    user.getScoreName().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // A method for cleaning up the repository
    public void clean() {
        this.repository.getOptimalAlignment().delete();
        this.repository.getOptimalSNPAlignment().delete();
        this.repository.getOptimalScore().delete();

        for (Bioinformatician user : this.repository.getBioinformaticians()) {
            user.getAlignmentName().delete();
            user.getSNPAlignmentName().delete();
            user.getScoreName().delete();
        }
    }
}
