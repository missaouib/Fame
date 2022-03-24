package com.designre.blog.listener.event;

import com.designre.blog.model.dto.CommentDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Getter
public class CommentNewEvent extends ApplicationEvent {

    private final CommentDto commentDto;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param commentDto comment dto
     */
    public CommentNewEvent(Object source, CommentDto commentDto) {
        super(source);
        this.commentDto = commentDto;
    }
}
