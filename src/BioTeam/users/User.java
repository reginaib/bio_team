package BioTeam.users;

// An abstract class that represents a user within the bioinformatics team
public abstract class User implements UserInfo {
    private final String firstName;
    private final String lastName;
    private int experience;

    // Constructor to initialize a user with first name and last name
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = 0;
    }

    // Constructor to initialize a user with first name, last name, and experience
    public User(String firstName, String lastName, int experience) {
        this.firstName = firstName;
        this.lastName = lastName;

        // Check if experience is negative and throw an exception if true
        if (experience < 0) {
            throw new IllegalArgumentException("Negative experience");
        }
        this.experience = experience;
    }

    // Getter the user's name and experience
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public Integer getExperience() {
        return this.experience;
    }

    // Set the user's years of experience, ensuring it's not negative
    public void setExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Negative experience");
        }
        this.experience = experience;
    }

    // Override the equals method to compare users based on their full names
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.getFullName().equals(((User) obj).getFullName());
    }

    // Override the hashCode method based on the user's full name
    @Override
    public int hashCode() {
        return this.getFullName().hashCode();
    }
}
