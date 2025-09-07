package org.flooc.plugin.icommit.notice;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

/**
 * @author sujie
 * @since 1.0.0
 */
public final class ICommitNotifications {

  private ICommitNotifications() {
  }

  public static void notify(String message, MessageType messageType) {
    NotificationGroupManager groupManager = NotificationGroupManager.getInstance();
    Notification notification = groupManager.getNotificationGroup("CustomNotificationGroup")
        .createNotification(message, messageType);
    Notifications.Bus.notify(notification);
  }

}
