package com.designre.blog.config;

import com.designre.blog.exception.LoginExpireException;
import com.designre.blog.exception.NotFoundException;
import com.designre.blog.exception.NotLoginException;
import com.designre.blog.exception.TipException;
import com.designre.blog.util.ErrorCode;
import com.designre.blog.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/*
  Status corresponding to SpringMVC custom exception code
  Exception                               HTTP Status Code
  ConversionNotSupportedException         500 (Internal Server Error)
  HttpMessageNotWritableException         500 (Internal Server Error)
  HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
  HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
  HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
  NoHandlerFoundException                 404 (Not Found)
  TypeMismatchException                   400 (Bad Request)
  HttpMessageNotReadableException         400 (Bad Request)
  MissingServletRequestParameterException 400 (Bad Request)
  MethodArgumentNotValidException         400 (Bad Request)
  BindException                           400 (Bad Request)
  ConstraintViolationException            400 (Bad Request)
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = TipException.class)
    public RestResponse<RestResponse.Empty> tipErrorHandler(HttpServletRequest req, TipException e) {
        return RestResponse.fail(e.getMessage());
    }

    /**
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = NotFoundException.class)
    public RestResponse<RestResponse.Empty> NotFoundErrorHandler(HttpServletRequest req, NotFoundException e) {
        String message = "";
        if (null != e.getClz()) {
            message = e.getClz().getSimpleName();
        }
        message += "Resource does not exist!";
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), message);
    }

    /**
     * NotLogin exception handling
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = NotLoginException.class)
    public RestResponse<RestResponse.Empty> NotLoginErrorHandler(HttpServletRequest req, HttpServletResponse rep, NotLoginException e) {
        return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg());
    }

    /**
     * LoginExpire exception handling
     *
     * @param req
     * @param rep
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginExpireException.class)
    public RestResponse<RestResponse.Empty> LoginExpireErrorHandler(HttpServletRequest req, HttpServletResponse rep, LoginExpireException e) {
        return RestResponse.fail(ErrorCode.LOGIN_EXPIRE.getCode(), ErrorCode.LOGIN_EXPIRE.getMsg());
    }

    /**
     * Runtime exception
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param re  {@link RuntimeException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(RuntimeException.class)
    public RestResponse<RestResponse.Empty> runtimeExceptionHandler(HttpServletRequest req, HttpServletResponse rep, RuntimeException re) {
        log.error("---RuntimeException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), re.getMessage(), re);
        rep.setStatus(ErrorCode.RUNTIME.getCode());
        return RestResponse.fail(ErrorCode.RUNTIME.getCode(), ErrorCode.RUNTIME.getMsg());
    }

    /**
     * Null pointer exception handling
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NullPointerException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NullPointerException.class)
    public RestResponse<RestResponse.Empty> nullPointerExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NullPointerException ex) {
        log.error("---NullPointerException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.NULL_POINTER.getCode());
        return RestResponse.fail(ErrorCode.NULL_POINTER.getCode(), ErrorCode.NULL_POINTER.getMsg());
    }

    /**
     * Yype conversion exception
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link ClassCastException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(ClassCastException.class)
    public RestResponse<RestResponse.Empty> classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, ClassCastException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.CLASS_CAST.getCode());
        return RestResponse.fail(ErrorCode.CLASS_CAST.getCode(), ErrorCode.CLASS_CAST.getMsg());
    }

    /**
     * IO Exception
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IOException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(IOException.class)
    public RestResponse<RestResponse.Empty> classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IOException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.IO.getCode());
        return RestResponse.fail(ErrorCode.IO.getCode(), ErrorCode.IO.getMsg());
    }

    /**
     * Unknown method exception
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NoSuchMethodException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public RestResponse<RestResponse.Empty> noSuchMethodExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NoSuchMethodException ex) {
        log.error("---noSuchMethodException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.NO_SUCH_METHOD.getCode());
        return RestResponse.fail(ErrorCode.NO_SUCH_METHOD.getCode(), ErrorCode.NO_SUCH_METHOD.getMsg());
    }

    /**
     * Array out of bounds exception
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IndexOutOfBoundsException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public RestResponse<RestResponse.Empty> indexOutOfBoundsExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IndexOutOfBoundsException ex) {
        log.error("---indexOutOfBoundsException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.INDEX_OUTOF_BOUNDS.getCode());
        return RestResponse.fail(ErrorCode.INDEX_OUTOF_BOUNDS.getCode(), ErrorCode.INDEX_OUTOF_BOUNDS.getMsg());
    }

    /**
     * 400 Exception handler
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, TypeMismatchException.class, MissingServletRequestParameterException.class})
    public RestResponse<RestResponse.Empty> request400(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request400 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.BAD_REQUEST.getCode());
        return RestResponse.fail(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMsg());
    }

    /**
     * {@link RequestBody} The exception thrown when the parameter is bound to the object
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public RestResponse<RestResponse.Empty> methodArgumentNotValidExceptionHandler(HttpServletRequest req, HttpServletResponse rep, MethodArgumentNotValidException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.BAD_REQUEST.getCode());

        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }


    /**
     * The exception thrown when the parameter is bound to the object
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = BindException.class)
    public RestResponse<RestResponse.Empty> handleBindException(HttpServletRequest req, HttpServletResponse rep, BindException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);

        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        rep.setStatus(ErrorCode.BAD_REQUEST.getCode());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }

    /**
     * Single parameter check
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestResponse<RestResponse.Empty> handleBindGetException(HttpServletRequest req, HttpServletResponse rep, ConstraintViolationException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);

        List<String> defaultMsg = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        rep.setStatus(ErrorCode.BAD_REQUEST.getCode());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }

    /**
     * 404 Exception
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResponse<RestResponse.Empty> request404(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request404 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.NOT_FOUND.getCode());
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }

    /**
     * 405 Related exception
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public RestResponse<RestResponse.Empty> request405(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request405 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.METHOD_BOT_ALLOWED.getCode());
        return RestResponse.fail(ErrorCode.METHOD_BOT_ALLOWED.getCode(), ErrorCode.METHOD_BOT_ALLOWED.getMsg());
    }

    /**
     * 406 Related exception
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public RestResponse<RestResponse.Empty> request406(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request406 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.NOT_ACCEPTABLE.getCode());
        return RestResponse.fail(ErrorCode.NOT_ACCEPTABLE.getCode(), ErrorCode.NOT_ACCEPTABLE.getMsg());
    }

    /**
     * 500 Related exception
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public RestResponse<RestResponse.Empty> server500(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---server500 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        return RestResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * Global exception
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link HttpServletResponse}
     * @return {@link Exception}
     */
    @ExceptionHandler(value = Exception.class)
    public RestResponse<RestResponse.Empty> defaultErrorHandler(HttpServletRequest req, HttpServletResponse rep, Exception e) {
        log.error("---DefaultException Handler---Host {}, invokes url {}, ERROR TYPE: {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getClass(), e.getMessage(), e);
        return RestResponse.fail(e.getMessage());
    }
}
