package com.googleform.FormService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respondents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
    @OneToMany(mappedBy = "respondents", cascade = CascadeType.ALL)
    private List<Response> responses = new ArrayList<>();
}
