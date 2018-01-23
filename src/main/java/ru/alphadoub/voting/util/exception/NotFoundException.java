package ru.alphadoub.voting.util.exception;

import static ru.alphadoub.voting.Messages.NOT_FOUND;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String arg) {
        super(String.format(NOT_FOUND, arg));
    }
}
