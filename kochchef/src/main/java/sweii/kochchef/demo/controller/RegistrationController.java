package sweii.kochchef.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.service.UserSecurityService;
import sweii.kochchef.demo.service.UserService;
import sweii.kochchef.demo.validator.RegistrationValidator;


@Controller
public class RegistrationController {

    public RegistrationController(UserSecurityService userSecurityService, RegistrationValidator validator, UserService userService) {
        this.userSecurityService = userSecurityService;
        this.validator = validator;
        this.userService = userService;
    }

    UserSecurityService userSecurityService;


    RegistrationValidator validator;

    UserService userService;



    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@ModelAttribute("user") User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(user);
        return "redirect:/";
    }


}
