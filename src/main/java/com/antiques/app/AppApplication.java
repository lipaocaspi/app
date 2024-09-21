package com.antiques.app;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.antiques.app.modules.person.domain.repository.PersonRepository;
import com.antiques.app.modules.person.persistence.Person;
import com.antiques.app.modules.personType.domain.repository.PersonTypeRepository;
import com.antiques.app.modules.personType.persistence.PersonType;
import com.antiques.app.modules.personType.persistence.PersonTypeEnum;

@SpringBootApplication
public class AppApplication {
	@Autowired
	PasswordEncoder encoder;

	@Autowired 
	PersonRepository personRepository;

	@Autowired 
	PersonTypeRepository personTypeRepository;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(){

		return args -> {
			PersonType personType = new PersonType();
			personType.setNamePersonType(PersonTypeEnum.ADMIN);

			Person adminPerson = new Person();
			adminPerson.setUsername("lilipao");
			adminPerson.setPassword(encoder.encode("1234"));
			adminPerson.setPersonTypes(Set.of(personType));
			Set<PersonType> personTypes = adminPerson.getPersonTypes();

			System.out.println("HHHHHHHHHHHEYYYYYYYYYYYYYY ----> ");
			for (PersonType pType : personTypes) {
				System.out.println("Role: " + pType.getNamePersonType());
		}
		personRepository.save(adminPerson);

			/*Role role2 = new Role();
			role2.setName(
				RoleEnum.USER
			);	
			Account testAccount2 = new Account();
			testAccount2.setUsername("sofia");
			testAccount2.setPassword(encoder.encode("12345"));
			testAccount2.setRoles(Set.of(role2));
	
			accountRepository.save(testAccount2); */
		};
	}
}