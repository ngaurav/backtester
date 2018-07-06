package com.q1.bt.machineLearning.driver;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;
import org.rosuda.javaGD.GDCanvas;
import org.rosuda.javaGD.GDInterface;

public class JavaGDhandler extends GDInterface implements java.awt.event.WindowListener
{
  static String[] factorList;
  public JComboBox<String> cb;
  JFrame f;
  
  public static void setFactorList(String[] factorl)
  {
    factorList = factorl;
  }
  

  public void process(String varName)
  {
    String[] newargs1 = { "--no-save" };
    Rengine engine = Rengine.getMainEngine();
    if (engine == null) {
      engine = new Rengine(newargs1, false, null);
    }
    String command = "RankPlotLogit(mydata, Quantiles,\"" + varName + 
      "\",,,\"plo9tr\")";
    System.out.println(command);
    engine.eval(command);
  }
  


  public void gdOpen(double w, double h)
  {
    if (this.f != null)
      gdClose();
    this.f = new JFrame();
    this.f.addWindowListener(this);
    


    this.f.getContentPane().setLayout(new java.awt.BorderLayout(0, 0));
    
    JPanel panel = new JPanel();
    this.f.getContentPane().add(panel, "North");
    panel.setLayout(new FlowLayout(1, 5, 5));
    
    JLabel lblFactor = new JLabel("Factor");
    panel.add(lblFactor);
    
    this.cb = new JComboBox(factorList);
    
    this.cb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        String selectedFactor = (String)JavaGDhandler.this.cb.getSelectedItem();
        JavaGDhandler.this.process(selectedFactor);
      }
    });
    panel.add(this.cb);
    
    this.c = new GDCanvas(w, h);
    this.f.add((GDCanvas)this.c);
    
    this.f.getContentPane().add((Component)this.c, "Center");
    this.f.pack();
    this.f.setVisible(true);
    this.f.setTitle("Rank Plot");
    this.f.setDefaultCloseOperation(2);
    this.f.setExtendedState(this.f.getExtendedState() | 0x6);
    this.f.repaint();
    this.f.revalidate();
  }
  

  public void gdClose()
  {
    super.gdClose();
    if (this.f != null) {
      this.c = null;
      this.f.removeAll();
      this.f.dispose();
      this.f = null;
    }
  }
  



  public void windowClosing(WindowEvent e)
  {
    if (this.c != null) {
      executeDevOff();
    }
  }
  
  public void windowClosed(WindowEvent e) {}
  
  public void windowOpened(WindowEvent e) {
    this.cb.setSelectedIndex(0);
    String selectedFactor = (String)this.cb.getSelectedItem();
    process(selectedFactor);
  }
  
  public void windowIconified(WindowEvent e) {}
  
  public void windowDeiconified(WindowEvent e) {}
  
  public void windowActivated(WindowEvent e) {}
  
  public void windowDeactivated(WindowEvent e) {}
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/JavaGDhandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */