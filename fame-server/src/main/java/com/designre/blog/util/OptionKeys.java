package com.designre.blog.util;

import java.util.Arrays;
import java.util.List;

public interface OptionKeys {
    String FAME_INIT = "fame_init";
    String SUMMARY_FLAG = "summary_flag";
    String BLOG_NAME = "blog_name";
    String BLOG_WEBSITE = "blog_website";
    String BLOG_FOOTER = "blog_footer";
    String META_TITLE = "meta_title";
    String META_DESCRIPTION = "meta_description";
    String META_KEYWORDS = "meta_keywords";
    String GOOGLE_SITE_VERIFICATION = "google_site_verification";
    String BAIDU_SITE_VERIFICATION = "baidu-site-verification";
    String GOOGLE_ANALYTICS = "google_analytics";

    String IS_EMAIL = "is_email";

    String EMAIL_HOST = "email_host";
    String EMAIL_PORT = "email_port";
    String EMAIL_USERNAME = "email_username";
    String EMAIL_PASSWORD = "email_password";
    String EMAIL_SUBJECT = "email_subject";
    List<String> FRONT_OPTION_KEYS = Arrays.asList(
            BLOG_NAME,
            BLOG_WEBSITE,
            BLOG_FOOTER,
            META_TITLE,
            META_DESCRIPTION,
            META_KEYWORDS,
            GOOGLE_SITE_VERIFICATION,
            BAIDU_SITE_VERIFICATION,
            GOOGLE_ANALYTICS);
}
