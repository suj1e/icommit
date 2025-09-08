package org.flooc.plugin.icommit.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.ui.Refreshable;
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler;
import java.util.List;
import org.flooc.plugin.icommit.notice.ICommitNotifications;
import org.flooc.plugin.icommit.prompt.PromptFormatter;
import org.flooc.plugin.icommit.service.AIService;
import org.flooc.plugin.icommit.service.AIServiceExecutor;
import org.flooc.plugin.icommit.setting.ICommitSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author sujie
 * @since 1.0.0
 */
public class GenMessageAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent actionEvent) {
    CommitMessageI commitMessageI = getCommitPanel(actionEvent);
    if (commitMessageI == null) {
      return;
    }
    AIService aiService = new AIServiceExecutor();
    String prompt;
    try {
      prompt = getPrompt(actionEvent, ICommitSettingsState.getInstance().promptTips);
    } catch (Exception e) {
      ICommitNotifications.notify("Failed to generate prompt", MessageType.ERROR);
      return;
    }
    if (prompt == null || prompt.isEmpty()) {
      ICommitNotifications.notify("Prompt is empty", MessageType.ERROR);
      return;
    }
    ProgressManager.getInstance().run(new Task.Backgroundable(actionEvent.getProject(),
        "Generating commit message", true) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setFraction(0.3);
        String commitMessage;
        try {
          commitMessage = aiService.generateCommitMessage(prompt);
        } catch (Exception e) {
          ICommitNotifications.notify("Generate commit message exception", MessageType.ERROR);
          return;
        }
        indicator.setFraction(0.8);
        if (commitMessage == null || commitMessage.isEmpty()) {
          ICommitNotifications.notify("Failed to generate commit message", MessageType.ERROR);
          return;
        }
        indicator.setFraction(1);
        ICommitNotifications.notify("Commit message generated", MessageType.INFO);
        ApplicationManager.getApplication()
            .invokeLater(() -> commitMessageI.setCommitMessage(commitMessage));
      }
    });
  }


  @Nullable
  private static CommitMessageI getCommitPanel(@Nullable AnActionEvent e) {
    if (e == null) {
      return null;
    }
    Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
    if (data instanceof CommitMessageI) {
      return (CommitMessageI) data;
    }
    return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
  }

  @SuppressWarnings("rawtypes")
  private String getPrompt(@Nullable AnActionEvent e, String promptTips) throws VcsException {
    if (e == null) {
      return null;
    }
    Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
    if (!(data instanceof CommitMessageI)) {
      return null;
    }
    AbstractCommitWorkflowHandler handler = (AbstractCommitWorkflowHandler) VcsDataKeys.COMMIT_WORKFLOW_HANDLER.getData(
        e.getDataContext());
    if (handler != null) {
      List<Change> includedChanges = handler.getUi().getIncludedChanges();
      if (includedChanges.isEmpty()) {
        ICommitNotifications.notify("No changes selected", MessageType.WARNING);
        return null;
      }
      return PromptFormatter.format(includedChanges, promptTips);
    }
    return null;
  }

}