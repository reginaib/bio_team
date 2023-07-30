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

    // A constructor to create a technical support instance with first and last name
    public TechnicalSupport(String firstName, String lastName) {
        super(firstName, lastName);
    }

    // A constructor to create a technical support instance with first name, last name and years of experience
    public TechnicalSupport(String firstName, String lastName, int experience) {
        super(firstName, lastName, experience);
    }

    // A method for reading the list of backups from a file in the repository
    private void readBackUps(Repository repository) throws IOException {
        this.backups = new ArrayList<>();

        try (FileReader fileReader = new FileReader(repository.getBackUpList())) {
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
    private void writeBackUps(Repository repository) throws IOException {
        try (FileWriter fileWriter = new FileWriter(repository.getBackUpList())) {
            // Writing each version from the backups list to the file
            for (String version : this.backups) {
                fileWriter.write(version + '\n');
            }
        }
    }

    // A method for creating a backup of the repository's data
    public void backUp(Repository repository) throws IOException {
        // If backups list is not read yet, read it from the file
        if (this.backups == null){
            this.readBackUps(repository);
        }

        // Creating a timestamp to be used as the version of the backup
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        String version = dateFormat.format(date);

        // Creating copies of the files for optimal alignment, SNP alignment, and score
        Files.copy(repository.getOptimalAlignment().toPath(),
                Path.of(repository.getOptimalAlignment().toString() + version),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(repository.getOptimalSNPAlignment().toPath(),
                Path.of(repository.getOptimalSNPAlignment().toString() + version),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(repository.getOptimalScore().toPath(),
                Path.of(repository.getOptimalScore().toString() + version),
                StandardCopyOption.REPLACE_EXISTING);

        // Creating copies of the files for each bioinformatician's alignment, SNP alignment, and score
        for (Bioinformatician user : repository.getBioinformaticians()) {
            Files.copy(Path.of(user.getFullName() + ".alignment.txt"),
                    Path.of(user.getFullName() + ".alignment.txt" + version),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(user.getFullName() + ".snp_alignment.txt"),
                    Path.of(user.getFullName() + ".snp_alignment.txt" + version),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(user.getFullName() + ".score.txt"),
                    Path.of(user.getFullName() + ".score.txt" + version),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // Adding the created version to the backups list and writing it to the file
        this.backups.add(version);
        this.writeBackUps(repository);
    }

    // A method for restoring a specific version of the repository's data
    public void restore(Repository repository, String version) throws IOException {
        // If backups list is not read yet, read it from the file
        if (this.backups == null){
            this.readBackUps(repository);
        }

        // Checking if the given version exists in the backups list
        if (this.backups.contains(version)) {
            throw new IllegalArgumentException("version doesn't exists");
        }

        // Restoring the files for optimal alignment, SNP alignment, and score
        Files.copy(Path.of(repository.getOptimalAlignment().toString() + version),
                repository.getOptimalAlignment().toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Path.of(repository.getOptimalSNPAlignment().toString() + version),
                repository.getOptimalSNPAlignment().toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Path.of(repository.getOptimalScore().toString() + version), repository.getOptimalScore().toPath(),

                StandardCopyOption.REPLACE_EXISTING);

        // Restoring the files for each bioinformatician's alignment, SNP alignment, and score
        for (Bioinformatician user : repository.getBioinformaticians()) {
            Files.copy(Path.of(user.getFullName() + ".alignment.txt" + version), Path.of(user.getFullName() + ".alignment.txt"),

                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(user.getFullName() + ".snp_alignment.txt" + version), Path.of(user.getFullName() + ".snp_alignment.txt"),

                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(user.getFullName() + ".score.txt" + version), Path.of(user.getFullName() + ".score.txt"),

                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // A method for cleaning up the repository
    public void clean(Repository repository) {
        repository.getOptimalAlignment().delete();
        repository.getOptimalSNPAlignment().delete();
        repository.getOptimalScore().delete();

        for (Bioinformatician user : repository.getBioinformaticians()) {
            new File(user.getFullName() + ".alignment.txt").delete();
            new File(user.getFullName() + ".snp_alignment.txt").delete();
            new File(user.getFullName() + ".score.txt").delete();
        }
    }
}
