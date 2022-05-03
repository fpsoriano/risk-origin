package com.origin.risk.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Error model class */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

  private static final long serialVersionUID = 1L;

  @JsonProperty private int code;

  @JsonProperty private String message;

  @JsonProperty private List<String> details;

  public Error(final ErrorCodes issue) {
    code = issue.getCode();
    message = issue.getFormattedMessage();
  }

  public Error(final ErrorCodes issue, final Object... args) {
    code = issue.getCode();
    message = issue.getFormattedMessage(args);
  }

  public Error(final ErrorCodes issue, final List<String> details) {
    this(issue);
    this.details = details;
  }


  @Override
  public String toString() {
    return String.format("Error{code= %s, message='%s' details= '%s'}", code, message, details);
  }
}
