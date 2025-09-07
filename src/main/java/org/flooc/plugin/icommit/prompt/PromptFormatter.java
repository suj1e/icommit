package org.flooc.plugin.icommit.prompt;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.Change.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.flooc.plugin.icommit.notice.ICommitNotifications;
import org.flooc.plugin.icommit.util.PropertiesUtils;

/**
 * @author sujie
 * @since 1.0.0
 */
public final class PromptFormatter {

  private PromptFormatter() {
  }

  public static PromptDTO readPromptConfig() {
    try {
      Properties properties = PropertiesUtils.readIfPossible("conf/prompt.properties");
      if (properties == null) {
        ICommitNotifications.notify("读取prompt配置失败", MessageType.ERROR);
        return null;
      }
      PromptDTO promptDTO = new PromptDTO();
      promptDTO.setTips(properties.getProperty("prompt.tips"));
      promptDTO.setItemFormat(properties.getProperty("prompt.itemFormat"));
      return promptDTO;
    } catch (IOException e) {
      ICommitNotifications.notify("读取prompt配置失败", MessageType.ERROR);
      return null;
    }
  }

  public static String format(List<Change> changes, String promptTips) throws VcsException {
    if (changes == null || changes.isEmpty()) {
      return "";
    }
    PromptDTO promptDTO = readPromptConfig();
    String itemFormat = promptDTO.getItemFormat();
    String tips = promptDTO.getTips();
    if (promptTips != null && !promptTips.isEmpty()) {
      tips = promptTips;
    }
    List<String> diffs = new ArrayList<>();
    // 新增
    new2Diffs(changes, itemFormat, diffs);
    // 修改
    modification2Diffs(changes, itemFormat, diffs);
    // 移动
    moved2Diffs(changes, itemFormat, diffs);
    // 删除
    deleted2Diffs(changes, itemFormat, diffs);
    return tips + "\n" + String.join("\n", diffs);
  }

  private static void deleted2Diffs(List<Change> changes, String itemFormat, List<String> diffs)
      throws VcsException {
    List<Change> deleteChanges = changes.stream().filter(i -> Type.DELETED.equals(i.getType()))
        .toList();
    if (!deleteChanges.isEmpty()) {
      for (Change change : deleteChanges) {
        String fileName = change.getBeforeRevision().getFile().getName();
        String before = change.getBeforeRevision().getContent();
        String changeType = change.getType().name();
        String diff = itemFormat.replace("{{fileName}}", fileName)
            .replace("{{before}}", before)
            .replace("{{after}}", "")
            .replace("{{changeType}}", changeType);
        diffs.add(diff);
      }
    }
  }

  private static void moved2Diffs(List<Change> changes, String itemFormat,
      List<String> diffs)
      throws VcsException {
    List<Change> movedChanges = changes.stream()
        .filter(i -> Type.MOVED.equals(i.getType()))
        .toList();
    if (!movedChanges.isEmpty()) {
      for (Change change : movedChanges) {
        String beforeFilePath = change.getBeforeRevision().getFile().getPath();
        String afterFilePath = change.getAfterRevision().getFile().getPath();
        String fileName = change.getAfterRevision().getFile().getName();
        String changeType = change.getType().name();
        String diff = itemFormat.replace("{{fileName}}", fileName)
            .replace("{{before}}", beforeFilePath)
            .replace("{{after}}", afterFilePath)
            .replace("{{changeType}}", changeType);
        diffs.add(diff);
      }
    }
  }

  private static void modification2Diffs(List<Change> changes, String itemFormat,
      List<String> diffs)
      throws VcsException {
    List<Change> modificationChanges = changes.stream()
        .filter(i -> Type.MODIFICATION.equals(i.getType()))
        .toList();
    if (!modificationChanges.isEmpty()) {
      for (Change change : modificationChanges) {
        String fileName = change.getVirtualFile().getName();
        String before = change.getBeforeRevision().getContent();
        String after = change.getAfterRevision().getContent();
        String changeType = change.getType().name();
        String diff = itemFormat.replace("{{fileName}}", fileName)
            .replace("{{before}}", before)
            .replace("{{after}}", after)
            .replace("{{changeType}}", changeType);
        diffs.add(diff);
      }
    }
  }

  private static void new2Diffs(List<Change> changes, String itemFormat, List<String> diffs)
      throws VcsException {
    List<Change> addChanges = changes.stream().filter(i -> Type.NEW.equals(i.getType()))
        .toList();
    if (!addChanges.isEmpty()) {
      for (Change change : addChanges) {
        String fileName = change.getAfterRevision().getFile().getName();
        String after = change.getAfterRevision().getContent();
        String changeType = change.getType().name();
        String diff = itemFormat.replace("{{fileName}}", fileName)
            .replace("{{before}}", "")
            .replace("{{after}}", after)
            .replace("{{changeType}}", changeType);
        diffs.add(diff);
      }
    }
  }


}
