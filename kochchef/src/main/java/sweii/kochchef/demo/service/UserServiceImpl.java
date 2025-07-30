package sweii.kochchef.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sweii.kochchef.demo.models.Role;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.repositories.RoleRepository;
import sweii.kochchef.demo.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;


    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Lazy
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        logger.debug("Encoded password for user {}: {}", user.getUsername(), user.getPassword());

        Role userRole = roleRepository.findByName("USER");
        logger.debug("Found role for user {}: {}", user.getUsername(), userRole.getName());

        user.setRole(userRole);
        user.setBlocked(false);
        user.setLoginTries(0);
        user.setBlockedSince(null);

        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        logger.debug("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) {
        logger.debug("Searching for user by username: {} or email: {}", username, email);
        return userRepository.findUserByUsernameOrEmail(username, email);
    }
}
