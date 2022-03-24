package com.designre.blog.service;


import com.designre.blog.model.entity.Comment;

public interface EmailService {

    void sendEmailToAdmin(Comment comment);
    void sendEmailToUser(Comment comment, String replyEmail);
}
