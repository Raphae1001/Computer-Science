package smarticulous.db;

/**
 * A Smarticulous user.
 */
public class User {
    // Identifiant unique de l'utilisateur
    public int id;
    
    public String username;
    public String firstname;
    public String lastname;

    // The password is stored only in the database!

    public User(int id, String username, String firstname, String lastname) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String username, String firstname, String lastname) {
        this(-1, username, firstname, lastname); // -1 indique que l'ID n'est pas encore attribué
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getId() {
        return id;
    }
}