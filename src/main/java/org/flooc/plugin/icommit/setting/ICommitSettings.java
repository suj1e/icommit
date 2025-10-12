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
@State(name = "org.flooc.plugin.icommit.setting.ICommitSettings", storages = @Storage("icommit-settings.xml"))
@Service
public final class ICommitSettings implements PersistentStateComponent<ICommitSettings.State> {

  public static class State {
    public String apiKey;
    public String apiUrl;
    public String serviceType;
    public String deepThinking;
    public String promptTips;
    public String model;
  }

  private State myState = new State();

  public static ICommitSettings getInstance() {
    return ApplicationManager.getApplication().getService(ICommitSettings.class);
  }

  @Override
  public @Nullable ICommitSettings.State getState() {
    return myState;
  }

  @Override
  public void loadState(@NotNull ICommitSettings.State state) {
//    XmlSerializerUtil.copyBean(state, this);
    this.myState = state;
  }
}
