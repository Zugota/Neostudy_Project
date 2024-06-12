package com.example.Neostudy_Project.deal.repo;

import com.example.Neostudy_Project.deal.models.Statement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatementRepository extends CrudRepository<Statement, UUID> {

}
