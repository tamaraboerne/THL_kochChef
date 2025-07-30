package sweii.kochchef.demo.registrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.repositories.UserRepository;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public TestDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Füge hier Testdaten ein
        createUser("test1@example.com", "TestUser1", "password1");
        createUser("test2@example.com", "TestUser2", "password2");
        createUser("test3@example.com", "TestUser3", "password3");
    }

    private void createUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }
}