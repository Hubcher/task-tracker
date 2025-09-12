package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AckDto {

    boolean answer;

    public static AckDto getAnswer(boolean answer) {

        return builder()
                .answer(answer)
                .build();
    }


}
