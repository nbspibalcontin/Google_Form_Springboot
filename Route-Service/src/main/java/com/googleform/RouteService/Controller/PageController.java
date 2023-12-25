package com.googleform.RouteService.Controller;


import com.googleform.RouteService.Dto.FormDto;
import com.googleform.RouteService.Exception.WebClientException;
import com.googleform.RouteService.Service.Form_Service;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class PageController {
   private final Form_Service formService;

    public PageController(Form_Service formService) {
        this.formService = formService;
    }

    @GetMapping("/home")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/getQuestions/{code}")
    public ModelAndView getQuestions(@PathVariable String code, Model model) {
        try {
            List<FormDto> formDtoList = formService.getQuestions(code);

            // Add the list to the model
            model.addAttribute("formDtoList", formDtoList);

            // Specify the view name (replace "yourViewName" with your actual view name)
            return new ModelAndView("questionaire", model.asMap());

        } catch (WebClientException e) {
            // Handle WebClientException (or its subclasses) if needed
            model.addAttribute("error", e.getMessage());
            return new ModelAndView("notfound", model.asMap());
        }
    }
}
