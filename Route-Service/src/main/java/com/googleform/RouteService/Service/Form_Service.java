package com.googleform.RouteService.Service;

import com.googleform.RouteService.Dto.FormDto;
import com.googleform.RouteService.Exception.WebClientException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class Form_Service {
    private final WebClient.Builder webClientBuilder;

    public Form_Service(WebClient.Builder webClientBuilder) {
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

        }catch (WebClientResponseException.InternalServerError e){
            throw new WebClientException(e.getResponseBodyAsString());
        }
    }
}
