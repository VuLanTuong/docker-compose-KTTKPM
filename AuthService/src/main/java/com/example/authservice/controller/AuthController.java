package com.example.authservice.controller;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping( "/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"/login"})
    public String login(Model model, HttpSession session) {
        User user = new User();

        if (session.getAttribute("user") != null)
            return "redirect:/index";

        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping(value = {"/login"})
    public String handleLogin(@ModelAttribute("user") User user, Model model, HttpSession httpSession) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if (userOptional.isEmpty() || !BCrypt.verifyer().verify(user.getPasswordHash().getBytes(), userOptional.get().getPasswordHash().getBytes()).verified) {
            model.addAttribute("user", user);
            model.addAttribute("error", true);

            return "login";
        }

        httpSession.setAttribute("user", userOptional.get());

        return "redirect:/index";
    }



    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();

        return "redirect:index";
    }




}
