package com.institutosermelhor.ManagerCore.infra.config;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "myMigrationChangeUnitId", order = "1", author = "mongock_test",
    systemVersion = "1")
public class MyMigrationChangeUnit {
  private final MongoTemplate template;

  public final String USER_COLLECTION_NAME = "user";

  public MyMigrationChangeUnit(MongoTemplate template) {
    this.template = template;
  }

  @BeforeExecution
  public void before() {
    template.createCollection(USER_COLLECTION_NAME);
  }

  @RollbackBeforeExecution
  public void rollbackBefore() {
    template.dropCollection(USER_COLLECTION_NAME);
  }

  @Execution
  public void migrationMethod() {
    String passwordHashed = new BCryptPasswordEncoder().encode("123456");

    template.save(
        new User(null, "Abner", "abn@gmail.com", passwordHashed, Role.ADMIN, true, null, null),
        USER_COLLECTION_NAME);
  }

  @RollbackExecution
  public void rollback() {
    template.remove(new Document());
  }
}
