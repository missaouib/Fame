package com.designre.blog.model.param;

import com.designre.blog.util.FameConst;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddCommentParam {
    @NotNull(message = "Associated articles are not allowed to be empty")
    private Integer articleId;

    private Integer parentId;

    @NotBlank(message = "Comment content is not allowed to be empty")
    @Size(max = FameConst.MAX_COMMENT_CONTENT_COUNT, message = "Comment content length cannot exceed {max}")
    private String content;

    @NotBlank(message = "Comment nickname is not allowed to be empty")
    @Size(max = FameConst.MAX_COMMENT_NAME_COUNT, message = "Comment nickname length cannot exceed {max}")
    private String name;

    @Email(message = "Email format error")
    @Size(max = FameConst.MAX_COMMENT_EMAIL_COUNT, message = "Email length cannot exceed {max}")
    private String email;

    @URL(message = "URL is malformed")
    @Size(max = FameConst.MAX_COMMENT_WEBSITE_COUNT, message = "URL length cannot exceed {max}")
    private String website;
}
