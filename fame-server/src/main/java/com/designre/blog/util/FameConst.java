package com.designre.blog.util;

import java.io.File;
import java.util.Map;

public interface FameConst {

    String MD5_SLAT = "riopwhjrv123bnopiw234q2ec";
    String DEFAULT_PAGE = "1";
    String PAGE_SIZE = "10";
    Integer MAX_TITLE_COUNT = 255;
    Integer MAX_CONTENT_COUNT = 200000;
    Integer MAX_SUMMARY_COUNT = 255;
    String DEFAULT_SUMMARY_FLAG = "<!--read more-->";
    int MAX_COMMENT_CONTENT_COUNT = 1023;
    int MAX_COMMENT_NAME_COUNT = 255;
    int MAX_COMMENT_EMAIL_COUNT = 255;
    int MAX_COMMENT_WEBSITE_COUNT = 255;
    String DEFAULT_EMAIL_TEMPLATE_SUBJECT = "Email sent from the blog site";
    static String getEmailTemplateAdminContent(Map<String, String> params) {
        String emptyString = "";
        String websiteName = params.getOrDefault("websiteName", emptyString);
        String name = params.getOrDefault("name", emptyString);
        String content = params.getOrDefault("content", emptyString);
        String website = params.getOrDefault("website", emptyString);
        String articleId = params.getOrDefault("articleId", emptyString);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>" + websiteName + " Email</title>" +
                "</head>" +
                "<body>" +
                "<h3>" + websiteName + "There is a new comment reply</h3>" +
                "<p>From" + name + "With comment：" + content + "</p>" +
                "<br>" +
                "<a href=\"" + website + "article/" + articleId + "\">check the details" +
                "</a>" +
                "</body>" +
                "</html>";
    }

    static String getEmailTemplateUserContent(Map<String, String> params) {
        String emptyString = "";
        String websiteName = params.getOrDefault("websiteName", emptyString);
        String name = params.getOrDefault("name", emptyString);
        String content = params.getOrDefault("content", emptyString);
        String website = params.getOrDefault("website", emptyString);
        String articleId = params.getOrDefault("articleId", emptyString);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>" + websiteName + " Email</title>" +
                "</head>" +
                "<body>" +
                "<h3>" + websiteName + " The comment has been answered, go check it out!</h3>" +
                "<p>From" + name + "With content：" + content + "</p>" +
                "<br>" +
                "<a href=\"" + website + "article/" + articleId + "\">check the details</a>" +
                "</body>" +
                "</html>";
    }
    String FAME_HOME = ".fame" + File.separator;
    String USER_HOME = System.getProperties().getProperty("user.home") + File.separator;
    String UPLOAD_DIR = "upload" + File.separator;
    String MEDIA_DIR = UPLOAD_DIR + "media" + File.separator;
    String BACKUP_DIR = UPLOAD_DIR + "backup" + File.separator;
    String MEDIA_THUMBNAIL_SUFFIX = "_thumbnail";
    String MARKDOWN_SUFFIX = ".md";

}
