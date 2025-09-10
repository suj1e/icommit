package org.flooc.plugin.icommit.util;

import com.intellij.openapi.actionSystem.DataContext;
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
import org.jetbrains.annotations.Nullable;

/**
 * @author sujie
 * @since 1.0.0
 */
public final class PromptUtils {

  private PromptUtils() {
  }

  @SuppressWarnings("rawtypes")
  public static String getPrompt(@Nullable DataContext dataContext, String promptTips)
      throws VcsException {
    if (dataContext == null) {
      return null;
    }
    Refreshable data = Refreshable.PANEL_KEY.getData(dataContext);
    if (!(data instanceof CommitMessageI)) {
      return null;
    }
    AbstractCommitWorkflowHandler handler = (AbstractCommitWorkflowHandler) VcsDataKeys.COMMIT_WORKFLOW_HANDLER.getData(
        dataContext);
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
