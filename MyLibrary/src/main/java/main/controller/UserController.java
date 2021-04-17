package main.controller;

import main.model.User;
import main.repository.UserRepository;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    public String getUser(
            @AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("user", user);
        //model.addAttribute("numberOfBooks", user.getBooks().size());
        return "users";
    }

    @PostMapping("/users/{userId}")
    public String userSave(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            Model model){
        if(userService.changeData(user, username, password)){
            model.addAttribute("message", "Data changed successfully");
        } else{
            model.addAttribute("message", "Data didn't change");
        }
        return "redirect:/users/{userId}";
    }
}
