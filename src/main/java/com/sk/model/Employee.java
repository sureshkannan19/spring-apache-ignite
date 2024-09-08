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
    private Long employeeId;
    @QueryTextField
    @QuerySqlField(index = true)
    private String firstName;
    @QueryTextField
    @QuerySqlField(index = true)
    private String lastName;
    @QueryTextField
    @QuerySqlField
    private String address;
    @QueryTextField
    @QuerySqlField
    private String job;
    @QuerySqlField(index = true)
    private long mobileNumber;

}
