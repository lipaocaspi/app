package com.antiques.app.modules.personType.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.antiques.app.modules.personType.persistence.PersonType;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Long>{

}