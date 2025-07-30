package sweii.kochchef.demo.validator;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.service.UserService;
import org.slf4j.Logger;

import java.sql.Timestamp;

@Component
public class LoginValidator implements Validator {


    private static final Logger logger = LoggerFactory.getLogger(LoginValidator.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String usernameOrEmailUser = user.getUsername() == null ? user.getEmail() : user.getUsername();
        User userFromDatabase = userService.findByUsernameOrEmail(usernameOrEmailUser, usernameOrEmailUser);

        // Check if a user with this email oder username is existing
        if (userFromDatabase == null) {
            logger.debug("No user found for {}", usernameOrEmailUser);
        } else {
            // Check whether the password is wrong
            String passwordFromUser = userFromDatabase.getPassword();

            if (userFromDatabase.checkBlockedStatus() && (userFromDatabase.getBlockedDuration() / 60000) >= 2) {
                userFromDatabase.setBlocked(false);
                userFromDatabase.setBlockedSince(null);
                userFromDatabase.setLoginTries(0);
                logger.debug("Account open again for user {}", userFromDatabase.getUsername());
            }

            if (userFromDatabase.isBlocked()) {
                logger.debug("User {} is currently blocked, come back in {}", userFromDatabase.getUsername(), userFromDatabase.getRemainingTime());
                errors.rejectValue("passwordConfirm", "User.Blocked");
            } else {
                // Check whether the entered password is correct
                if (!bCryptPasswordEncoder.matches(user.getPassword(), passwordFromUser)) {
                    userFromDatabase.setLoginTries(userFromDatabase.getLoginTries() + 1);

                    logger.debug("Wrong password entered for user {}. Login tries: {}", userFromDatabase.getUsername(), userFromDatabase.getLoginTries());

                    if (userFromDatabase.getLoginTries() >= 3) {
                        userFromDatabase.setBlocked(true);
                        userFromDatabase.setBlockedSince(new Timestamp(System.currentTimeMillis()));
                        logger.debug("User {} blocked after 3 failed login attempts", userFromDatabase.getUsername());
                    }

                    errors.rejectValue("passwordConfirm", "User.Password.Wrong");
                } else {
                    userFromDatabase.setLoginTries(0);
                    logger.debug("Login tries reset for user {}", userFromDatabase.getUsername());
                }
            }
            userService.update(userFromDatabase);
        }
    }
}
