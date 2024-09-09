package com.sk.model;

import lombok.Data;

@Data
public class IgniteFilter {

    private String fieldName;
    private String fieldValue;
    private String sort = "asc";

}
