package sweii.kochchef.demo.service;

public interface UserSecurityService {

    String findLoggedInEmail();

    void autoLogin(String username, String password);

}
