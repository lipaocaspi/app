package com.antiques.app.config.security;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.antiques.app.modules.person.domain.repository.PersonRepository;
import com.antiques.app.modules.person.persistence.Person;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        Optional<Person> person = personRepository.findByUsername(userName);

        if (person.isEmpty()){
            throw(new UsernameNotFoundException("El usuario con el usuario ingresado no existe"));  
        }

        Person foundPerson = person.get();
        
        Collection<? extends GrantedAuthority> authorities = foundPerson.getPersonTypes()
            .stream()
            .map(personType -> new SimpleGrantedAuthority("ROLE_".concat(personType.getNamePersonType().name())))
            .collect(Collectors.toSet());

        return new User(
            foundPerson.getUsername(),
            foundPerson.getPassword(),
            true,
            true,
            true,
            true,
            authorities
        );
    }
}