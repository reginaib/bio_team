package BioTeam.users;

public abstract class User implements UserInfo {
    private final String firstName;
    private final String lastName;
    private int experience;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = 0;
    }

    public User(String firstName, String lastName, int experience) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (experience < 0) {
            throw new IllegalArgumentException("Negative experience");
        }
        this.experience = experience;
    }

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

    public void setExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Negative experience");
        }
        this.experience = experience;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.getFullName().equals(((User) obj).getFullName());
    }

    @Override
    public int hashCode() {
        return this.getFullName().hashCode();
    }
}
