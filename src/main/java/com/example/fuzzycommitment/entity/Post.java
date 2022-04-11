package com.example.fuzzycommitment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Post {
    @Id
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private LocalDateTime creationDate;

}
