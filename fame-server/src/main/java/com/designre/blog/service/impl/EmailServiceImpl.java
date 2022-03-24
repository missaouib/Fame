package com.designre.blog.service.impl;

import com.designre.blog.listener.event.LogEvent;
import com.designre.blog.model.enums.LogAction;
import com.designre.blog.model.enums.LogType;
import com.designre.blog.service.EmailService;
import com.designre.blog.service.SysOptionService;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.OptionKeys;
import com.designre.blog.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class EmailServiceImpl implements EmailService {

    private final SysOptionService sysOptionService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Async
    public void sendEmailToAdmin(Comment comment) {
        if (!isEmail(comment.getEmail())) {
            return;
        }

        Map<String, String> params = getEmailParams(comment);
        String content = FameConst.getEmailTemplateAdminContent(params);

        String logData = content + ";  Send to admin";
        log.info("sendEmailToAdmin start: {}", new Date());
        try {
            String emailUsername = sysOptionService.get(OptionKeys.EMAIL_USERNAME);
            sendEmail(content, emailUsername);

            LogEvent logEvent = new LogEvent(this, logData, LogAction.SUCCESS, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
        } catch (Exception e) {
            LogEvent logEvent = new LogEvent(this, logData, LogAction.FAIL, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendEmailToUser(Comment comment, String replyEmail) {
        if (!isEmail(replyEmail)) {
            return;
        }

        Map<String, String> params = getEmailParams(comment);
        String content = FameConst.getEmailTemplateUserContent(params);

        String logData = content + ";  send to:" + replyEmail;
        log.info("sendEmailToUser start: {}", new Date());
        try {
            sendEmail(content, replyEmail);

            LogEvent logEvent = new LogEvent(this, logData, LogAction.SUCCESS, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
        } catch (Exception e) {
            LogEvent logEvent = new LogEvent(this, logData, LogAction.FAIL, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
            log.error(e.getMessage(), e);
        }
    }


    private boolean isEmail(String email) {
        boolean isEmail = sysOptionService.get(OptionKeys.IS_EMAIL, Boolean.FALSE);
        if (!isEmail) {
            return false;
        }

        String adminUserEmail = sysOptionService.get(OptionKeys.EMAIL_USERNAME, "");
        return ObjectUtils.isEmpty(adminUserEmail) || !adminUserEmail.equals(email);
    }

    private Map<String, String> getEmailParams(Comment comment) {
        Map<String, String> params = new HashMap<>();

        String websiteName = sysOptionService.get(OptionKeys.BLOG_NAME);
        String website = sysOptionService.get(OptionKeys.BLOG_WEBSITE);

        if (!ObjectUtils.isEmpty(website)
                && website.lastIndexOf("/") != website.length()) {
            website = website + "/";
        }

        params.put("websiteName", websiteName);
        params.put("website", website);
        params.put("name", comment.getName());
        params.put("content", comment.getContent());
        params.put("articleId", String.valueOf(comment.getArticleId()));
        return params;
    }

    private void sendEmail(String content, String to) throws MessagingException {
        String subject = sysOptionService.get(OptionKeys.EMAIL_SUBJECT, FameConst.DEFAULT_EMAIL_TEMPLATE_SUBJECT);
        String host = sysOptionService.get(OptionKeys.EMAIL_HOST);
        Integer port = sysOptionService.get(OptionKeys.EMAIL_PORT, 25);
        String username = sysOptionService.get(OptionKeys.EMAIL_USERNAME);
        String password = sysOptionService.get(OptionKeys.EMAIL_PASSWORD);

        JavaMailSender mailSender = (JavaMailSender) mailSender(host, port,
                username, password);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(username);
        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);
        mailSender.send(mimeMessage);
    }

    private MailSender mailSender(String host, Integer port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }
}
