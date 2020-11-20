package com.example.distribute.repository;

import com.example.distribute.model.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {
    Optional<Distribution> findFirstByToken(String token);
}
