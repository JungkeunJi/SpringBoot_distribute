package com.example.distribute.model.entity;

import com.example.distribute.model.enumclass.AllocatedStatus;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"distribution"})
@Accessors(chain = true)
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
