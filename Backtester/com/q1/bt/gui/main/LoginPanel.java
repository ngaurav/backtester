package com.q1.bt.gui.main;

import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.parameter.LoginParameter;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;









public class LoginPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  public JLabel btVersionLabel;
  public JPanel mainPanel;
  public JTextField usernameText;
  public JLabel usernameLabel;
  public JLabel dataPathLabel;
  public JTextField dataPathText;
  public JButton dataPathBrowseButton;
  public JLabel passwordLabel;
  public JPasswordField passwordText;
  public JLabel capitalLabel;
  public JTextField capitalText;
  public JTextField rptText;
  public JLabel rptLabel;
  public JComboBox<BacktesterProcess> modeBox;
  public JLabel modeLabel;
  public JButton loginButton;
  String version;
  
  public LoginPanel(BacktesterGlobal btGlobal, String version)
  {
    this.btGlobal = btGlobal;
    
    this.version = version;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.btVersionLabel = new JLabel("Backtester v" + this.version);
    this.btVersionLabel.setHorizontalAlignment(0);
    this.btVersionLabel.setFont(new Font("SansSerif", 1, 16));
    this.btVersionLabel.setBorder(new EtchedBorder(1, null, null));
    this.btVersionLabel.setBounds(74, 45, 620, 51);
    add(this.btVersionLabel);
    
    this.mainPanel = new JPanel();
    this.mainPanel.setBorder(new EtchedBorder(1, null, null));
    this.mainPanel.setBounds(74, 100, 620, 411);
    add(this.mainPanel);
    this.mainPanel.setLayout(null);
    
    this.usernameLabel = new JLabel("SQL Username");
    this.usernameLabel.setBounds(79, 74, 97, 29);
    this.mainPanel.add(this.usernameLabel);
    
    this.usernameText = new JTextField();
    this.usernameText.setText("root");
    this.usernameText.setBounds(168, 75, 271, 28);
    this.mainPanel.add(this.usernameText);
    this.usernameText.setColumns(10);
    
    this.passwordLabel = new JLabel("SQL Password");
    this.passwordLabel.setBounds(79, 119, 97, 29);
    this.mainPanel.add(this.passwordLabel);
    
    this.loginButton = new JButton("Login");
    this.loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        try {
          LoginPanel.this.runLogin();
        }
        catch (UnknownHostException e) {
          e.printStackTrace();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    this.loginButton.setFont(new Font("SansSerif", 1, 12));
    this.loginButton.setBounds(242, 349, 116, 43);
    this.mainPanel.add(this.loginButton);
    
    this.dataPathLabel = new JLabel("Data Path");
    this.dataPathLabel.setBounds(79, 164, 64, 29);
    this.mainPanel.add(this.dataPathLabel);
    
    this.dataPathText = new JTextField();
    this.dataPathText.setColumns(10);
    this.dataPathText.setBounds(168, 164, 271, 28);
    this.mainPanel.add(this.dataPathText);
    
    this.dataPathBrowseButton = new JButton("BROWSE");
    this.dataPathBrowseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select a data path");
        chooser.setFileSelectionMode(1);
        if (chooser.showOpenDialog(null) == 0) {
          String dataPath = chooser.getCurrentDirectory().getPath() + "\\" + 
            chooser.getSelectedFile().getName();
          LoginPanel.this.dataPathText.setText(dataPath);
        }
      }
    });
    this.dataPathBrowseButton.setBounds(470, 164, 89, 29);
    this.mainPanel.add(this.dataPathBrowseButton);
    
    this.passwordText = new JPasswordField();
    this.passwordText.setBounds(168, 119, 271, 28);
    this.mainPanel.add(this.passwordText);
    
    this.capitalLabel = new JLabel("Capital");
    this.capitalLabel.setBounds(79, 204, 82, 29);
    this.mainPanel.add(this.capitalLabel);
    
    this.capitalText = new JTextField();
    this.capitalText.setText("500000.0");
    this.capitalText.setColumns(10);
    this.capitalText.setBounds(168, 204, 271, 28);
    this.mainPanel.add(this.capitalText);
    
    this.rptLabel = new JLabel("Risk Per Trade(%)");
    this.rptLabel.setBounds(79, 247, 97, 29);
    this.mainPanel.add(this.rptLabel);
    
    this.rptText = new JTextField();
    this.rptText.setText("1");
    this.rptText.setColumns(10);
    this.rptText.setBounds(168, 247, 271, 28);
    this.mainPanel.add(this.rptText);
    
    this.modeBox = new JComboBox();
    this.modeBox.setModel(new DefaultComboBoxModel(
      new BacktesterProcess[] { BacktesterProcess.Backtest, BacktesterProcess.SensitivityAnalysis }));
    this.modeBox.setBounds(168, 287, 271, 29);
    this.mainPanel.add(this.modeBox);
    
    this.modeLabel = new JLabel("Mode");
    this.modeLabel.setBounds(79, 287, 82, 29);
    this.mainPanel.add(this.modeLabel);
  }
  


  public void runLogin()
    throws Exception
  {
    this.btGlobal.loginParameter.setSqlUsername(this.usernameText.getText());
    this.btGlobal.loginParameter.setSqlPassword(this.passwordText.getText());
    this.btGlobal.loginParameter.setDataPath(this.dataPathText.getText());
    this.btGlobal.loginParameter.setCapital(Double.valueOf(Double.parseDouble(this.capitalText.getText())));
    this.btGlobal.loginParameter.setRiskPerTrade(Double.valueOf(Double.parseDouble(this.rptText.getText())));
    
    if (!InetAddress.getByName(this.btGlobal.loginParameter.getSqlIPAddress()).isReachable(3000)) {
      this.btGlobal.displayMessage("SQL Server for ip Address: " + this.btGlobal.loginParameter.getSqlIPAddress() + 
        " not connected. Please check.");
      throw new Exception();
    }
    

    this.btGlobal.processFlow.add((BacktesterProcess)this.modeBox.getSelectedItem());
    this.btGlobal.processFlow.update();
    

    this.btGlobal.shiftTab();
    

    this.btGlobal.initializeProcess();
    

    this.btGlobal.displayMessage("Succesfully Logged In.");
  }
  
  public void initialize(LoginParameter loginParameter)
  {
    this.usernameText.setText(loginParameter.getSqlUsername());
    this.passwordText.setText(loginParameter.getSqlPassword());
    this.modeBox.setSelectedItem(loginParameter.getProcess());
    this.dataPathText.setText(loginParameter.getDataPath());
    this.capitalText.setText(loginParameter.getCapital().toString());
    this.rptText.setText(loginParameter.getRiskPerTrade().toString());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/LoginPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */