package com.designre.blog.util;

public enum ErrorCode {
    RUNTIME(1000, "RuntimeException"),
    NULL_POINTER(1001, "NullPointerException "),
    CLASS_CAST(1002, "ClassCastException"),
    IO(1003, "IOException"),
    NO_SUCH_METHOD(1004, "NoSuchMethodException"),
    INDEX_OUTOF_BOUNDS(1005, "IndexOutOfBoundsException"),
    METHOD_ARGUMENT_NOT_VALID(400, "METHOD_ARGUMENT_NOT_VALID"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_BOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_LOGIN(999, "Not Login"),
    LOGIN_EXPIRE(998, "Login Expire"),
    ;

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
