package org.flooc.plugin.icommit.service;

import com.intellij.openapi.progress.ProgressIndicator;

/**
 * Interface for AI services to generate commit messages
 */
public interface AIService {

  String DOUBAO_SERVICE_TYPE = "doubao";

  /**
   * Generate commit message based on git diff
   *
   * @param prompt Prompt
   * @return Generated commit message
   * @throws Exception If there's an error during generation
   */
  String generateCommitMessage(String prompt, ProgressIndicator indicator) throws Exception;


}