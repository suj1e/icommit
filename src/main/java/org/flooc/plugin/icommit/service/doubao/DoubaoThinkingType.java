package org.flooc.plugin.icommit.service.doubao;

/**
 * @author sujie
 * @since 1.0.0
 */
public enum DoubaoThinkingType {
  ENABLED("enabled"),
  DISABLED("disabled"),
  AUTO("auto"),
  ;

  private final String value;

  DoubaoThinkingType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
