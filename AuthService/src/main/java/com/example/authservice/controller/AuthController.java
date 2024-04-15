package com.example.authservice.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final UserRepository userRepository;

    private final String gatewayBaseUrl;

    public AuthController(UserRepository userRepository,
            @Value("${gateway.uri}") String gatewayUri,
            @Value("${gateway.port}") int gatewayPort) {
        this.userRepository = userRepository;
        this.gatewayBaseUrl = String.format("http://%s:%d", gatewayUri, gatewayPort);
    }

    @GetMapping(value = { "/login" })
    public String login(Model model, HttpSession session) {
        User user = new User();

        if (session.getAttribute("user") != null)
            return "redirect:/index";

        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping(value = { "/login" })
    public ModelAndView handleLogin(@ModelAttribute("user") User user, HttpSession httpSession) {
        logger.info("User: " + user);
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if (userOptional.isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("user", user);
            modelAndView.addObject("error", true);

            return modelAndView;
        }

        httpSession.setAttribute("user", userOptional.get().getEmail());
        logger.info("Session: " + httpSession.getAttribute("user"));

        // String redirectUrl = this.gatewayBaseUrl + "/blog/index";
        return new ModelAndView("redirect:http://localhost:8888/blog/index");
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();

        return "redirect:http://localhost:8888/blog/index";
    }

}
