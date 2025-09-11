package org.flooc.plugin.icommit.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts.ConfigurableName;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import org.flooc.plugin.icommit.constant.VolcConstant;
import org.flooc.plugin.icommit.prompt.PromptConstant;
import org.flooc.plugin.icommit.service.ServiceType;
import org.flooc.plugin.icommit.service.volc.VolcDeepThinking;
import org.flooc.plugin.icommit.service.volc.VolcServiceImpl;
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
    ICommitSettingsState state = ICommitSettingsState.getInstance();
    return !component.getApiKey().getText().equals(state.apiKey) ||
        !component.getApiUrl().getText().equals(state.apiUrl) ||
        !String.valueOf(component.getServiceType().getSelectedItem()).equals(state.serviceType) ||
        !component.getPromptTips().getText().equals(state.promptTips) ||
        !component.getModel().getText().equals(state.model) ||
        !String.valueOf(component.getDeepThinking().getSelectedItem()).equals(state.deepThinking);
  }

  @Override
  public void apply() throws ConfigurationException {
    ICommitSettingsState state = ICommitSettingsState.getInstance();
    state.apiKey = component.getApiKey().getText();
    state.apiUrl = component.getApiUrl().getText();
    state.serviceType = String.valueOf(component.getServiceType().getSelectedItem());
    state.promptTips = component.getPromptTips().getText();
    state.model = component.getModel().getText();
    state.deepThinking = String.valueOf(component.getDeepThinking().getSelectedItem());
  }

  @Override
  public void reset() {
    ICommitSettingsState state = ICommitSettingsState.getInstance();
    component.getApiKey().setText(state.apiKey);
    // 其他值初始化给默认值
    String[] serviceTypeItems = new String[]{ServiceType.VOLC.getValue()};
    component.getServiceType().setModel(new DefaultComboBoxModel<>(serviceTypeItems));
    component.getDeepThinking()
        .setModel(new DefaultComboBoxModel<>(Arrays.stream(VolcDeepThinking.values()).map(
            VolcDeepThinking::getValue).toArray(String[]::new)));
    component.getApiUrl().setText(VolcConstant.DEFAULT_URL);
    component.getModel().setText(VolcConstant.DEFAULT_MODEL);
    component.getPromptTips().setText(PromptConstant.DEFAULT_PROMPT_TIPS);
  }

  @Override
  public void disposeUIResources() {
    component = null;
  }
}
