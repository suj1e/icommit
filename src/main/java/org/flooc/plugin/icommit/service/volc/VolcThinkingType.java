package org.flooc.plugin.icommit.service.volc;

/**
 * @author sujie
 * @since 1.0.0
 */
public enum VolcThinkingType {
  ENABLED("enabled"),
  DISABLED("disabled"),
  AUTO("auto"),
  ;

  private final String value;

  VolcThinkingType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
