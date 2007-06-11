package petrieditor.modules.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

public class SimulationDialog extends javax.swing.JDialog {
    private boolean result;
    private ButtonGroup buttonGroup1;
    private JButton okButton;
    private JButton anulujButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JSpinner jSpinner1;    
    
    public SimulationDialog(java.awt.Frame parent, boolean modal) {        
        super(parent, modal);        
        setLocationRelativeTo(null);
        initComponents();
    }
    
    private void initComponents() {
        buttonGroup1 = new ButtonGroup();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jSpinner1 = new JSpinner();
        okButton = new JButton();
        anulujButton = new JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Opcje symulacji");
        setResizable(false);
        jSpinner1.setValue(100);
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setText("Ilo\u015b\u0107 odpale\u0144 tranzycji:");

        jLabel2.setText("Spos\u00f3b odpalania tranzycji:");

        jRadioButton1.setText("Losowo");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonGroup1.add(jRadioButton1);

        jRadioButton2.setText("O najni\u017cszym numerze");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonGroup1.add(jRadioButton2);
        
        jRadioButton1.setSelected(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = true;
                setVisible(false);
            }
            
        });

        anulujButton.setText("Anuluj");
        anulujButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = false;
                setVisible(false);
            }
            
        });        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(anulujButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(anulujButton))
                .addContainerGap())
        );
        pack();
        
    }                   

    boolean getResult() {
        return result;
    }

    int getIloscOdpalen() {
        return (Integer)jSpinner1.getValue();
    }

    boolean getLosoweOdpalenia() {
        return jRadioButton1.isSelected();
    }
    
}
