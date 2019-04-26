package com.mymoneyapp.backend.response;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;

    private String postalCode;

    private String street;

    private String number;

    private String district;

    private String city;

    private String state;

}
