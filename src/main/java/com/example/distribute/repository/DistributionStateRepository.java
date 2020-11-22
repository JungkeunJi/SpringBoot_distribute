package com.example.distribute.repository;

import com.example.distribute.model.entity.DistributionState;
import com.example.distribute.model.enumclass.AllocatedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributionStateRepository extends JpaRepository<DistributionState, Long> {
    Optional<DistributionState> findFirstByTokenAndAllocatedUserId(String token, Integer userId);

    Optional<DistributionState> findFirstByTokenAndAllocatedStatus(String token, AllocatedStatus allocatedStatus);


}
