package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);

  List<User> findByIsEnabledTrue();
}
