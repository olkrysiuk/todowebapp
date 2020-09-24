package com.example.todo.controllers;

import com.example.todo.entities.Role;
import com.example.todo.entities.User;
import com.example.todo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,
                          @RequestParam(required = false) String password,
                          @RequestParam(required = false) String passwordConfirmation,
                          Model model) {

        User userFromDb = userRepo.findByUsername(user.getUsername());

        if(userFromDb != null) {
            model.addAttribute("userExists", "User already exists");
            return "registration";
        }
        if(!password.equals(passwordConfirmation)) {
            model.addAttribute("passwordMismatch", "Password mismatch");
            return "registration";
        }

        if(password.length() < 6) {
            model.addAttribute("passwordMismatch", "Password too short");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
