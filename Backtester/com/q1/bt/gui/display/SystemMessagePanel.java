package com.q1.bt.gui.display;

import com.q1.bt.global.BacktesterGlobal;
import com.q1.math.DateTime;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;





public class SystemMessagePanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private JLabel messageLabel;
  private JScrollPane messagePanel;
  private JTable messageTable;
  
  public SystemMessagePanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBounds(0, 0, 405, 310);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.messageLabel = new JLabel("System Messages");
    this.messageLabel.setBounds(0, 0, 405, 25);
    add(this.messageLabel);
    this.messageLabel.setHorizontalAlignment(0);
    this.messageLabel.setFont(new Font("SansSerif", 1, 12));
    this.messageLabel.setBorder(new EtchedBorder(1, null, 
      null));
    
    this.messagePanel = new JScrollPane();
    this.messagePanel.setBounds(0, 27, 405, 283);
    add(this.messagePanel);
    this.messagePanel.setBorder(new EtchedBorder(1, null, 
      null));
    
    this.messageTable = new JTable();
    this.messageTable.setGridColor(Color.LIGHT_GRAY);
    this.messageTable.setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Timestamp", "Message" })
      {
        boolean[] columnEditables = new boolean[2];
        
        public boolean isCellEditable(int row, int column) {
          return this.columnEditables[column];
        }
      });
    this.messageTable.getColumnModel().getColumn(0).setPreferredWidth(63);
    this.messageTable.getColumnModel().getColumn(0).setMaxWidth(100);
    this.messageTable.getColumnModel().getColumn(1).setPreferredWidth(260);
    this.messageTable.getColumnModel().getColumn(1)
      .setCellRenderer(new wrapCellRenderer());
    
    this.messagePanel.setViewportView(this.messageTable);
  }
  
  public class wrapCellRenderer extends JTextArea implements TableCellRenderer
  {
    public wrapCellRenderer()
    {
      setLineWrap(true);
      setWrapStyleWord(true);
    }
    

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      setText(value.toString());
      setFont(new Font("Tahoma", 0, 11));
      setSize(table.getColumnModel().getColumn(column).getWidth(), 
        getPreferredSize().height);
      if (table.getRowHeight(row) != getPreferredSize().height) {
        table.setRowHeight(row, getPreferredSize().height);
      }
      return this;
    }
  }
  
  public void displayMessage(String message)
  {
    DefaultTableModel messageModel = (DefaultTableModel)this.messageTable
      .getModel();
    String[] data = { DateTime.getCurrentTime(), message };
    try {
      messageModel.insertRow(0, data);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println(message + ": Could not be printed to table");
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/display/SystemMessagePanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */