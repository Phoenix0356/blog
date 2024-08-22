package com.phoenix.common.exceptions.clientException;

public class CommentNotFoundException extends BaseException {
    public CommentNotFoundException() {
        super("Comment not found");
    }

    public CommentNotFoundException(String msg) {
        super(msg);
    }
}
