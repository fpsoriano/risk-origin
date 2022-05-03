package com.origin.risk.exception;

/** Base exception. */
public class RiskBaseException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Error error;

  protected RiskBaseException(final Error error) {
    this.error = error;
  }

  public Error getError() {
    return error;
  }
}
