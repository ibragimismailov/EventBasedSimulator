package GUI.ConfigurationFrames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import Model.AbstractConfiguration.GetMethod;
import Model.AbstractConfiguration.SetMethod;
import Model.Configuration;
import Model.Simulator;
import Model.Compactors.AbstractCompactor;
import Model.Compactors.CompactionAlgorithm;

/**
 * Configuration Frame is main frame of application. It provides GUI for changing simulator settings: 
 * left side of frame contains general HBase configurations from Configuration class
 * right side of frame contains configurations for all compaction configurations 
 * @author ibra
 */
public class ConfigurationFrame extends AbstractConfigurationFrame {

  /**
   * List of compactors - (compactor is a compaction algorithm and it's configuration)
   */
  private final List<AbstractCompactor> compactors;

  /**
   * Buttons whose action is to change compaction configurations of each compaction algorithm
   */
  private final List<JButton> storeButtons;

  /**
   * ComboBoxes for choosing type of compaction algorithms
   */
  private final List<JComboBox<CompactionAlgorithm>> storeCombos;

  /**
   * Apply button - for applying changes to Configuration
   */
  private JButton applyButton;

  /**
   * Go button - button for starting HBase simulator
   */
  private JButton goButton;

  /**
   * creates and initializes ConfigurationFrame object
   * @param title - main title of frame
   * @param getFields - get fields map of compaction configuration
   * @param setFields - set fields map of compaction configuration
   */
  public ConfigurationFrame(final String title, final Map<String, GetMethod> getFields,
      final Map<String, SetMethod> setFields) {
    this.compactors = new ArrayList<AbstractCompactor>();
    this.storeButtons = new ArrayList<JButton>();
    this.storeCombos = new ArrayList<JComboBox<CompactionAlgorithm>>();

    // initialize all compaction algorithms, its buttons and comboboxes
    final CompactionAlgorithm[] compactionAlgorithms = CompactionAlgorithm.values();
    for (int i = 0; i < Configuration.INSTANCE.MAX_COMPACTION_ALGOS_COUNT; i++) {
      final int id = i;
      final AbstractCompactor compactor = compactionAlgorithms[0].createCompactor();
      final JButton button = new JButton(Integer.toString(i));
      final JComboBox<CompactionAlgorithm> comboBox = new JComboBox<CompactionAlgorithm>(
          compactionAlgorithms);

      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          ConfigurationFrame.this.compactors.get(id).getCompactionConfiguration()
              .showFrame(button.getText());
        }
      });
      comboBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
          ConfigurationFrame.this.compactors.set(id,
            ((CompactionAlgorithm) comboBox.getSelectedItem()).createCompactor());
        }
      });

      this.compactors.add(compactor);
      this.storeButtons.add(button);
      this.storeCombos.add(comboBox);
    }
    this.initComponents(title, getFields, setFields);
    this.updateStores();
  }

  /**
   * initialize and layout gui components
   */
  private void initComponents(final String title, final Map<String, GetMethod> getFields,
      final Map<String, SetMethod> setFields) {
    final JPanel jPanel1 = new JPanel();
    final JPanel jPanel2 = new JPanel();
    final JPanel jPanel3 = new JPanel();

    this.applyButton = new JButton();
    this.goButton = new JButton();
    this.getFields = getFields;
    this.setFields = setFields;

    this.labels = new ArrayList<JLabel>();
    this.fields = new ArrayList<JTextField>();

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setTitle(title);
    this.setResizable(false);

    jPanel1.setBackground(new java.awt.Color(165, 165, 165));
    jPanel1.setBorder(BorderFactory.createTitledBorder("Genaral configurations"));

    for (String key : getFields.keySet()) {
      this.labels.add(new JLabel(key));
      this.fields.add(new JTextField(getFields.get(key).get()));
    }

    // @formatter:off
    final GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(createParallelGroup(jPanel1Layout, this.labels, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
      .addGap(40, 40, 40)
      .addGroup(createParallelGroup(jPanel1Layout, this.fields, GroupLayout.DEFAULT_SIZE, 300, GroupLayout.PREFERRED_SIZE))
      .addContainerGap()
    );
    jPanel1Layout.setVerticalGroup(
      createSequentialGroup(jPanel1Layout, this.labels, this.fields, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    );
    // @formatter:on
    jPanel2.setBackground(new java.awt.Color(165, 165, 165));
    jPanel2.setBorder(BorderFactory.createTitledBorder("Store compaction configurations"));

    // @formatter:off
    final GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(createParallelGroup(jPanel2Layout, this.storeButtons, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
      .addGap(40, 40, 40)
      .addGroup(createParallelGroup(jPanel2Layout, this.storeCombos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
      .addContainerGap()
    );
    jPanel2Layout.setVerticalGroup(
      createSequentialGroup(jPanel2Layout, this.storeButtons, this.storeCombos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    );
    // @formatter:on

    jPanel3.setBorder(BorderFactory.createEtchedBorder());

    this.applyButton.setText("Apply");
    this.applyButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(final java.awt.event.ActionEvent evt) {
        ConfigurationFrame.this.applyActionPerformed(evt);
      }
    });

    this.goButton.setText("Go!");
    this.goButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(final java.awt.event.ActionEvent evt) {
        ConfigurationFrame.this.goActionPerformed(evt);
      }
    });

    // @formatter:off
    final GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createSequentialGroup()
      .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(this.goButton,    GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.applyButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
      .addContainerGap()
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.applyButton, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
      .addComponent(this.goButton,    GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
    );
    // @formatter:on

    // @formatter:off
    final GroupLayout layout = new GroupLayout(this.getContentPane());
    this.getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
          .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        )
      )
      .addContainerGap()
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      )
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
      .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    // @formatter:on

    this.pack();
  }

  /**
   * Method sets values from textFields to configuration
   */
  private void applyActionPerformed(final java.awt.event.ActionEvent evt) {

    // get titles of incorrectly entered by user values
    final List<String> badValues = this.getFieldsBadValues();

    if (badValues.isEmpty()) {
      this.updateStores();
      JOptionPane.showMessageDialog(this, "Applied!");
    } else {
      JOptionPane.showMessageDialog(this, "Values, you entered to fields\n"
          + badValues.toString().replace(", ", ",\n") + "\n" + "are incorrect");

      // restore correct (previous) values of fields
      for (int i = 0; i < this.labels.size(); i++) {
        String key = this.labels.get(i).getText();
        this.fields.get(i).setText(this.getFields.get(key).get());
      }
      this.updateStores();
    }
  }

  /**
   * Start simulator
   */
  private void goActionPerformed(final java.awt.event.ActionEvent evt) {
    this.goButton.setVisible(false);

    new Thread(new Runnable() {
      @Override
      public void run() {
        new Simulator(ConfigurationFrame.this.compactors.subList(0,
          (int) Configuration.INSTANCE.getCompactionAlgosCount())).go();
      }
    }).start();
  }

  /**
   * Update amount of stores, show/hide new/redundant store's compaction configurations
   */
  private void updateStores() {
    for (int i = 0; i < this.compactors.size(); i++) {
      this.storeButtons.get(i).setVisible(i < Configuration.INSTANCE.getCompactionAlgosCount());
      this.storeCombos.get(i).setVisible(i < Configuration.INSTANCE.getCompactionAlgosCount());
    }
  }
}
