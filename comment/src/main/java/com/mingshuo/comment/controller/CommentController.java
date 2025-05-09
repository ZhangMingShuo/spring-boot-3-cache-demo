package com.mingshuo.comment.controller;

import com.mingshuo.comment.pojo.Comment;
import com.mingshuo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/findCommentById")
    public Comment findCommentById(Integer id){
        Comment comment = commentService.findCommentById(id);
        return comment;
    }
}