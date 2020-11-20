package com.example.distribute.repository;

import com.example.distribute.model.entity.DistributionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionStateRepository extends JpaRepository<DistributionState, Long> {
}
