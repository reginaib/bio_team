package BioTeam.users;

import BioTeam.repository.Repository;

// An interface for providing access to repository
public interface RepoAccessible {
    public void setRepository(Repository repository);
}
