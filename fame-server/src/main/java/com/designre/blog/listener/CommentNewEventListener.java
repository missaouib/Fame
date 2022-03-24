package com.designre.blog.listener;

import com.designre.blog.listener.event.CommentNewEvent;
import com.designre.blog.model.dto.CommentDto;
import com.designre.blog.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentNewEventListener {


    private final EmailService emailService;

    @Async
    @EventListener
    public void onCommentNewEvent(CommentNewEvent event) {
        log.info("onCommentNewEvent event:{}", event);

        CommentDto commentDetail = event.getCommentDto();

        emailService.sendEmailToAdmin(commentDetail);
        if (null != commentDetail.getParentComment() && !StringUtils.hasText(commentDetail.getParentComment().getEmail())) {
            emailService.sendEmailToUser(commentDetail, commentDetail.getParentComment().getEmail());
        }
    }
}
