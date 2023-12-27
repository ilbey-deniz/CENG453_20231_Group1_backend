package com.group1.backend.dto;


import lombok.Data;

@Data
public class PlayerDto {
    String name;
    String color;
    boolean ready;
    boolean host;
    boolean cpu;
}
