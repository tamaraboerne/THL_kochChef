package sweii.kochchef.demo.registrationTests;

import org.junit.jupiter.api.Test;
import sweii.kochchef.demo.security.CommonPasswordChecker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonPasswordTest {

    @Test
    void testIsCommonPassword() {
        CommonPasswordChecker passwordChecker = new CommonPasswordChecker();

        assertTrue(passwordChecker.isCommonPassword("123456")); // common PW
        assertFalse(passwordChecker.isCommonPassword("Minecraft")); // not common PW
        assertFalse(passwordChecker.isCommonPassword("loadCommonPasswords")); // not Common PW
    }
}
