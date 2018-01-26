package ru.alphadoub.voting.util.exception;

import org.springframework.security.access.AccessDeniedException;

import static ru.alphadoub.voting.Messages.CAN_NOT_VOTE;

public class VotingTimeConstraintException extends AccessDeniedException {
    public VotingTimeConstraintException(String arg) {
        super(String.format(CAN_NOT_VOTE, arg));
    }
}
