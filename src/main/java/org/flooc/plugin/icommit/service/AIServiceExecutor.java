package org.flooc.plugin.icommit.service;

import java.util.HashMap;
import java.util.Map;
import org.flooc.plugin.icommit.service.volc.VolcServiceImpl;
import org.flooc.plugin.icommit.setting.ICommitSettingsState;

/**
 * @author sujie
 * @since 1.0.0
 */
public class AIServiceExecutor implements AIService {

  private static final Map<String, AIService> AI_SERVICES = new HashMap<>();

  static {
    AI_SERVICES.put(ServiceType.VOLC.getValue(), new VolcServiceImpl());
  }

  @Override
  public String generateCommitMessage(String prompt) throws Exception {
    String serviceType = ICommitSettingsState.getInstance().serviceType;
    if (serviceType == null || serviceType.trim().isEmpty()
        || AI_SERVICES.get(serviceType) == null) {
      return AI_SERVICES.get(ServiceType.VOLC.getValue())
          .generateCommitMessage(prompt);
    }
    return AI_SERVICES.get(serviceType).generateCommitMessage(prompt);
  }
}
