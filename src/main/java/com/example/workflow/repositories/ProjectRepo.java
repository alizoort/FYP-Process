package com.example.workflow.repositories;

import com.example.workflow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends CrudRepository<Project,Integer> {

}
