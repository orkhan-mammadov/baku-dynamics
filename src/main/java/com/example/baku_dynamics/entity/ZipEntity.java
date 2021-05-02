package com.example.baku_dynamics.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZipEntity {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TaskStatus taskStatus;
    private String filePath;
}
