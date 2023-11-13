package com.institutosermelhor.ManagerCore.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.models.entity.Person;
import com.institutosermelhor.ManagerCore.models.repository.PersonRepository;

@Service
public class PersonService {

  PersonRepository repository;

  @Autowired
  public PersonService(PersonRepository repository) {
    this.repository = repository;
  }

  public Person create(Person person) {
    String hashedPassword = new BCryptPasswordEncoder().encode(person.getPassword());
    person.setPassword(hashedPassword);
    return repository.save(person);
  }

  public List<Person> getUsers() {
    return repository.findAll();
  }

  public Person findById(String personId) {
    Person personExists =
        repository.findById(personId).orElseThrow(() -> new NotFoundException("User not found!"));

    return personExists;
  }

  public void delete(String personId) {
    Person person = findById(personId);
    person.setDeleted(true);
    repository.save(person);
  }
}
