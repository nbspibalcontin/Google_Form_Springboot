package com.googleform.ResponseService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;
    private String response;
    @OneToOne
    @JoinColumn(name = "question_id")
    private Questions questions;
    @ManyToOne
    @JoinColumn(name = "respondents_id")
    private Respondents respondents;
}
