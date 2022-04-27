package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreatePostDto;
import com.example.fuzzycommitment.entity.Post;
import com.example.fuzzycommitment.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@Validated
public class PostController {
    private PostService postService;
    @PutMapping("/post")
    public ResponseEntity<Map<String, String>> createPost(Authentication authentication, @RequestBody CreatePostDto dto) {
        var post = postService.createPost(null, dto);
        return new ResponseEntity<>(Map.of("postId", post.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(Authentication authentication, @PathVariable String id) {
        var post = postService.getPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
    @GetMapping("/post")
    public ResponseEntity<List<Post>> getLastPosts(@Valid @Min(1) @Max(100) @RequestParam Integer limit,
                                                      @RequestParam(required = false, name = "from") String cursorIterator) {
        var posts = postService.getPosts(cursorIterator, limit);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> updatePost(Authentication authentication,@PathVariable String id, @RequestBody CreatePostDto dto) {
        var post = postService.updatePost(id, dto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(Authentication authentication, @PathVariable String id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
