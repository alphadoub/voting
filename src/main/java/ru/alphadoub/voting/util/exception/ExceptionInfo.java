package ru.alphadoub.voting.util.exception;

public class ExceptionInfo {
    private String url;

    private String message;

    public ExceptionInfo(CharSequence url, String message) {
        this.url = url.toString();
        this.message = message;
    }
}
