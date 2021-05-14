package com.vinod.microservices.best.practices.exception;

import com.vinod.microservices.best.practices.util.ErrorCode;
import com.vinod.microservices.best.practices.util.ResourceMessage;
import com.vinod.microservices.best.practices.util.Response;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vinod.microservices.best.practices.util.GlobalUtility.buildResponseForError;

@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResourceMessage resourceMessage;

    /**
     * User not found exception handling.
     * @param e - UserNotFoundException object.
     * @return  - ResponseEntity object.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFound(UserNotFoundException e) {
        log.warn("UserNotFoundException occurred, error msg: {}, error details: {}", e.getMessage(), e);
        return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(ErrorCode.USER_NOT_FOUND.getCode()),resourceMessage.getMessage("customer.not.found.emailId"),null);
    }

    /**
     * Method Argument validation.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, org.springframework.http.HttpStatus status, WebRequest request) {
        List<String> errorMsg = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("MethodArgumentNotValidException occurred, error msg: {}, error details: {}", errorMsg, ex);
        Response apiError = Response.builder().status(HttpStatus.SC_BAD_REQUEST).errorCode(String.valueOf(ErrorCode.BAD_PARAMETER.getCode())).errorMessage(resourceMessage.getMessage("application.status.400")).errorData(errorMsg).build();
        return handleExceptionInternal(ex, apiError, headers, org.springframework.http.HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Path variable validation.
     *
     * @param ex - ConstraintViolationException object.
     * @return  - ResponseEntity object.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolation(ConstraintViolationException ex) {

        List<String> errorMsg = new ArrayList<>();
        List<String> displayErrorMsg = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String msg = violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage();
            displayErrorMsg.add(violation.getMessage());
            errorMsg.add(msg);
        }
        log.warn("ConstraintViolationException occurred, error msg: {}, error details: {}", errorMsg, ex);
        return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(ErrorCode.BAD_PARAMETER.getCode()),resourceMessage.getMessage("application.status.400"),displayErrorMsg);
    }


    /**
     * General Exception handling.
     *
     * @param e - Exception
     * @return  - ResponseEntity object.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        log.error("Exception occurred, error msg: {}", e.getMessage(), e);
        return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(ErrorCode.INTERNAL_ERROR.getCode()),resourceMessage.getMessage("application.status.500"),null);
    }
}
