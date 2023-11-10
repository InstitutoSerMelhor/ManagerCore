package com.institutosermelhor.ManagerCore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.institutosermelhor.ManagerCore.controller.Dtos.PersonCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.PersonDto;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.Person;
import com.institutosermelhor.ManagerCore.service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController {

  PersonService service;

  @Autowired
  public PersonController(PersonService service) {
    this.service = service;
  }

  @GetMapping
  public List<Person> getUsers() {
    return service.getUsers();
  }

  @PostMapping()
  public ResponseEntity<PersonDto> create(@RequestBody PersonCreationDto personData) {
    Person person = Person.builder().username(personData.username()).email(personData.email())
        .password(personData.password()).role(Role.USER).build();

    Person newPerson = service.create(person);

    PersonDto personDto =
        new PersonDto(newPerson.getUsername(), newPerson.getEmail(), newPerson.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
  }
}
