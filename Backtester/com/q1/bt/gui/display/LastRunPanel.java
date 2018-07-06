package com.q1.bt.gui.display;

import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.csv.CSVReader;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;








public class LastRunPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private JLabel lrLabel;
  private JScrollPane lrScrollPane;
  private JTable lrTable;
  
  public LastRunPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBounds(0, 315, 405, 350);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.lrLabel = new JLabel("Last Run Parameters");
    this.lrLabel.setBounds(0, 0, 405, 25);
    add(this.lrLabel);
    this.lrLabel.setHorizontalAlignment(0);
    this.lrLabel.setFont(new Font("SansSerif", 1, 12));
    this.lrLabel.setBorder(new EtchedBorder(1, null, 
    
      null));
    
    this.lrScrollPane = new JScrollPane();
    this.lrScrollPane.setBounds(0, 25, 405, 325);
    add(this.lrScrollPane);
    this.lrScrollPane.setBorder(new EtchedBorder(1, null, 
    
      null));
    
    this.lrTable = new JTable();
    this.lrTable.setModel(new DefaultTableModel(new Object[0][], new String[] {
      "Strategy", "Parameter", "Value" }));
    this.lrScrollPane.setViewportView(this.lrTable);
  }
  

  public void displayRunParameters(String timeStamp)
    throws IOException
  {
    String paramPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + 
      "/Parameters";
    
    File paramPathFile = new File(paramPath);
    File[] paramFiles = paramPathFile.listFiles();
    
    DefaultTableModel model = (DefaultTableModel)this.lrTable
      .getModel();
    model.setRowCount(0);
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = paramFiles).length; for (int i = 0; i < j; i++) { File pFile = arrayOfFile1[i];
      String[] pVal = pFile.getName().split("\\.")[0].split(" ");
      if (pVal[1].equals("Parameters"))
      {
        CSVReader reader = new CSVReader(paramPath + "/" + pFile.getName(), 
          ',', 0);
        
        String[] paramData;
        while ((paramData = reader.getLine()) != null) { String[] paramData;
          String[] rowData = { pVal[0], paramData[0], paramData[1] };
          model.addRow(rowData);
        }
      }
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/display/LastRunPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */