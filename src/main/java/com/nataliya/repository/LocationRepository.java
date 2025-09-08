package com.nataliya.repository;

import com.nataliya.model.Location;
import com.nataliya.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    int countByUser(User user);

    boolean existsByUserAndLatitudeAndLongitude(User user, BigDecimal latitude, BigDecimal longitude);
}
