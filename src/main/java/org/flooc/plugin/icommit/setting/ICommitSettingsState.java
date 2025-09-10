package org.flooc.plugin.icommit.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author sujie
 * @since 1.0.0
 */
@State(name = "org.flooc.plugin.icommit.setting.ICommitSettingsState", storages = @Storage("icommit-settings.xml"))
@Service
public final class ICommitSettingsState implements PersistentStateComponent<ICommitSettingsState> {

  public String apiKey;
  public String apiUrl;
  public String serviceType;
  public String deepThinking;
  public String promptTips;
  public String model;

  public static ICommitSettingsState getInstance() {
    return ApplicationManager.getApplication().getService(ICommitSettingsState.class);
  }

  @Override
  public @Nullable ICommitSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull ICommitSettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
