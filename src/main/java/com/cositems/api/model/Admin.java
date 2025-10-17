package com.cositems.api.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Document(collection = "users")
@TypeAlias("admin")
public class Admin extends User {
    public Admin() {
        super();
    }
}
