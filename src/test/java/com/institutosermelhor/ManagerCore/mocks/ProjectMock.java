package com.institutosermelhor.ManagerCore.mocks;

import com.institutosermelhor.ManagerCore.models.entity.Project;
import lombok.Data;

@Data
public class ProjectMock {
    public Project giveMeAProject() {
        return new Project(
                "validId123",
                "Project 1",
                "Random project",
                "Development",
                null,
                null,
                true);
    }
}
