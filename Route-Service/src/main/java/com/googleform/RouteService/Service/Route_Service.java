package com.googleform.RouteService.Service;

import com.googleform.RouteService.Dto.FormDto;
import com.googleform.RouteService.Exception.WebClientException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class Route_Service {
    private final WebClient.Builder webClientBuilder;

    public Route_Service(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    String FORM_URL = "http://localhost:8081/api/form";

    public List<FormDto> getQuestions(String code) {
        try{
            return webClientBuilder.build()
                    .get()
                    .uri(FORM_URL + "/code/{code}", code)
                    .retrieve()
                    .bodyToFlux(FormDto.class) // Use bodyToFlux for handling arrays
                    .collectList() // Collect the array elements into a List
                    .block();

        }catch (WebClientResponseException.NotFound e){
            throw new WebClientException(e.getResponseBodyAsString());
        }
    }

    //TODO create validation in response html

    //ADMIN

    //TODO create a CRUD
    //TODO DESIGN HTML FILES
}
