package BioTeam;

import java.io.FileWriter;
import java.io.IOException;

public class Leader {
    private final Repository repository;

    public Leader(Repository repository) {
        this.repository = repository;
    }

    public void promoteUserAlignment(String name) throws UserNotFound {
        Alignment alignment = this.repository.getUser(name).getAlignment().copy();
        this.repository.setAlignment(alignment);
    }

    public void setUserAlignment(String name) throws UserNotFound {
        this.repository.getUser(name).setAlignment(this.repository.getAlignment().copy());
    }

    public void writeScore(FileWriter file) throws IOException, UserNotFound, EmptyAlignment {
        file.write(this.repository.getAlignment().getScore() + " (current optimal)\n");

        for (int i = 0; i < this.repository.getSize(); i++) {
            try {
                this.repository.getUser(i).writeScore(file, true);
            }
            catch (EmptyAlignment ignored) {}
        }
    }

    public void writeFASTA(FileWriter file) throws UserNotFound, IOException, EmptyAlignment {
        this.repository.getAlignment().writeFASTA(file, "Current Optimal");
        for (int i = 0; i < this.repository.getSize(); i++) {
            try {
                this.repository.getUser(i).writeFASTA(file, true);
            }
            catch (EmptyAlignment ignored) {}
        }
    }

    public void writeSNIP(FileWriter file) throws UserNotFound, IOException, EmptyAlignment {
        this.repository.getAlignment().writeSNIP(file, "Current Optimal");
        for (int i = 0; i < this.repository.getSize(); i++) {
            try {
                this.repository.getUser(i).writeSNIP(file, true);
            }
            catch (EmptyAlignment ignored) {}
        }
    }
}
