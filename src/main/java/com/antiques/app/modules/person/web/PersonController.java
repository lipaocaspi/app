package com.antiques.app.modules.person.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antiques.app.modules.person.domain.dto.PersonDto;
import com.antiques.app.modules.person.domain.service.PersonService;
import com.antiques.app.modules.person.persistence.Person;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> view(@PathVariable Long id){
        Optional<Person> optionalPerson  = personService.findById(id);
        if (optionalPerson.isPresent()){
            return ResponseEntity.ok(optionalPerson.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody PersonDto person, BindingResult result){
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(person));
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person person){
        Optional<Person> personOptional = this.personService.update(id, person);
        if (personOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(personOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> delete(@PathVariable Long id){
        //Person person = new Person();
        //person.setId(id);
        Optional<Person> optionalPerson = this.personService.delete(id);
        if (optionalPerson.isPresent()){
            return ResponseEntity.ok(optionalPerson.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}