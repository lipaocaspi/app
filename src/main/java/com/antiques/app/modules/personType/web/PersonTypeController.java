package com.antiques.app.modules.personType.web;

import java.util.HashMap;
import java.util.List;
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

import com.antiques.app.modules.personType.domain.service.PersonTypeService;
import com.antiques.app.modules.personType.persistence.PersonType;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/person-types")
public class PersonTypeController {
    @Autowired
    private PersonTypeService personTypeService;

    @GetMapping
    //@PreAuthorize("hasPersonType('ADMIN')")
    public List<PersonType> listPersonType(){
        return this.personTypeService.findAll();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasPersonType('ADMIN')")
    public ResponseEntity<PersonType> view(@PathVariable Long id){
        Optional<PersonType> optionalPersonType  = personTypeService.findById(id);
        if (optionalPersonType.isPresent()){
            return ResponseEntity.ok(optionalPersonType.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    //@PreAuthorize("hasPersonType('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody PersonType personType, BindingResult result){
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(personTypeService.save(personType));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasPersonType('ADMIN')")
    public ResponseEntity<PersonType> update(@PathVariable Long id, @Valid @RequestBody PersonType personType){
        Optional<PersonType> personTypeOptional = this.personTypeService.update(id, personType);
        if (personTypeOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(personTypeOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasPersonType('ADMIN')")
    public ResponseEntity<PersonType> delete(@PathVariable Long id){
        //PersonType personType = new PersonType();
        //personType.setId(id);
        Optional<PersonType> optionalPersonType = this.personTypeService.delete(id);
        if (optionalPersonType.isPresent()){
            return ResponseEntity.ok(optionalPersonType.orElseThrow());
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