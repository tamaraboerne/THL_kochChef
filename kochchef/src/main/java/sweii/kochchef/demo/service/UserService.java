package sweii.kochchef.demo.service;

import sweii.kochchef.demo.models.User;

public interface UserService {

    void save(User user);
    void update(User user);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

}
