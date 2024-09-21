package com.antiques.app.modules.person.domain.dto;

import java.util.Set;

import com.antiques.app.modules.personType.persistence.PersonType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    @NotBlank (message = "username must not be empty")
    private String username;

    @NotBlank( message =  "password must not be empty")
    private String password;

    private Set<PersonType> personTypes;
}