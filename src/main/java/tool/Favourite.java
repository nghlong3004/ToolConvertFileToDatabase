package tool;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

public class Favourite extends JPanel {

  private static final long serialVersionUID = 1L;

  public Favourite() {
    
    JPanel panel = new JPanel();
    panelHeader(panel);
    
    JPanel panel_1 = new JPanel();
    
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE))
          .addGap(16))
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(panel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
          .addGap(17))
    );
    setLayout(groupLayout);
    
  }
  
  private void panelHeader(JPanel panel) {
    panel.setLayout(new MigLayout("fill"));
    JLabel lblNumber = new JLabel("0");
    lblNumber.setHorizontalAlignment(SwingConstants.CENTER);
    lblNumber.setHorizontalTextPosition(SwingConstants.CENTER);
    JLabel lblDescription = new JLabel("Mục yêu thích");
    JTextField txtSearch = new JTextField();
    JButton cmdDelete = new JButton("X");
    panel.add(lblNumber, "dock west, width 20!");
    JPanel smallPanel = new JPanel();
    smallPanel.setLayout(new MigLayout("fill"));
    smallPanel.add(lblDescription, "dock west");
    smallPanel.add(txtSearch, "grow, center, height 30!, gapx 10");
    panel.add(smallPanel, "grow");
    panel.add(cmdDelete, "east, width 30!, height 30!, center, gapx 5");
  }
  
  

}
