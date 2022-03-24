package com.designre.blog.listener.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

@ToString
@Getter
public class ArticleHitsEvent extends ApplicationEvent {

    private final Integer articleId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param articleId article id
     */
    public ArticleHitsEvent(Object source, @NonNull Integer articleId) {
        super(source);
        this.articleId = articleId;
    }
}
