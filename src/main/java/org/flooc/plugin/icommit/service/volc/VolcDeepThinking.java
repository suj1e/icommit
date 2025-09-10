package org.flooc.plugin.icommit.service.volc;

/**
 * @author sujie
 * @since 1.0.0
 */
public enum VolcDeepThinking {
  DISABLED("disabled"),
  ENABLED("enabled"),
  AUTO("auto"),
  ;

  private final String value;

  VolcDeepThinking(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
