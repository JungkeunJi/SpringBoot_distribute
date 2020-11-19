package com.example.distribute.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistributeApiRequest {

    private Long totalMoney;

    private Integer totalDistributeNum;
}
