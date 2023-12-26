package com.googleform.ResponseService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
    @OneToOne(mappedBy = "questions", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Response response;
}
