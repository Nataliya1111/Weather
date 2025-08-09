package com.nataliya.repository;

import com.nataliya.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {
}
