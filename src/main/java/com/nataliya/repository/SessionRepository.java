package com.nataliya.repository;

import com.nataliya.model.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :now")  //not necessary to write
    void deleteSessionByExpiresAtBefore(@Param("now") LocalDateTime now);
}
