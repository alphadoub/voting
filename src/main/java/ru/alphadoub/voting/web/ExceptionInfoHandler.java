package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.alphadoub.voting.util.ValidationUtil;
import ru.alphadoub.voting.util.exception.ExceptionInfo;
import ru.alphadoub.voting.util.exception.IncomingDataException;
import ru.alphadoub.voting.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static ru.alphadoub.voting.Messages.*;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@ResponseBody
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static final Map<String, String> UNIQUENESS_VIOLATIONS = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("restaurants_unique_name_idx", NOT_UNIQUE_RESTAURANT);
                    put("dishes_unique_restaurant_name_date_idx", NOT_UNIQUE_DISH);
                    put("users_unique_email_idx", NOT_UNIQUE_EMAIL);
                }
            });

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ExceptionInfo handleNoHandler(HttpServletRequest request, NoHandlerFoundException e) {
        return logAndGetExceptionInfo(request, e, false, e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionInfo handleNoSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return logAndGetExceptionInfo(request, e, false, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ExceptionInfo handleParsingError(HttpServletRequest request, Exception e) {
        return logAndGetExceptionInfo(request, e, false);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({NotFoundException.class, IncomingDataException.class})
    public ExceptionInfo handleNotFoundException(HttpServletRequest request, Exception e) {
        return logAndGetExceptionInfo(request, e, false, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionInfo handleForbidden(HttpServletRequest request, Exception e) {
        return logAndGetExceptionInfo(request, e, false, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionInfo handleIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = UNIQUENESS_VIOLATIONS.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                return logAndGetExceptionInfo(request, e, false, entry.get().getValue());
            }
        }
        return logAndGetExceptionInfo(request, e, true, DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ExceptionInfo handleValidationError(HttpServletRequest request, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        StringJoiner message = new StringJoiner("; ");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    message.add(msg);
                });
        return logAndGetExceptionInfo(request, e, false, message.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionInfo handle(HttpServletRequest request, Exception e) {
        return logAndGetExceptionInfo(request, e, true);
    }

    private ExceptionInfo logAndGetExceptionInfo(HttpServletRequest req, Throwable e, boolean logException, String message) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(e + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", e, req.getRequestURL(), rootCause.toString());
        }

        return new ExceptionInfo(req.getRequestURL(), message != null ? message : rootCause.toString());
    }
    private ExceptionInfo logAndGetExceptionInfo(HttpServletRequest req, Throwable e, boolean logException) {
        return logAndGetExceptionInfo(req, e, logException, null);
    }
}
