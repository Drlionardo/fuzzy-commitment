package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreatePostDto;
import com.example.fuzzycommitment.dto.response.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class PostController {
    @PutMapping("/post")
    public ResponseEntity<Map<String, String>> createPost(@RequestParam String userId, @RequestBody CreatePostDto dto) {
        return new ResponseEntity<>(Map.of("postId", "GENERATED_ID"), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String id) {
        return new ResponseEntity<>(new PostDto(), HttpStatus.OK);
    }
    @GetMapping("/post")
    public ResponseEntity<List<PostDto>> getLastPosts() {
        return new ResponseEntity<>(List.of(new PostDto()), HttpStatus.OK);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Void> updatePost(String userId, @PathVariable String id) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(String userId, @PathVariable String id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
