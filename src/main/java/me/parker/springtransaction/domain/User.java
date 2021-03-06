package me.parker.springtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class User {
    private Long id;
    private String name;
    private Long balance;
}
