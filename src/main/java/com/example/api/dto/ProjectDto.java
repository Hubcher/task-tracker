package com.example.api.dto;


import lombok.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    @NonNull
    Integer id;

    @NonNull
    String name;
}
