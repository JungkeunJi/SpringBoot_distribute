package com.example.distribute.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllocatedStatus {

    ALLOCATED(0, "할당", "분배된 돈을 누군가 받은 상태"),
    UNALLOCATED(1, "미할당", "분배된 돈을 누구도 받지 않은 상태")
    ;

    private Integer id;
    private String title;
    private String description;
}
