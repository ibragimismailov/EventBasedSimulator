package GUI.ConfigurationFrames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import Model.AbstractConfiguration.GetMethod;
import Model.AbstractConfiguration.SetMethod;

/**
 * When user clicks button that is responsible for some compaction configuration (right part of
 * ConfigurationFrame), this frame is showed. User can edit compaction algorithm configurations values with it
 * @author ibra
 */
public class CompactionConfigurationFrame extends AbstractConfigurationFrame {

  /**
   * @param title - main title of frame
   * @param getFields - get fields map of compaction configuration
   * @param setFields - get fields map of compaction configuration
   */
  public CompactionConfigurationFrame(final String title, final Map<String, GetMethod> getFields,
      final Map<String, SetMethod> setFields) {
    this.initComponents(title, getFields, setFields);
  }

  /**
   * initializes and layouts all gui components
   * @param title - main title of frame
   * @param getFields - get fields map of compaction configuration
   * @param setFields - get fields map of compaction configuration
   */
  private void initComponents(final String title, final Map<String, GetMethod> getFields,
      final Map<String, SetMethod> setFields) {
    JPanel mainPanel = new javax.swing.JPanel();
    JPanel applyCancelPanel = new JPanel();
    JButton applyButton = new JButton();
    JButton cancelButton = new JButton();
    this.getFields = getFields;
    this.setFields = setFields;

    this.labels = new ArrayList<JLabel>();
    this.fields = new ArrayList<JTextField>();

    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.setTitle(title);

    mainPanel.setBackground(new java.awt.Color(204, 204, 255));

    for (final String s : this.getFields.keySet()) {
      this.labels.add(new JLabel(s));
      this.fields.add(new JTextField(this.getFields.get(s).get()));
    }

    // @formatter:off
    final GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(mainPanelLayout.createSequentialGroup()
      .addContainerGap()
      .addGroup(createParallelGroup(mainPanelLayout, this.labels, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
      .addGap(40, 40, 40)
      .addGroup(createParallelGroup(mainPanelLayout, this.fields, GroupLayout.DEFAULT_SIZE, 300, GroupLayout.PREFERRED_SIZE))
      .addContainerGap()
    );
    mainPanelLayout.setVerticalGroup(
      createSequentialGroup(mainPanelLayout, this.labels, this.fields, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    );
    // @formatter:on

    applyCancelPanel.setBackground(new java.awt.Color(153, 153, 255));

    applyButton.setText("OK");
    applyButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(final java.awt.event.ActionEvent evt) {
        CompactionConfigurationFrame.this.applyButtonActionPerformed(evt);
      }
    });

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(final java.awt.event.ActionEvent evt) {
        CompactionConfigurationFrame.this.cancelButtonActionPerformed(evt);
      }
    });

    // @formatter:off
    final GroupLayout applyCancelPanelLayout = new GroupLayout(applyCancelPanel);
    applyCancelPanel.setLayout(applyCancelPanelLayout);
    applyCancelPanelLayout.setHorizontalGroup(applyCancelPanelLayout.createSequentialGroup()
      .addGap(0, 188, Short.MAX_VALUE)
      .addComponent(cancelButton)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(applyButton)
    );
    // @formatter:on

    applyCancelPanelLayout.setVerticalGroup(applyCancelPanelLayout
        .createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(applyButton)
        .addComponent(cancelButton));

    // @formatter:off
    final GroupLayout layout = new GroupLayout(this.getContentPane());
    this.getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(applyCancelPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      )
      .addContainerGap()
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(applyCancelPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
      .addContainerGap()
    );
    // @formatter:on

    this.pack();
  }

  /**
   * Method sets values from textFields to compaction configuration
   */
  private void applyButtonActionPerformed(final java.awt.event.ActionEvent evt) {

    // get titles of incorrectly entered by user values
    List<String> badValues = this.getFieldsBadValues();

    if (badValues.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Applied!");
      this.setVisible(false);
      this.dispose();
    } else {
      JOptionPane.showMessageDialog(this, "Values, you entered to fields\n"
          + badValues.toString().replace(", ", ",\n") + "\n" + "are incorrect");

      // restore correct (previous) values of fields
      for (int i = 0; i < this.labels.size(); i++) {
        String key = this.labels.get(i).getText();
        this.fields.get(i).setText(this.getFields.get(key).get());
      }
    }
  }

  /**
   * method cancels user changes to compaction configuration by closing frame without applying
   * changes
   */
  private void cancelButtonActionPerformed(final java.awt.event.ActionEvent evt) {
    this.setVisible(false);
    this.dispose();
  }
}