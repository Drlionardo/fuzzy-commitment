package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.dto.request.CreatePostDto;
import com.example.fuzzycommitment.dto.response.PostDto;
import com.example.fuzzycommitment.entity.Post;
import com.example.fuzzycommitment.exception.PostNotFoundException;
import com.example.fuzzycommitment.repository.PostRepo;
import com.mongodb.client.MongoClient;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private PostRepo repo;
    private MongoTemplate mongoTemplate;

    public Post createPost(String userId, CreatePostDto dto) {
        var post = new Post();
        post.setOwnerId(userId);
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setCreationDate(LocalDateTime.now());
        return repo.insert(post);
    }

    public Post getPost(String postId) {
        return repo.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public Post updatePost(String postId, CreatePostDto dto) {
        var post = getPost(postId);
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        return repo.save(post);
    }

    public void deletePost(String postId) {
        repo.deleteById(postId);
    }

    public List<Post> getPosts(String cursorIterator, Integer limit) {
        // todo: implement cursor pagination
//        Query.query(Criteria.where("name").), User.class);
        return null;
    }
}
