package org.flooc.plugin.icommit.service;

/**
 * Interface for AI services to generate commit messages
 */
public interface AIService {

  String VOLC_SERVICE_TYPE = "volc";

  /**
   * Generate commit message based on git diff
   *
   * @param prompt Prompt
   * @return Generated commit message
   * @throws Exception If there's an error during generation
   */
  String generateCommitMessage(String prompt) throws Exception;


}