package org.flooc.plugin.icommit.service.volc;

import static org.flooc.plugin.icommit.constant.VolcConstant.*;
import static org.flooc.plugin.icommit.constant.VolcConstant.DEFAULT_MODEL;

import com.intellij.openapi.ui.MessageType;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest.ChatCompletionRequestThinking;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.flooc.plugin.icommit.constant.VolcConstant;
import org.flooc.plugin.icommit.notice.ICommitNotifications;
import org.flooc.plugin.icommit.service.AIService;
import org.flooc.plugin.icommit.setting.ICommitSettingsState;

/**
 * Implementation of AI service for Volc API
 */
public class VolcServiceImpl implements AIService {



  private static final ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
  private static final Dispatcher dispatcher = new Dispatcher();

  @Override
  public String generateCommitMessage(String prompt) throws Exception {
    String apiKey = ICommitSettingsState.getInstance().apiKey;
    String apiUrl = ICommitSettingsState.getInstance().apiUrl;
    String model = ICommitSettingsState.getInstance().model;
    String deepThinking = ICommitSettingsState.getInstance().deepThinking;
    if (apiKey == null || apiKey.isEmpty()) {
      ICommitNotifications.notify("API key is empty", MessageType.ERROR);
      return null;
    }
    if (apiUrl == null || apiUrl.isEmpty()) {
      apiUrl = DEFAULT_URL;
    }
    if (model == null || model.isEmpty()) {
      model = DEFAULT_MODEL;
    }
    ArkService service = ArkService.builder().dispatcher(dispatcher)
        .connectionPool(connectionPool).baseUrl(apiUrl)
        .apiKey(apiKey).build();
    final List<ChatMessage> messages = new ArrayList<>();
    final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
    multiParts.add(ChatCompletionContentPart.builder().type("text").text(prompt).build());
    final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER)
        .multiContent(multiParts).build();
    messages.add(userMessage);

    ChatCompletionRequestThinking thinkingForReq = new ChatCompletionRequestThinking(deepThinking);
    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
        .model(model)
        .messages(messages)
        .stream(false)
        .thinking(thinkingForReq)
        .build();
    // Send request
    StringBuilder sb = new StringBuilder();
    service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> {
      sb.append(choice.getMessage().getContent());
    });
    service.shutdownExecutor();
    return sb.toString();
  }

}