package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.controller.Dtos.PersonCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.PersonDto;
import com.institutosermelhor.ManagerCore.models.entity.Person;
import com.institutosermelhor.ManagerCore.security.Role;
import com.institutosermelhor.ManagerCore.service.PersonService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

  PersonService service;

  @Autowired
  public PersonController(PersonService service) {
    this.service = service;
  }

  @PostMapping()
  public ResponseEntity<PersonDto> create(@RequestBody PersonCreationDto personData) {
    Person person = new Person(personData.username(), personData.email(), personData.password(),
        Role.USER,
        new Date(), new Date());
    Person newPerson = service.create(person);
    PersonDto personDto = new PersonDto(newPerson.getUsername(), newPerson.getEmail(),
        newPerson.getRole());
    return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
  }
}
