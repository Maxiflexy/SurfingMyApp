package com.digicore.omni.root.services.modules.common.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class TransactionHistoryPages {

    private Integer pageNumber = 0;
    private Integer pageSize = 50;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "createdOn";

}
