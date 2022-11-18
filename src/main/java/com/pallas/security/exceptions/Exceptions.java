package com.pallas.security.exceptions;

import com.pallas.security.models.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;


@ControllerAdvice
public class Exceptions extends Throwable {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(com.pallas.security.exceptions.NotFoundException.class)
    public Response notFoundException() {
        return new Response().setResponse(new ArrayList<>()).setStatus(HttpStatus.OK.value()).setMessage("Data Not Found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AlreadyExistException.class)
    public Response alreadyExistException() {
        return new Response().setStatus(HttpStatus.ALREADY_REPORTED.value()).setMessage("Data Already Exist");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthenticationException.class)
    public Response authenticationException() {
        return new Response().setStatus(HttpStatus.UNAUTHORIZED.value()).setMessage("UNAUTHORIZED USER");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingParameterException.class)
    public Response missingParameterException(MissingParameterException e) {
        return new Response().setStatus(HttpStatus.BAD_REQUEST.value()).setMessage("Missing Parameter");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(com.pallas.security.exceptions.DestinationLockedException.class)
    public Response destinationLockedException(com.pallas.security.exceptions.DestinationLockedException e) {
        return new Response().setStatus(HttpStatus.DESTINATION_LOCKED.value()).setMessage("Destination Locked");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthErrorException.class)
    public Response unauthorized() {
        return new Response().setStatus(HttpStatus.UNAUTHORIZED.value()).setMessage("Unauthorized");
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ReplaySelectException.class)
    public Response replaySelect() {
        return new Response().setStatus(HttpStatus.ALREADY_REPORTED.value()).setMessage("other name please");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AccessDeniedException.class)
    public Response accessDeniedException(AccessDeniedException e) {
        return new Response().setStatus(HttpStatus.UNAUTHORIZED.value()).setMessage("Access Denied");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotAcceptableException.class)
    public Response notAcceptable (NotAcceptableException e){
        return new Response().setStatus(HttpStatus.NOT_ACCEPTABLE.value()).setMessage("Not Acceptable");
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response dataIntegrityViolationException(DataIntegrityViolationException e) {
        return new Response().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public Response invalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
        return new Response().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(JpaSystemException.class)
    public Response jpaSystemException(JpaSystemException e) {
        return new Response().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InternalServerErrorException.class)
    public Response internalErrorException(InternalServerErrorException e) {
        return new Response().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new Response().setStatus(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new Response().setStatus(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new Response().setStatus(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new Response().setStatus(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new Response().setStatus(HttpStatus.METHOD_NOT_ALLOWED.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return new Response().setStatus(HttpStatus.METHOD_NOT_ALLOWED.value()).setMessage(e.getMessage());
    }
}
