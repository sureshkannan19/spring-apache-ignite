package com.sk.model;

import lombok.Data;

import java.util.List;

@Data
public class IgniteDtoWrapper {

    private int count;

    private List<?> data;

}
