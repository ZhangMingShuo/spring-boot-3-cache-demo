package com.mingshuo.comment.repository;

import com.mingshuo.comment.pojo.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    // 根据评论id修改评论作者author
    @Transactional
    @Modifying
    @Query(value = "update t_comment c set c.author = ?1 where c.id=?2",nativeQuery = true)
    public int updateComment(String author, Integer id);
}

