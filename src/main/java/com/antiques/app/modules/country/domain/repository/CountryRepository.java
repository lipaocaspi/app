package com.antiques.app.modules.country.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.antiques.app.modules.country.persistence.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
}