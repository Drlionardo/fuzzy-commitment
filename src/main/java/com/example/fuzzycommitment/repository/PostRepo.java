package com.example.fuzzycommitment.repository;

import com.example.fuzzycommitment.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepo extends MongoRepository<Post, String> {
}
