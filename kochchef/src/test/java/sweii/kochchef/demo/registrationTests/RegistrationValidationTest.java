package sweii.kochchef.demo.registrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.validator.RegistrationValidator;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationValidationTest {


    @Autowired
    private RegistrationValidator validator;

    @Test
    void testValidUser() {
        User user = new User();
        user.setUsername("ValidUser");
        user.setEmail("valid@example.com");
        user.setPassword("ValidPassword123");
        user.setPasswordConfirm("ValidPassword123");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testInvalidUsername() {
        User user = new User();
        user.setUsername(""); // Invalid, empty username
        user.setEmail("invalid@example.com");
        user.setPassword("ValidPassword123");
        user.setPasswordConfirm("ValidPassword123");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("username"));
        assertEquals("NotEmpty", Objects.requireNonNull(errors
                .getFieldError("username")).getCode());
    }

    @Test
    void testInvalidPassword() {
        User user = new User();
        user.setUsername("ValidUser");
        user.setEmail("valid@example.com");
        user.setPassword("short"); // Invalid, too short
        user.setPasswordConfirm("short");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("password"));
        assertEquals("Size.userForm.password", Objects.requireNonNull(errors
                .getFieldError("password")).getCode());
    }

    @Test
    void testNonMatchingPasswords() {
        User user = new User();
        user.setUsername("ValidUser");
        user.setEmail("valid@example.com");
        user.setPassword("ValidPassword123");
        user.setPasswordConfirm("DifferentPassword"); // Invalid, passwords don't match

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("passwordConfirm"));
        assertEquals("Diff.userForm.passwordConfirm", Objects.requireNonNull(errors
                .getFieldError("passwordConfirm")).getCode());
    }

    @Test
    void testValidCommonPW() {
        User user = new User();
        user.setUsername("ValidUser");
        user.setEmail("valid@example.com");
        user.setPassword("Minecraft");
        user.setPasswordConfirm("Minecraft");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    void testInvalidToLongPW() {
        User user = new User();
        user.setUsername("ValidUser");
        user.setEmail("valid@example.com");
        user.setPassword("shortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshort" +
                "shortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshort"); // Invalid, too long
        user.setPasswordConfirm("shortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshort"+
                "shortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshortshort");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }
}
