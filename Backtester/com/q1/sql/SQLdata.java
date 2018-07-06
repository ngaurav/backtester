package com.q1.sql;

import com.q1.math.DateTime;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;



public class SQLdata
{
  Connection conn;
  Statement stmt;
  
  public SQLdata(String ipAddress, String database, String username, String password)
    throws ClassNotFoundException, SQLException
  {
    String dbClassName = "com.mysql.jdbc.Driver";
    String CONNECTION;
    String CONNECTION; if (database == null) {
      CONNECTION = "jdbc:mysql://" + ipAddress + "/?autoReconnect=true";
    } else {
      CONNECTION = "jdbc:mysql://" + ipAddress + "/" + database + "?autoReconnect=true";
    }
    
    Class.forName(dbClassName);
    

    Properties p = new Properties();
    p.put("user", username);
    p.put("password", password);
    

    this.conn = DriverManager.getConnection(CONNECTION, p);
    this.stmt = this.conn.createStatement();
  }
  
  public void close()
  {
    try {
      this.stmt.close();
      this.conn.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }
  

  public void backupDatabase(String inDatabase, String outDatabase, String inTable, String timeTable, String key)
    throws ClassNotFoundException, SQLException
  {
    Integer newKey = Integer.valueOf(Integer.parseInt(key) % 5 + 1);
    key = newKey.toString();
    
    String SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + 
      "`.`backup_" + key + "`='" + DateTime.getCurrentDate() + " " + DateTime.getCurrentTime() + 
      " Incomplete" + "'";
    this.stmt.executeUpdate(SQL);
    

    SQL = "DELETE FROM `" + outDatabase + "`.`backup_" + key + "`" + " WHERE 1";
    this.stmt.executeUpdate(SQL);
    SQL = "INSERT INTO `" + outDatabase + "`.`backup_" + key + "` SELECT * FROM `" + inDatabase + "`.`" + inTable + 
      "`";
    this.stmt.executeUpdate(SQL);
    SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + "`.`Key`='" + 
      key + "'";
    this.stmt.executeUpdate(SQL);
    SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + "`.`backup_" + 
      key + "`='" + DateTime.getCurrentDate() + " " + DateTime.getCurrentTime() + " Complete" + "'";
    this.stmt.executeUpdate(SQL);
  }
  



  public void cleanDatabase(SQLdata sqlObject, String inDatabase, String outDatabase, String outTable)
    throws ClassNotFoundException, SQLException
  {
    String outDbTable = "`" + outDatabase + "`.`" + outTable + "`";
    String inDbTable = "`" + inDatabase + "`.`" + "clean_backup`";
    
    ArrayList<String> fieldList = sqlObject.getTableFields("clean_backup");
    

    String SQL = "UPDATE " + outDbTable + ", " + inDbTable + " SET ";
    for (String field : fieldList) {
      SQL = SQL + outDbTable + ".`" + field + "` = " + inDbTable + ".`" + field + "`,";
    }
    SQL = SQL.substring(0, SQL.length() - 1) + " WHERE ";
    SQL = SQL + inDbTable + ".`Scrip ID` = " + outDbTable + ".`Scrip ID` AND " + inDbTable + ".`Segment ID` = " + 
      outDbTable + ".`Segment ID` AND " + inDbTable + ".`Account ID` = " + outDbTable + ".`Account ID`;";
    
    this.stmt.executeUpdate(SQL);
  }
  
  public void sendTableEntry(String table, Object[] tableEntry)
    throws Exception
  {
    String SQL = "INSERT INTO `" + table + 
      "`(`Timestamp`, `Scrip`, `Segment`, `Side`, `Type`, `Trigger`, `Action`) VALUES ('" + 
      tableEntry[0].toString() + "','" + tableEntry[1].toString() + "','" + tableEntry[2].toString() + "','" + 
      tableEntry[3].toString() + "','" + tableEntry[4].toString() + "'," + tableEntry[5].toString() + ",'" + 
      tableEntry[6].toString() + "')";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  
  public void deleteTableEntries(String table, Object[] tableEntry)
    throws Exception
  {
    String SQL = "DELETE FROM `" + table + "` WHERE `Scrip`='" + tableEntry[1].toString() + "' AND `Segment`='" + 
      tableEntry[2].toString() + "' AND `Side`='" + tableEntry[3].toString() + "' AND `Type`='" + 
      tableEntry[4].toString() + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  
  public void clearTable(String table)
    throws Exception
  {
    String SQL = "DELETE FROM `" + table + "` WHERE 1";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  
  public ArrayList<Object[]> getMessageTableEntries(String table) throws Exception
  {
    ArrayList<Object[]> output = new ArrayList();
    

    String SQL = "SELECT * FROM `" + table + "` WHERE 1";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      Object[] tempOut = { rs.getString(1), rs.getString(2) };
      output.add(tempOut);
    }
    rs.close();
    
    return output;
  }
  
  public ArrayList<Object[]> getTableEntries(String table) throws Exception
  {
    ArrayList<Object[]> output = new ArrayList();
    

    String SQL = "SELECT * FROM `" + table + "` WHERE 1";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      Object[] tempOut = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
        Double.valueOf(rs.getDouble(6)), rs.getString(7) };
      output.add(tempOut);
    }
    rs.close();
    
    return output;
  }
  


  public void sendConsoleMessage(String table, String timestamp, String message)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "INSERT INTO `" + table + "`(`Timestamp`, `Message`) VALUES ('" + timestamp + "','" + message + 
      "')";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public ArrayList<String> getTableFields(String table)
  {
    ArrayList<String> output = new ArrayList();
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + table + "';";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          int i = 1;
          if (rs.getString(i) != null)
            output.add(rs.getString(i++));
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  


  public int getPromptTableCount(String table, String scrip, String segment, String side, String type)
  {
    int output = 1;
    try
    {
      String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
        "' and `Side`='" + side + "' and `Type`='" + type + "'";
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  

  public int getInstructionTableCountForInstructionID(String table, Integer instructionID)
  {
    int output = 1;
    try
    {
      String SQL = "select count(*) from `" + table + "` where `Instruction ID`=" + instructionID;
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  

  public int getInstructionTableCountForScrip(String table, String scrip, String segment)
  {
    int output = 0;
    try
    {
      String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
        "'";
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  

  public int getInstructionTableCountForValue(String table, String value)
  {
    int output = 0;
    try
    {
      String SQL = "select count(*) from `" + table + "` where `Scrip`='" + value + "'";
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  

  public int getInstructionTableCount(String table)
  {
    int output = 1;
    try
    {
      String SQL = "select count(*) from `" + table + "` where 1";
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  
  public int getInstructionTableMax(String table, String account)
    throws Exception
  {
    int output = 1;
    
    String SQL = "select `Instruction Max` from `" + table + "` where `Account ID`='" + account + "'";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      output = rs.getInt(1);
    }
    rs.close();
    
    return output;
  }
  


  public int getPromptTableCountWithPrice(String table, String scrip, String segment, String side, String type, Double price)
  {
    int output = 1;
    try
    {
      String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
        "' and `Side`='" + side + "' and `Type`='" + type + "' and `Trigger`=" + price;
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next()) {
        output = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception localException) {}
    
    return output;
  }
  

  public int getTableCount(String table)
  {
    int output = 0;
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "select count(*) from `" + table + "`";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getInt(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  


  public void setStringVal(String table, String keyName, String keyValue, String value, String entry)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
      "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  



  public void setDoubleVal(String table, String keyName, String keyValue, String value, double entry)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
      "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void setIntVal(String table, String keyName, String keyValue, String value, int entry)
    throws Exception
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
      "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void setIntValWithoutKey(String table, String value, int entry)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE 1";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  



  public void setDoubleValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, double entry)
    throws Exception
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
      "' AND `" + keyName2 + "`='" + keyValue2 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void resetCheckTable(String checkTable)
    throws Exception
  {
    String SQL = "UPDATE `" + checkTable + 
      "` SET `Buy Entry Trade`=0,`Sell Entry Trade`=0,`Confirm Buy Entry Trade`=0,`Confirm Sell Entry Trade`=0,`Confirm Buy Exit Order`=0,`Confirm Sell Exit Order`=0,`Buy Exit Trade`=0,`Sell Exit Trade`=0,`Confirm Buy Exit Trade`=0,`Confirm Sell Exit Trade`=0,`Modify Buy Exit Order`=0,`Modify Sell Exit Order`=0,`Modify Buy Entry Order`=0,`Modify Sell Entry Order`=0,`Confirm Buy Entry Order`=0,`Confirm Sell Entry Order`=0,`Place Buy Entry Order`=0,`Place Sell Entry Order`=0,`Place Buy Exit Order`=0,`Place Sell Exit Order`=0,`Check Buy Entry Order`=0,`Check Sell Entry Order`=0 WHERE 1";
    this.stmt = this.conn.createStatement();
    this.stmt.executeUpdate(SQL);
  }
  


  public void resetAccountTable(String accountTable)
    throws Exception
  {
    String SQL = "UPDATE `" + accountTable + 
      "` SET `State`=0,`NOW Status`=0,`Purge Status`=0,`Client Value`=0,`Client Status`=0,`Client Last Update`='00:00:00',`Tradebook Last Update`='00:00:00',`Instruction Max`=0,`Equity State`=0,`Comex State`=0 WHERE 1";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void resetParamTable(String paramsTable)
    throws Exception
  {
    String SQL = "UPDATE `" + paramsTable + 
      "` SET `State`=0,`Buy Entry OID`='',`Sell Entry OID`='',`Buy Exit OID`='',`Sell Exit OID`='',`Buy Entry Price`=0,`Sell Entry Price`=0,`Buy Exit Price`=0,`Sell Exit Price`=0,`Buy Entry OStatus`='',`Sell Entry OStatus`='',`Buy Exit OStatus`='',`Sell Exit OStatus`='',`Buy Entry Qty`=0,`Sell Entry Qty`=0,`Buy Exit Qty`=0,`Sell Exit Qty`=0,`Last Purge`='01-Jan-1970 00:00:00',`M2M`=0,`Position`=0 WHERE 1";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void resetScripTable(String scripTable)
    throws Exception
  {
    String SQL = "UPDATE `" + scripTable + 
      "` SET `Buy Entry Trigger`=0.0,`Sell Entry Trigger`=0.0,`Buy Exit Trigger`=0.0,`Sell Exit Trigger`=0.0,`State`=0,`High`=0,`Low`=1000000,`Lower Limit`=0,`Upper Limit`=1000000 WHERE 1";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void resetParamTableForAccScrip(String paramsTable, String account, String scrip, String segment)
    throws Exception
  {
    String SQL = "UPDATE `" + paramsTable + 
      "` SET `Buy Entry OID`='',`Sell Entry OID`='',`Buy Exit OID`='',`Sell Exit OID`='',`Buy Entry Price`=0,`Sell Entry Price`=0,`Buy Exit Price`=0,`Sell Exit Price`=0,`Buy Entry OStatus`='',`Sell Entry OStatus`='',`Buy Exit OStatus`='',`Sell Exit OStatus`='',`Buy Entry Qty`=0,`Sell Entry Qty`=0,`Buy Exit Qty`=0,`Sell Exit Qty`=0,`Position`=0,`M2M`=0.0 WHERE `Account ID`='" + 
      account + "' AND `Scrip ID`='" + scrip + "' AND `Segment ID`='" + segment + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public void setIntValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, int entry)
    throws Exception
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
      "' AND `" + keyName2 + "`='" + keyValue2 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  



  public void setStringValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, String entry)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = '" + entry + "' WHERE `" + keyName1 + "`='" + 
      keyValue1 + "' AND `" + keyName2 + "`='" + keyValue2 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  



  public void setStringValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, String entry)
    throws Exception
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = '" + entry + "' WHERE `" + keyName1 + "`='" + 
      keyValue1 + "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  




  public void setDoubleValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, double entry)
    throws ClassNotFoundException, SQLException
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
      "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  



  public void setIntValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, int entry)
    throws Exception
  {
    String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
      "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  


  public String getStringVal(String table, String keyName, String keyValue, String value)
  {
    String output = "";
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getString(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  

  public String getStringValwithoutKey(String table, String value)
  {
    String output = "";
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE 1";
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getString(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  
  public int getIntValwithoutKey(String table, String value)
    throws Exception
  {
    int output = 0;
    

    String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE 1";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      output = rs.getInt(1);
    }
    rs.close();
    
    return output;
  }
  
  public double getDoubleVal(String table, String keyName, String keyValue, String value)
  {
    double output = 0.0D;
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getDouble(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  
  public int getIntVal(String table, String keyName, String keyValue, String value)
    throws Exception
  {
    int output = 0;
    
    String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
    
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      output = rs.getInt(1);
    }
    rs.close();
    return output;
  }
  

  public double getDoubleValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
  {
    double output = 0.0D;
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
          "' AND `" + keyName2 + "`='" + keyValue2 + "'";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getDouble(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
      }
    }
    




    return output;
  }
  

  public int getIntValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
  {
    int output = 0;
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
          "' AND `" + keyName2 + "`='" + keyValue2 + "'";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getInt(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    return output;
  }
  

  public int getIntValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
  {
    int output = 0;
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
          "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getInt(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    return output;
  }
  

  public double getDoubleValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
    throws SQLException
  {
    double output = 0.0D;
    



    String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + "' AND `" + 
      keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
    
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      output = rs.getDouble(1);
    }
    rs.close();
    








    return output;
  }
  


  public String getStringValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
  {
    String output = "";
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
          "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getString(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in reading from SQL. Retrying...");
      }
    }
    
    return output;
  }
  


  public String getStringValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
  {
    String output = "";
    
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
          "' AND `" + keyName2 + "`='" + keyValue2 + "'";
        
        ResultSet rs = this.stmt.executeQuery(SQL);
        while (rs.next()) {
          output = rs.getString(1);
        }
        rs.close();
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
      }
    }
    




    return output;
  }
  
  public ArrayList<String> getStringArrayList(String table, String Key)
    throws Exception
  {
    ArrayList<String> arrayLst = new ArrayList();
    

    String SQL = "SELECT `" + Key + "` FROM `" + table + "` WHERE 1";
    
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      arrayLst.add(rs.getString(1));
    }
    
    return arrayLst;
  }
  

  public ArrayList<String[]> getStringArrayArrayList(String table, String[] Key)
    throws ClassNotFoundException, SQLException
  {
    ArrayList<String[]> arrayLst = new ArrayList();
    

    String SQL = "SELECT `" + Key[0] + "`,`" + Key[1] + "` FROM `" + table + "` WHERE 1";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      String[] array = new String[2];
      array[0] = rs.getString(1);
      array[1] = rs.getString(2);
      arrayLst.add(array);
    }
    return arrayLst;
  }
  

  public ArrayList<String[]> getStringArrayArrayListWithThreeKeys(String table, String[] outKeys, String[] inKeys, String[] inKeyVals)
    throws Exception
  {
    ArrayList<String[]> arrayLst = new ArrayList();
    

    String SQL = "SELECT `" + outKeys[0] + "`,`" + outKeys[1] + "` FROM `" + table + "` WHERE `" + inKeys[0] + "`='" + 
      inKeyVals[0] + "' AND `" + inKeys[1] + "`='" + inKeyVals[1] + "' AND `" + inKeys[2] + "`='" + 
      inKeyVals[2] + "'";
    ResultSet rs = this.stmt.executeQuery(SQL);
    while (rs.next()) {
      String[] array = new String[2];
      array[0] = rs.getString(1);
      array[1] = rs.getString(2);
      arrayLst.add(array);
    }
    return arrayLst;
  }
  
  public void deleteRowFromTable(String table, String key, String keyVal) {
    int catchFlag = 1;
    while (catchFlag == 1) {
      try
      {
        String SQL = "DELETE FROM `" + table + "` WHERE `" + key + "`='" + keyVal + "'";
        this.stmt.executeUpdate(SQL);
        catchFlag = 0;
      } catch (Exception e) {
        catchFlag = 1;
        e.printStackTrace();
        System.out.println("Exception caught in deleting from SQL. Retrying...");
      }
    }
  }
  

  public void sendOrderToDatabase(String table, String[] order, String account, String scrip, String segment)
    throws Exception
  {
    String SQL = "INSERT INTO `" + table + 
      "` (`Account ID`, `Scrip ID`, `Segment ID`, `Timestamp`, `Order ID`) VALUES ('" + account + "' ,'" + 
      scrip + "','" + segment + "','" + order[0] + "','" + order[1] + "')";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
  
  public int sendInstructionToDatabase(String account, String accountTable, String timeStamp, int type, String params, String scrip, String segment)
    throws Exception
  {
    String table = "instructions_" + account.toLowerCase();
    int tableCount = getInstructionTableMax(accountTable, account) + 1;
    setIntVal(accountTable, "Account ID", account, "Instruction Max", tableCount);
    
    String SQL = "INSERT INTO `" + table + 
      "` (`Timestamp`, `Instruction ID`, `Type`, `Parameters`, `Scrip`, `Segment`) VALUES ('" + timeStamp + 
      "'," + tableCount + ",'" + type + "','" + params + "','" + scrip + "','" + segment + "')";
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
    return tableCount;
  }
  
  public String[] getOldestInstruction(String account)
    throws Exception
  {
    String[] instruction = null;
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    Date minDate = df.parse("01-May-3101 09:10:00");
    
    try
    {
      String SQL = "SELECT `Timestamp`, `Instruction ID`, `Type`, `Parameters` FROM `instructions_" + account + 
        "` WHERE 1";
      ResultSet rs = this.stmt.executeQuery(SQL);
      while (rs.next())
      {
        Date curDate = df.parse(rs.getString(1));
        if (curDate.before(minDate)) {
          minDate = curDate;
          String[] newInstruction = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) };
          instruction = newInstruction;
        }
      }
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception caught in reading from SQL. Retrying...");
    }
    return instruction;
  }
  
  public void removeInstruction(String account, String instructionID) throws Exception
  {
    String SQL = "DELETE FROM `instructions_" + account + "` WHERE " + "`Instruction ID`=" + instructionID;
    Statement stmt = this.conn.createStatement();
    stmt.executeUpdate(SQL);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/sql/SQLdata.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */