package com.example.distribute.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"distributionStates"})
@Accessors(chain = true)
@Builder
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private String roomId;

    private Long totalMoney;

    private int totalDistributeNum;

    @Column(unique = true)
    private String token;

    private LocalDateTime registeredAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "distribution")
    private List<DistributionState> distributionStates;
}
