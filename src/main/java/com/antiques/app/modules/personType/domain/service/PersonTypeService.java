package com.antiques.app.modules.personType.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antiques.app.modules.personType.domain.repository.PersonTypeRepository;
import com.antiques.app.modules.personType.persistence.PersonType;

import jakarta.transaction.Transactional;

@Service
public class PersonTypeService {
    @Autowired
    PersonTypeRepository personTypeRepository;
    
    @Transactional
    public Optional<PersonType> delete(Long id) {
        Optional<PersonType> optionalPersonType = this.personTypeRepository.findById(id);
        optionalPersonType.ifPresent(
            PersonTypeFound -> {
                this.personTypeRepository.delete(PersonTypeFound);
            }
        );
        return optionalPersonType;
    }
 
    public List<PersonType> findAll() {
        return (List<PersonType>) this.personTypeRepository.findAll();
    }

    public Optional<PersonType> findById(Long id) {
        return this.personTypeRepository.findById(id);
    }

    public PersonType save(PersonType PersonType) {
        return this.personTypeRepository.save(PersonType);
    }

    public Optional<PersonType> update(Long id, PersonType personType) {
        Optional<PersonType> optionalPersonType = this.personTypeRepository.findById(id);
        if (optionalPersonType.isPresent()) {
            PersonType personTypeItem = optionalPersonType.orElseThrow();
            return Optional.of(this.personTypeRepository.save(personTypeItem));
        }
        return optionalPersonType;
    }
}