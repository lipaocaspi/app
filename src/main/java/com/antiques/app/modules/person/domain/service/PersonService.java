package com.antiques.app.modules.person.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.antiques.app.modules.person.domain.dto.PersonDto;
import com.antiques.app.modules.person.domain.repository.PersonRepository;
import com.antiques.app.modules.person.persistence.Person;

import jakarta.transaction.Transactional;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Transactional
    public Optional<Person> delete(Long id) {
        Optional<Person> optionalPerson = this.personRepository.findById(id);
        optionalPerson.ifPresent(
            PersonFound -> {
                this.personRepository.delete(PersonFound);
            }
        );
        return optionalPerson;
    }
 
    public List<Person> findAll() {
        return (List<Person>) this.personRepository.findAll();
    }

    public Person toEntity(PersonDto dto){
        return Person.builder()
            .username(dto.getUsername())
            .password( passwordEncoder.encode(dto.getPassword() ) )
            .personTypes(dto.getPersonTypes())
            .build();
    }

    public Optional<Person> findById(Long id) {
        return this.personRepository.findById(id);
    }

    public Person save(PersonDto dto) {
        Person person = this.toEntity(dto);
        return this.personRepository.save(person);
    }

    public Optional<Person> update(Long id, Person person) {
        Optional<Person> optionalPerson = this.personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person personItem = optionalPerson.orElseThrow();
            return Optional.of(this.personRepository.save(personItem));
        }
        return optionalPerson;
    }
}