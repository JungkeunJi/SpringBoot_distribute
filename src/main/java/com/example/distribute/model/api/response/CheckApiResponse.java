package com.example.distribute.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckApiResponse {

    private LocalDateTime registeredAt;

    private Long totalMoney;

    private Long sumAllocatedMoney;

    private List<Receiver> receivers;
}
