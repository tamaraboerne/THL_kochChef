package sweii.kochchef.demo.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.security.CommonPasswordChecker;
import sweii.kochchef.demo.service.UserService;


@Component
public class RegistrationValidator implements Validator {

    private final UserService userService;

    final CommonPasswordChecker commonPasswordChecker = new CommonPasswordChecker();

    public RegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String username = "username";
        String password = "password";


        //Requirements for BN
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, username, "NotEmpty");
        if (user.getUsername().length() < 2 || user.getUsername().length() > 31) {
            errors.rejectValue(username, "Size.userForm.username");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue(username, "Duplicate.userForm.username");
        }

        //Requirements for email
        if (user.getEmail().length() > 254) {
            errors.rejectValue("email", "Size.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, password, "NotEmpty");

        if (user.getPassword().length() < 12 || user.getPassword().length() > 100) {
            errors.rejectValue(password, "Size.userForm.password");
        }
        if (!user.getPassword().matches(".*\\d.*")) {
            errors.rejectValue(password, "Password.NoNumber",
                    "Das Passwort muss mindestens eine Zahl enthalten.");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }

        if (commonPasswordChecker.isCommonPassword(user.getPassword())) {
            errors.rejectValue("password", "Password.Common",
                    "Das Passwort darf kein häufig verwendetes Passwort sein.");
        }


    }

}
