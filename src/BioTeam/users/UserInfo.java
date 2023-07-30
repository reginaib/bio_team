package BioTeam.users;

// An interface for providing a user information
public interface UserInfo {
    // A method for getting the first name of the user
    public String getFirstName();

    // A method for getting the last name of the user
    public String getLastName();

    // A method for getting the full name of the user
    public String getFullName();

    // A method for getting the years of experience of the user
    public Integer getExperience();

    // A method for setting the years of experience of the user
    public void setExperience(int experience);
}
