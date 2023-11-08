package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {}
