package org.flooc.plugin.icommit.prompt;

/**
 * @author sujie
 * @since 1.0.0
 */
public final class PromptConstant {

  private PromptConstant() {
  }

  public static final String DEFAULT_PROMPT_TIPS =
      "你是一个智能助手，你的任务是根据代码变更生成符合规范的提交信息。\n提交信息应简洁明了，不超过80个字符，概括主要变更内容。\n提交信息规范：符合Conventional Commits。提交信息格式：<type>[optional scope]: <description>";

}
