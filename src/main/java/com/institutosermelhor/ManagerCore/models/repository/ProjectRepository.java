package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

}
