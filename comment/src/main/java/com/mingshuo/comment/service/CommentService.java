package com.mingshuo.comment.service;

import com.mingshuo.comment.pojo.Comment;
import com.mingshuo.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Cacheable(cacheNames = "comment")
    public Comment findCommentById(Integer id){
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            Comment comment1 = comment.get();
            return comment1;
        }
        return null;
    }

    @CachePut(cacheNames="comment",key="#result.id")
    public Comment updateComment(Comment comment){
        commentRepository.updateComment(comment.getAuthor(),comment.getId());
        return comment;
    }

    @CacheEvict(cacheNames = "comment")
    public void deleteComment(int id){
        commentRepository.deleteById(id);
    }
}