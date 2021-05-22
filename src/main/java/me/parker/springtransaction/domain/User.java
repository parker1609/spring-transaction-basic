package me.parker.springtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class User {
    private Long id;
    private String name;
    private int age;
}
