package sweii.kochchef.demo.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "KOCHCHEF_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();

    @Transient
    private String passwordConfirm;
    @ManyToOne
    @JoinColumn(name = "ROLE")
    private Role role;

    @Column(name = "BLOCKED")
    private boolean blocked;

    @Column(name = "BLOCKED_SINCE")
    private Timestamp blockedSince;

    @Column(name = "LOGIN_TRIES")
    private int loginTries;


    // Getter und Setter für id, username, E-Mail und password

    // Getter und Setter für id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter und Setter für username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter und Setter für E-Mail
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter und Setter für password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter und Setter für passwordConfirm
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Timestamp getBlockedSince() {
        return blockedSince;
    }

    public void setBlockedSince(Timestamp blockedSince) {
        this.blockedSince = blockedSince;
    }

    public int getLoginTries() {
        return loginTries;
    }

    public void setLoginTries(int loginTries) {
        this.loginTries = loginTries;
    }

    public long getBlockedDuration() {
        if (blockedSince != null) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            return currentTimestamp.getTime() - blockedSince.getTime();
        } else {
            return 0;
        }
    }

    public String getRemainingTime() {
        if (blockedSince != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long blockedTimeMillis = blockedSince.getTime();
            long elapsedTimeMillis = currentTimeMillis - blockedTimeMillis;

            long totalSeconds = elapsedTimeMillis / 1000;
            long remainingSeconds = 120 - totalSeconds;

            if (remainingSeconds < 0) {
                return "0:00";
            }

            long remainingMinutes = remainingSeconds / 60;
            remainingSeconds = remainingSeconds % 60;

            return String.format("%02d:%02d", remainingMinutes, remainingSeconds);
        } else {
            return "0:00";
        }
    }

    public boolean checkBlockedStatus() {
        if (blockedSince != null) {
            long duration = getBlockedDuration();
            return duration > 0;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return getEmail() +  " " + getRole() + " " + getUsername() + " " + getPassword() + " " + getId();
    }
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void addRecipes(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public void removeRecipes(Recipe recipe) {
        this.recipes.remove(recipe);
    }
}
