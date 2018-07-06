package com.q1.bt.global;

import com.q1.bt.gui.display.LastRunPanel;
import com.q1.bt.gui.display.SystemMessagePanel;
import com.q1.bt.gui.main.BacktestPanel;
import com.q1.bt.gui.main.BatchProcessPanel;
import com.q1.bt.gui.main.LoginPanel;
import com.q1.bt.gui.main.MachineLearningPanel;
import com.q1.bt.gui.main.OOSAnalysisPanel;
import com.q1.bt.gui.main.PostProcessPanel;
import com.q1.bt.gui.main.ResultsPanel;
import com.q1.bt.gui.main.RollingAnalysisPanel;
import com.q1.bt.gui.main.SensitivityAnalysisPanel;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.PackageParameter;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;



public class BacktesterGUI
  extends JFrame
{
  private String version = "8.17.2";
  
  BacktesterGlobal btGlobal;
  
  private JPanel contentPane;
  
  private JPanel mainPanel;
  
  private JPanel displayPanel;
  
  private JTabbedPane btTabbedPane;
  
  public LoginPanel loginPanel;
  
  public BacktestPanel backtestPanel;
  
  public BatchProcessPanel batchProcessPanel;
  
  public MachineLearningPanel mlPanel;
  
  public ResultsPanel resultsPanel;
  
  public RollingAnalysisPanel rollingAnalysisPanel;
  
  public OOSAnalysisPanel isOsPanel;
  
  public SensitivityAnalysisPanel sensitivityPanel;
  
  public PostProcessPanel postPanel;
  public SystemMessagePanel sysMsgPanel;
  public LastRunPanel lRunPanel;
  LoginParameter loginParameter;
  PackageParameter packageParameter;
  
  public void runGUI()
    throws Exception
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e1)
        {
          e1.printStackTrace();
        }
        
        try
        {
          BacktesterGUI frame = new BacktesterGUI(BacktesterGUI.this.loginParameter, BacktesterGUI.this.packageParameter);
          

          frame.setVisible(true);
          frame.setLocationRelativeTo(null);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  

  public BacktesterGUI(LoginParameter loginParameter, PackageParameter packageParameter)
    throws Exception
  {
    this.loginParameter = loginParameter;
    this.packageParameter = packageParameter;
    this.btGlobal = new BacktesterGlobal(this, loginParameter, packageParameter);
    

    addWindowListener(new WindowAdapter()
    {

      public void windowOpened(WindowEvent arg0)
      {
        for (Integer idx : ProcessFlow.tabIndexMap.values()) {
          BacktesterGUI.this.btTabbedPane.setEnabledAt(idx.intValue(), false);
        }
        
        BacktesterGUI.this.btTabbedPane.setEnabledAt(0, true);

      }
      

    });
    setTitle("Backtester v" + this.version);
    setDefaultCloseOperation(3);
    setBounds(100, 100, 1261, 720);
    

    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout(null);
    

    this.mainPanel = new JPanel();
    this.mainPanel.setBounds(10, 6, 814, 665);
    this.contentPane.add(this.mainPanel);
    this.mainPanel.setLayout(null);
    

    this.btTabbedPane = new JTabbedPane(1);
    this.btTabbedPane.setBounds(0, 0, 814, 665);
    this.mainPanel.add(this.btTabbedPane);
    

    this.loginPanel = new LoginPanel(this.btGlobal, this.version);
    this.btTabbedPane.addTab("Login", null, this.loginPanel, null);
    

    this.backtestPanel = new BacktestPanel(this.btGlobal);
    this.btTabbedPane.addTab("Backtest", null, this.backtestPanel, null);
    

    this.batchProcessPanel = new BatchProcessPanel(this.btGlobal);
    this.btTabbedPane.addTab("Batch Process", null, this.batchProcessPanel, null);
    

    this.mlPanel = new MachineLearningPanel(this.btGlobal);
    this.btTabbedPane.addTab("Machine Learning", null, this.mlPanel, null);
    

    this.resultsPanel = new ResultsPanel(this.btGlobal);
    this.btTabbedPane.addTab("Results", null, this.resultsPanel, null);
    

    this.rollingAnalysisPanel = new RollingAnalysisPanel(this.btGlobal);
    this.btTabbedPane.addTab("Rolling Analysis", null, this.rollingAnalysisPanel, null);
    

    this.sensitivityPanel = new SensitivityAnalysisPanel(this.btGlobal);
    this.btTabbedPane.addTab("Sensitivity Analysis", null, this.sensitivityPanel, null);
    

    this.isOsPanel = new OOSAnalysisPanel(this.btGlobal);
    this.btTabbedPane.addTab("InSample OOS Analysis", null, this.isOsPanel, null);
    

    this.postPanel = new PostProcessPanel(this.btGlobal);
    this.btTabbedPane.addTab("Post Process", null, this.postPanel, null);
    

    this.displayPanel = new JPanel();
    this.displayPanel.setBounds(830, 6, 405, 665);
    this.contentPane.add(this.displayPanel);
    this.displayPanel.setLayout(null);
    

    this.sysMsgPanel = new SystemMessagePanel(this.btGlobal);
    this.displayPanel.add(this.sysMsgPanel);
    

    this.lRunPanel = new LastRunPanel(this.btGlobal);
    this.displayPanel.add(this.lRunPanel);
    

    this.btGlobal.processFlow.add(BacktesterProcess.Login);
    this.btGlobal.initializeProcess();
  }
  

  public void shiftTab(int tabIndex)
  {
    this.btTabbedPane.setEnabledAt(tabIndex, true);
    this.btTabbedPane.setEnabledAt(this.btTabbedPane.getSelectedIndex(), false);
    this.btTabbedPane.setSelectedIndex(tabIndex);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterGUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */