package com.origin.risk.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/** Exception handler das exceptions do projeto. */
@Slf4j
@ControllerAdvice
@Hidden
public class RiskExceptionHandler {

  private static final String DEFAULT_ERROR_HANDLING_MESSAGE = "Handling {}: {}";

  @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected Error handleBindException(final BindException ex) {
    final ErrorCodes errorCode = ErrorCodes.MALFORMED_REQUEST;
    log.error("Handling {}: {}", ex.getClass().getSimpleName(), errorCode, ex);

    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    return new Error(errorCode, errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected Error processHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
    final ErrorCodes errorCode = ErrorCodes.INTERNAL_SERVER_ERROR;
    log.error(DEFAULT_ERROR_HANDLING_MESSAGE, ex.getClass().getSimpleName(), errorCode, ex);
    return new Error(errorCode);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected Error processBadRequestException(final RiskBaseException ex) {
    log.error(DEFAULT_ERROR_HANDLING_MESSAGE, ex.getClass().getSimpleName(), ex.getError(), ex);
    return ex.getError();
  }

  @ExceptionHandler(RiskBaseException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  protected Error gameException(final RiskBaseException ex) {
    log.error(DEFAULT_ERROR_HANDLING_MESSAGE, ex.getClass().getSimpleName(), ex.getError(), ex);
    return ex.getError();
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  protected Error processHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException ex) {
    final ErrorCodes errorCode = ErrorCodes.METODO_NAO_PERMITIDO;
    log.error(DEFAULT_ERROR_HANDLING_MESSAGE, ex.getClass().getSimpleName(), errorCode, ex);
    return new Error(errorCode);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  protected Error processException(final Exception ex) {
    final ErrorCodes errorCode = ErrorCodes.INTERNAL_SERVER_ERROR;
    log.error(DEFAULT_ERROR_HANDLING_MESSAGE, ex.getClass().getSimpleName(), errorCode, ex);
    return new Error(errorCode);
  }
}
