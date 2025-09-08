package org.flooc.plugin.icommit.setting;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * @author sujie
 * @since 1.0.0
 */
public class ICommitSettingsComponent {

  private JPanel contentPanel;
  private JTextField apiKey;
  private JTextField apiUrl;
  private JTextField model;
  private JTextPane promptTips;
  private JComboBox<String> serviceType;

  public JPanel getContentPanel() {
    return contentPanel;
  }

  public JTextField getApiKey() {
    return apiKey;
  }

  public JTextField getApiUrl() {
    return apiUrl;
  }

  public JComboBox<String> getServiceType() {
    return serviceType;
  }

  public JTextPane getPromptTips() {
    return promptTips;
  }

  public JTextField getModel() {
    return model;
  }

}
