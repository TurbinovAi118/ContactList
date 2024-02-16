package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("contacts", contactService.findAll());

        return "index";
    }

    @GetMapping("/contact/create")
    public String showCreateForm(Model model){
        model.addAttribute("contacts", new Contact());

        return "fragments/сreate :: create";
    }

    @PostMapping("/contact/create")
    public String createContact(@ModelAttribute Contact contact){

        contactService.save(contact);

        return "redirect:/";
    }

    @GetMapping("/contact/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        Contact contact = contactService.findById(id);
        if (contact != null) {
            model.addAttribute("contacts", contact);
            return "fragments/update :: update";
        }

        return "redirect:/";
    }

    @PostMapping("/contact/edit")
    public String updateContact(@ModelAttribute Contact contact){

        contactService.update(contact);

        return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String updateContact(@PathVariable Long id){

        contactService.deleteById(id);

        return "redirect:/";
    }

}
