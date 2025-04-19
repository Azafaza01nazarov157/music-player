package org.example.musicplayer.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RegistrationViewController {

    @GetMapping("/complete-registration/{uuid}")
    public String completeRegistration(@PathVariable("uuid") String uuid, Model model) {
        model.addAttribute("registrationUuid", uuid);
        
        return "complete-registration";
    }
}