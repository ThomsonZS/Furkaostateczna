
package wizut.tpsi.ogloszenia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wizut.tpsi.ogloszenia.services.UsersService;
import wizut.tpsi.ogloszenia.web.LoginForm;


public class UsersController {
    @Autowired
    UsersService usersService;
    @GetMapping("/login")
    public String login(Model model,LoginForm loginform) {
        
        return "login";
    }
}
