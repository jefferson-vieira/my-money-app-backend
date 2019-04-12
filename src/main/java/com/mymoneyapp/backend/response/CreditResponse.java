package com.mymoneyapp.backend.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditResponse {

    private Long id;

    private String description;

    private Double value;

    private LocalDate date;

}
