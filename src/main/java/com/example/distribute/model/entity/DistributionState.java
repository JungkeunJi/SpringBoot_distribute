package com.example.distribute.model.entity;

import com.example.distribute.model.enumclass.AllocatedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class DistributionState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Long allocatedMoney;

    @Enumerated(EnumType.STRING)
    private AllocatedStatus allocatedStatus;

    private Integer allocatedUserId;

    @ManyToOne
    private Distribution distribution;
}
