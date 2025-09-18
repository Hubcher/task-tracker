package com.example.api.dto;

import lombok.*;

@Builder
@Value
public class AckDto {

    boolean answer;

    public static AckDto getAnswer(boolean answer) {

        return builder()
                .answer(answer)
                .build();
    }


}
