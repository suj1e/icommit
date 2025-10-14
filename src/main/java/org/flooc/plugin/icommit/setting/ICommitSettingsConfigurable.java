package org.flooc.plugin.icommit.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts.ConfigurableName;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flooc.plugin.icommit.constant.VolcConstant;
import org.flooc.plugin.icommit.prompt.PromptConstant;
import org.flooc.plugin.icommit.enums.ServiceType;
import org.flooc.plugin.icommit.enums.VolcDeepThinking;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author sujie
 * @since 1.0.0
 */
public class ICommitSettingsConfigurable implements Configurable {

  private static final String DEFAULT_DISPLAY_NAME = "ICommit";

  private ICommitSettingsComponent component;


  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public @ConfigurableName String getDisplayName() {
    return DEFAULT_DISPLAY_NAME;
  }


  @Override
  public @Nullable JComponent createComponent() {
    component = initComponent();
    return component.getContentPanel();
  }

  private ICommitSettingsComponent initComponent() {
    component = new ICommitSettingsComponent();
    return component;
  }

  @Override
  public boolean isModified() {
    ICommitSettings.State state = ICommitSettings.getInstance().getState();
    return !component.getApiKey().getText().equals(state.apiKey) ||
        !component.getApiUrl().getText().equals(state.apiUrl) ||
        !String.valueOf(component.getServiceType().getSelectedItem()).equals(state.serviceType) ||
        !component.getPromptTips().getText().equals(state.promptTips) ||
        !component.getModel().getText().equals(state.model) ||
        !String.valueOf(component.getDeepThinking().getSelectedItem()).equals(state.deepThinking);
  }

  @Override
  public void apply() throws ConfigurationException {
    ICommitSettings.State state = Objects.requireNonNull(ICommitSettings.getInstance().getState());
    state.apiKey = component.getApiKey().getText();
    state.apiUrl = component.getApiUrl().getText();
    state.serviceType = String.valueOf(component.getServiceType().getSelectedItem());
    state.promptTips = component.getPromptTips().getText();
    state.model = component.getModel().getText();
    state.deepThinking = String.valueOf(component.getDeepThinking().getSelectedItem());
  }

  @Override
  public void reset() {
    ICommitSettings.State state = Objects.requireNonNull(ICommitSettings.getInstance().getState());
    component.getApiKey().setText(state.apiKey);
    String[] serviceTypes = new String[ServiceType.values().length];
    if (StringUtils.isNotEmpty(state.serviceType)) {
      serviceTypes[0] = state.serviceType;
      List<ServiceType> serviceTypeList = Arrays.stream(ServiceType.values())
          .filter(j -> !j.getValue().equals(state.serviceType)).toList();
      if (!CollectionUtils.isEmpty(serviceTypeList)) {
        for (int i = 0; i < serviceTypeList.size(); i++) {
          serviceTypes[i + 1] = serviceTypeList.get(i).getValue();
        }
      }
    } else {
      serviceTypes = Arrays.stream(ServiceType.values()).map(ServiceType::getValue)
          .toArray(String[]::new);
    }
    component.getServiceType()
        .setModel(new DefaultComboBoxModel<>(serviceTypes));

    String[] deepThinking = new String[VolcDeepThinking.values().length];
    if (StringUtils.isNotEmpty(state.deepThinking)) {
      deepThinking[0] = state.deepThinking;
      List<VolcDeepThinking> deepThinkingList = Arrays.stream(VolcDeepThinking.values())
          .filter(j -> !j.getValue().equals(state.deepThinking)).toList();
      if (!CollectionUtils.isEmpty(deepThinkingList)) {
        for (int i = 0; i < deepThinkingList.size(); i++) {
          deepThinking[i + 1] = deepThinkingList.get(i).getValue();
        }
      }
    } else {
      deepThinking = Arrays.stream(VolcDeepThinking.values()).map(VolcDeepThinking::getValue)
          .toArray(String[]::new);
    }
    component.getDeepThinking()
        .setModel(new DefaultComboBoxModel<>(deepThinking));
    component.getApiUrl()
        .setText(StringUtils.defaultIfEmpty(state.apiUrl, VolcConstant.DEFAULT_URL));
    component.getModel()
        .setText(StringUtils.defaultIfEmpty(state.model, VolcConstant.DEFAULT_MODEL));
    component.getPromptTips()
        .setText(StringUtils.defaultIfEmpty(state.promptTips, PromptConstant.DEFAULT_PROMPT_TIPS));
  }

  @Override
  public void disposeUIResources() {
    component = null;
  }
}
