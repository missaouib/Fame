package com.designre.blog.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.designre.blog.model.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.designre.blog.exception.NotLoginException;
import com.designre.blog.exception.TipException;
import com.designre.blog.model.entity.User;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

@Slf4j
public class FameUtils {

    /**
     * {@code jackson} ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final MutableDataSet MARKDOWN_OPTIONS = new MutableDataSet();

    static {
        MARKDOWN_OPTIONS.setFrom(ParserEmulationProfile.MARKDOWN);
        MARKDOWN_OPTIONS.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));
    }

    private static final Parser PARSER = Parser.builder(MARKDOWN_OPTIONS).build();
    private static final HtmlRenderer HTML_RENDER = HtmlRenderer.builder(MARKDOWN_OPTIONS).build();
    private FameUtils() {
        throw new TipException("Constructor not allow");
    }

    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            throw new NotLoginException();
        }
        Object principal = authentication.getPrincipal();
        if (null == principal) {
            throw new NotLoginException();
        }
        return ((UserDetailsDto) principal).getUser();
    }
    public static Integer getLoginUserId() {
        return getLoginUser().getId();
    }

    public static String getJwtHeaderToken() {
        String header = getRequest().getHeader(JwtUtil.JWT_HEADER_KEY);
        if (!StringUtils.hasText(header)) {
            return null;
        }

        return header.replace(JwtUtil.JWT_HEADER_TOKEN_HEAD_KEY, "").trim();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(attrs).getRequest();
    }

    public static String getDomain() {
        StringBuffer url = getRequest().getRequestURL();
        return url.delete(url.length() - getRequest().getRequestURI().length(), url.length()).toString();
    }

    public static String getHostAddress() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("Get InetAddress error!", e);
            return "";
        }
        return address.getHostAddress();
    }

    public static String getIp() {
        String unknown = "unknown";
        String[] ipHeaders = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        String ip;
        for (String header : ipHeaders) {
            ip = getRequest().getHeader(header);
            if (ip != null && ip.length() != 0 && !unknown.equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return getRequest().getRemoteAddr();
    }

    public static String getAgent() {
        return getRequest().getHeader(HttpHeaders.USER_AGENT);
    }

    public static void writeJsonResponse(RestResponse<?> resp, HttpServletResponse response) {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(objectToJson(resp));
            printWriter.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
    public static String getMd5(String str) {
        String base = str + FameConst.MD5_SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static String getSummary(String content, String flag) {
        int index = 0;
        if (!ObjectUtils.isEmpty(flag)) {
            index = FameUtils.ignoreCaseIndexOf(content, flag);
        }
        if (ObjectUtils.isEmpty(flag) || -1 == index) {
            index = content.length() > FameConst.MAX_SUMMARY_COUNT ? FameConst.MAX_SUMMARY_COUNT : content.length();
        }
        return content.substring(0, index);
    }

    public static String mdToHtml(String md) {
        if (ObjectUtils.isEmpty(md)) {
            return "";
        }

        Node document = PARSER.parse(md);
        return HTML_RENDER.render(document);
    }

    public static String contentTransform(String content, boolean isSummary, boolean isHtml, String summaryFlag) {
        if (isSummary || isHtml) {
            if (isSummary) {
                content = FameUtils.getSummary(content, summaryFlag);
            }
            if (isHtml) {
                content = FameUtils.mdToHtml(content);
            }
        }
        return content;
    }
    @SneakyThrows
    public static <T> T jsonToObject(@NonNull String json, @NonNull Class<T> clz) {
        return OBJECT_MAPPER.readValue(json, clz);
    }
    @SneakyThrows
    public static String objectToJson(@NonNull Object data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }


    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static int ignoreCaseIndexOf(String str, String flag) {
        return StrUtil.indexOfIgnoreCase(str, flag);
    }
    public static <T> T convertStrTo(String value, Class<T> type) {
        return Convert.convert(type, value);
    }
    public static <T> T convertTo(Object source, Class<T> targetClz) {
        return BeanUtil.toBean(source, targetClz);
    }
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().ignoreNullValue());
    }

    public static Path getFameDir() {
        Path dir = Paths.get(FameConst.USER_HOME, FameConst.FAME_HOME);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new TipException(e);
            }
        }
        return dir;
    }

    public static String getFileBaseName(String fileName) {
        return FileUtil.getPrefix(fileName);
    }


    public static String getFileSuffix(String fileName) {
        return FileUtil.getSuffix(fileName);
    }


    public static boolean isImgFile(String suffix) {
        return StrUtil.equalsAnyIgnoreCase(suffix,
                ImgUtil.IMAGE_TYPE_GIF,
                ImgUtil.IMAGE_TYPE_JPG,
                ImgUtil.IMAGE_TYPE_JPEG,
                ImgUtil.IMAGE_TYPE_BMP,
                ImgUtil.IMAGE_TYPE_PNG,
                ImgUtil.IMAGE_TYPE_PSD);
    }
    public static void compressImage(File source, File target, float imageQuality) {
        ImgUtil.scale(source, target, imageQuality);
    }
}
