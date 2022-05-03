package com.origin.risk.exception;

/**
 * Exception for business error
 * */
public final class BadRequestException extends RiskBaseException {

  private static final long serialVersionUID = 1L;

  private BadRequestException(final Error error) {
    super(error);
  }

  public static BadRequestException error(final ErrorCodes message, final Object... args) {
    return new BadRequestException(new Error(message, args));
  }
}
