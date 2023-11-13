package com.institutosermelhor.ManagerCore.models.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.institutosermelhor.ManagerCore.models.entity.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
}
