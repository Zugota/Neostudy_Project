package com.example.Neostudy_Project.deal.repo;

import com.example.Neostudy_Project.deal.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends CrudRepository<Client, UUID> {

}
