package com.googleform.RouteService.Controller;


import com.googleform.RouteService.Dto.FormDto;
import com.googleform.RouteService.Dto.FormWithResponseDto;
import com.googleform.RouteService.Exception.FormNotFoundException;
import com.googleform.RouteService.Service.Route_Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class RouteController {
    private final Route_Service formService;

    public RouteController(Route_Service formService) {
        this.formService = formService;
    }

    //Create form
    @GetMapping("/create-question")
    public ModelAndView createQuestions() {
        return new ModelAndView("questionaire");
    }

    //Create response
    @GetMapping("/getQuestions/{code}")
    public ModelAndView getQuestions(@PathVariable String code, Model model) {
        try {
            List<FormDto> formDtoList = formService.getQuestions(code);

            // Add the list to the model
            model.addAttribute("formDtoList", formDtoList);

            // Specify the view name (replace "yourViewName" with your actual view name)
            return new ModelAndView("response", model.asMap());

        } catch (FormNotFoundException e) {
            model.addAttribute("notFound", e.getMessage());
            return new ModelAndView("notFound", model.asMap());
        } catch (Exception e) {
            // Handle other exceptions or log them if needed
            model.addAttribute("errorMessage", "An error occurred while processing your request.");
            return new ModelAndView("error", model.asMap());
        }
    }

    @GetMapping("/admin/display-form/{code}")
    public ModelAndView displayFormResponse(@PathVariable String code) {
        ModelAndView modelAndView = new ModelAndView("displayForm");

        try {
            List<FormWithResponseDto> formResponses = formService.displayQuestionAndResponse(code);
            modelAndView.addObject("formResponses", formResponses);
        } catch (Exception e) {
            modelAndView.addObject("error", "An error occurred: " + e.getMessage());
            // You can set an error view here if needed
            // modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @GetMapping("/admin/editResponse")
    public ModelAndView editDisplayForm() {
        return new ModelAndView("editdisplayForm");
    }
}
