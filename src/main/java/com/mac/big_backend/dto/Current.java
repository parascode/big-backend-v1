package com.mac.big_backend.dto;

import lombok.Data;

@Data
public class Current {
    private double temp_c;
    private double temp_f;
    private Condition condition;
}
