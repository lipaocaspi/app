package com.antiques.app.modules.country.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.antiques.app.modules.country.persistence.Country;
import com.antiques.app.modules.country.domain.repository.CountryRepository;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;
    
    @Transactional
    public Optional<Country> delete(Long id) {
        Optional<Country> optionalCountry = this.countryRepository.findById(id);
        optionalCountry.ifPresent(
            CountryFound -> {
                this.countryRepository.delete(CountryFound);
            }
        );
        return optionalCountry;
    }
 
    public List<Country> findAll() {
        return (List<Country>) this.countryRepository.findAll();
    }

    public Optional<Country> findById(Long id) {
        return this.countryRepository.findById(id);
    }

    public Country save(Country Country) {
        return this.countryRepository.save(Country);
    }

    public Optional<Country> update(Long id, Country country) {
        Optional<Country> optionalCountry = this.countryRepository.findById(id);
        if (optionalCountry.isPresent()) {
            Country countryItem = optionalCountry.orElseThrow();
            //SETS
            
            return Optional.of(this.countryRepository.save(countryItem));
        }
        return optionalCountry;
    }
}