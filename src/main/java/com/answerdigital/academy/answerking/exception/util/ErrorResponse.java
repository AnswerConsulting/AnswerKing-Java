package com.answerdigital.academy.answerking.exception.util;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Slf4j
@Getter
public class ErrorResponse {

    private final String type;

    private final String title;

    private final int status;

    private final String detail;

    private final String instance;

    private final Collection<String> errors;

    private final String traceId;

    public ErrorResponse(final AnswerKingException exception, final HttpServletRequest request) {
        this.type = exception.getType();
        this.title = exception.getTitle();
        this.status = exception.getStatus().value();
        this.detail = exception.getDetail();
        this.instance = request.getRequestURI();
        this.errors = exception.getErrors();
        this.traceId = CorrelationId.getId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s - ERRORS: %s - TRACEID: %s",
                type, title, status, detail, instance, errors.toString(), traceId)
        );
    }
}
