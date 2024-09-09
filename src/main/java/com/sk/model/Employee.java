package com.sk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 4078175067760766420L;

    @QuerySqlField(index = true)
    @QueryTextField
    private Long employeeId;

    @QuerySqlField(index = true)
    @QueryTextField
    private String firstName;

    @QuerySqlField(index = true)
    @QueryTextField
    private String lastName;

    @QuerySqlField(index = true)
    @QueryTextField
    private String address;

    @QuerySqlField(index = true)
    @QueryTextField
    private String job;

    @QuerySqlField(index = true)
    @QueryTextField
    private long mobileNumber;

}
