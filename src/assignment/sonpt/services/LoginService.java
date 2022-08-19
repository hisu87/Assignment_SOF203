package assignment.sonpt.services;

import assignment.sonpt.models.User;
import assignment.sonpt.repositories.LoginRepository;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author sonpt_ph19600
 */
public class LoginService{
    private LoginRepository repository;

    public LoginService() {
        this.repository = new LoginRepository();
    }
    

    public Integer login(String username, String password){
        User user = repository.findByUsernameAndPassword(username, password);
        if (Objects.isNull(user)) {
            return -1;
        }
        if (user.getRole() == 0) { //Tài khoản đào tạo
            return 0;
        } else { //Tài khoản giảng viên
            return 1;
        }
    }
}
