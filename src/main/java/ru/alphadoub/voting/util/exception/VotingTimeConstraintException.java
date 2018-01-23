package ru.alphadoub.voting.util.exception;

import static ru.alphadoub.voting.Messages.CAN_NOT_VOTE;

public class VotingTimeConstraintException extends RuntimeException {
    public VotingTimeConstraintException(String arg) {
        super(String.format(CAN_NOT_VOTE, arg));
    }
}
