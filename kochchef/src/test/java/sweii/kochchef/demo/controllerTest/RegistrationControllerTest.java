package sweii.kochchef.demo.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import sweii.kochchef.demo.controller.RegistrationController;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.service.UserService;
import sweii.kochchef.demo.validator.RegistrationValidator;

class RegistrationControllerTest {


    @Mock
    private RegistrationValidator validator;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;


    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    /* the test can only be executed if the controller has a @RestController annotation.
    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }
    */

    @Test
    void testProcessRegistration_withErrors() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/registration")
                        .flashAttr("user", new User()))
                .andExpect(view().name("redirect:/"));

        verify(validator).validate(any(User.class), any(BindingResult.class));
    }

    @Test
    void testProcessRegistration_withoutErrors() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/registration")
                        .flashAttr("user", new User()))
                .andExpect(redirectedUrl("/"));

        verify(validator).validate(any(User.class), any(BindingResult.class));
        verify(userService).save(any(User.class));
    }
}
