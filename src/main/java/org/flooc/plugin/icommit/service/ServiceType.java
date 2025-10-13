package org.flooc.plugin.icommit.service;

/**
 * @author sujie
 * @since 1.0.0
 */
public enum ServiceType {
  VOLC("volc"),
  ALIYUN("aliyun"),
  ;
  private final String value;

  ServiceType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
