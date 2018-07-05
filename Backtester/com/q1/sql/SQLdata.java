/*     */ package com.q1.sql;
/*     */ 
/*     */ import com.q1.math.DateTime;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLdata
/*     */ {
/*     */   Connection conn;
/*     */   Statement stmt;
/*     */   
/*     */   public SQLdata(String ipAddress, String database, String username, String password)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  25 */     String dbClassName = "com.mysql.jdbc.Driver";
/*     */     String CONNECTION;
/*  27 */     String CONNECTION; if (database == null) {
/*  28 */       CONNECTION = "jdbc:mysql://" + ipAddress + "/?autoReconnect=true";
/*     */     } else {
/*  30 */       CONNECTION = "jdbc:mysql://" + ipAddress + "/" + database + "?autoReconnect=true";
/*     */     }
/*     */     
/*  33 */     Class.forName(dbClassName);
/*     */     
/*     */ 
/*  36 */     Properties p = new Properties();
/*  37 */     p.put("user", username);
/*  38 */     p.put("password", password);
/*     */     
/*     */ 
/*  41 */     this.conn = DriverManager.getConnection(CONNECTION, p);
/*  42 */     this.stmt = this.conn.createStatement();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*     */     try {
/*  48 */       this.stmt.close();
/*  49 */       this.conn.close();
/*     */     }
/*     */     catch (SQLException e) {
/*  52 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void backupDatabase(String inDatabase, String outDatabase, String inTable, String timeTable, String key)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  60 */     Integer newKey = Integer.valueOf(Integer.parseInt(key) % 5 + 1);
/*  61 */     key = newKey.toString();
/*     */     
/*  63 */     String SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + 
/*  64 */       "`.`backup_" + key + "`='" + DateTime.getCurrentDate() + " " + DateTime.getCurrentTime() + 
/*  65 */       " Incomplete" + "'";
/*  66 */     this.stmt.executeUpdate(SQL);
/*     */     
/*     */ 
/*  69 */     SQL = "DELETE FROM `" + outDatabase + "`.`backup_" + key + "`" + " WHERE 1";
/*  70 */     this.stmt.executeUpdate(SQL);
/*  71 */     SQL = "INSERT INTO `" + outDatabase + "`.`backup_" + key + "` SELECT * FROM `" + inDatabase + "`.`" + inTable + 
/*  72 */       "`";
/*  73 */     this.stmt.executeUpdate(SQL);
/*  74 */     SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + "`.`Key`='" + 
/*  75 */       key + "'";
/*  76 */     this.stmt.executeUpdate(SQL);
/*  77 */     SQL = "UPDATE `" + outDatabase + "`.`" + timeTable + "` SET `" + outDatabase + "`.`" + timeTable + "`.`backup_" + 
/*  78 */       key + "`='" + DateTime.getCurrentDate() + " " + DateTime.getCurrentTime() + " Complete" + "'";
/*  79 */     this.stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cleanDatabase(SQLdata sqlObject, String inDatabase, String outDatabase, String outTable)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  88 */     String outDbTable = "`" + outDatabase + "`.`" + outTable + "`";
/*  89 */     String inDbTable = "`" + inDatabase + "`.`" + "clean_backup`";
/*     */     
/*  91 */     ArrayList<String> fieldList = sqlObject.getTableFields("clean_backup");
/*     */     
/*     */ 
/*  94 */     String SQL = "UPDATE " + outDbTable + ", " + inDbTable + " SET ";
/*  95 */     for (String field : fieldList) {
/*  96 */       SQL = SQL + outDbTable + ".`" + field + "` = " + inDbTable + ".`" + field + "`,";
/*     */     }
/*  98 */     SQL = SQL.substring(0, SQL.length() - 1) + " WHERE ";
/*  99 */     SQL = SQL + inDbTable + ".`Scrip ID` = " + outDbTable + ".`Scrip ID` AND " + inDbTable + ".`Segment ID` = " + 
/* 100 */       outDbTable + ".`Segment ID` AND " + inDbTable + ".`Account ID` = " + outDbTable + ".`Account ID`;";
/*     */     
/* 102 */     this.stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */   public void sendTableEntry(String table, Object[] tableEntry)
/*     */     throws Exception
/*     */   {
/* 108 */     String SQL = "INSERT INTO `" + table + 
/* 109 */       "`(`Timestamp`, `Scrip`, `Segment`, `Side`, `Type`, `Trigger`, `Action`) VALUES ('" + 
/* 110 */       tableEntry[0].toString() + "','" + tableEntry[1].toString() + "','" + tableEntry[2].toString() + "','" + 
/* 111 */       tableEntry[3].toString() + "','" + tableEntry[4].toString() + "'," + tableEntry[5].toString() + ",'" + 
/* 112 */       tableEntry[6].toString() + "')";
/* 113 */     Statement stmt = this.conn.createStatement();
/* 114 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */   public void deleteTableEntries(String table, Object[] tableEntry)
/*     */     throws Exception
/*     */   {
/* 120 */     String SQL = "DELETE FROM `" + table + "` WHERE `Scrip`='" + tableEntry[1].toString() + "' AND `Segment`='" + 
/* 121 */       tableEntry[2].toString() + "' AND `Side`='" + tableEntry[3].toString() + "' AND `Type`='" + 
/* 122 */       tableEntry[4].toString() + "'";
/* 123 */     Statement stmt = this.conn.createStatement();
/* 124 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */   public void clearTable(String table)
/*     */     throws Exception
/*     */   {
/* 130 */     String SQL = "DELETE FROM `" + table + "` WHERE 1";
/* 131 */     Statement stmt = this.conn.createStatement();
/* 132 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */   public ArrayList<Object[]> getMessageTableEntries(String table) throws Exception
/*     */   {
/* 137 */     ArrayList<Object[]> output = new ArrayList();
/*     */     
/*     */ 
/* 140 */     String SQL = "SELECT * FROM `" + table + "` WHERE 1";
/* 141 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 142 */     while (rs.next()) {
/* 143 */       Object[] tempOut = { rs.getString(1), rs.getString(2) };
/* 144 */       output.add(tempOut);
/*     */     }
/* 146 */     rs.close();
/*     */     
/* 148 */     return output;
/*     */   }
/*     */   
/*     */   public ArrayList<Object[]> getTableEntries(String table) throws Exception
/*     */   {
/* 153 */     ArrayList<Object[]> output = new ArrayList();
/*     */     
/*     */ 
/* 156 */     String SQL = "SELECT * FROM `" + table + "` WHERE 1";
/* 157 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 158 */     while (rs.next()) {
/* 159 */       Object[] tempOut = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
/* 160 */         Double.valueOf(rs.getDouble(6)), rs.getString(7) };
/* 161 */       output.add(tempOut);
/*     */     }
/* 163 */     rs.close();
/*     */     
/* 165 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sendConsoleMessage(String table, String timestamp, String message)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 173 */     String SQL = "INSERT INTO `" + table + "`(`Timestamp`, `Message`) VALUES ('" + timestamp + "','" + message + 
/* 174 */       "')";
/* 175 */     Statement stmt = this.conn.createStatement();
/* 176 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayList<String> getTableFields(String table)
/*     */   {
/* 183 */     ArrayList<String> output = new ArrayList();
/*     */     
/* 185 */     int catchFlag = 1;
/* 186 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 189 */         String SQL = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + table + "';";
/*     */         
/* 191 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 192 */         while (rs.next()) {
/* 193 */           int i = 1;
/* 194 */           if (rs.getString(i) != null)
/* 195 */             output.add(rs.getString(i++));
/*     */         }
/* 197 */         rs.close();
/* 198 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 200 */         catchFlag = 1;
/* 201 */         e.printStackTrace();
/* 202 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 206 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPromptTableCount(String table, String scrip, String segment, String side, String type)
/*     */   {
/* 213 */     int output = 1;
/*     */     try
/*     */     {
/* 216 */       String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
/* 217 */         "' and `Side`='" + side + "' and `Type`='" + type + "'";
/* 218 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 219 */       while (rs.next()) {
/* 220 */         output = rs.getInt(1);
/*     */       }
/* 222 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 226 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getInstructionTableCountForInstructionID(String table, Integer instructionID)
/*     */   {
/* 232 */     int output = 1;
/*     */     try
/*     */     {
/* 235 */       String SQL = "select count(*) from `" + table + "` where `Instruction ID`=" + instructionID;
/* 236 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 237 */       while (rs.next()) {
/* 238 */         output = rs.getInt(1);
/*     */       }
/* 240 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 244 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getInstructionTableCountForScrip(String table, String scrip, String segment)
/*     */   {
/* 250 */     int output = 0;
/*     */     try
/*     */     {
/* 253 */       String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
/* 254 */         "'";
/* 255 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 256 */       while (rs.next()) {
/* 257 */         output = rs.getInt(1);
/*     */       }
/* 259 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 263 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getInstructionTableCountForValue(String table, String value)
/*     */   {
/* 269 */     int output = 0;
/*     */     try
/*     */     {
/* 272 */       String SQL = "select count(*) from `" + table + "` where `Scrip`='" + value + "'";
/* 273 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 274 */       while (rs.next()) {
/* 275 */         output = rs.getInt(1);
/*     */       }
/* 277 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 281 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getInstructionTableCount(String table)
/*     */   {
/* 287 */     int output = 1;
/*     */     try
/*     */     {
/* 290 */       String SQL = "select count(*) from `" + table + "` where 1";
/* 291 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 292 */       while (rs.next()) {
/* 293 */         output = rs.getInt(1);
/*     */       }
/* 295 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 299 */     return output;
/*     */   }
/*     */   
/*     */   public int getInstructionTableMax(String table, String account)
/*     */     throws Exception
/*     */   {
/* 305 */     int output = 1;
/*     */     
/* 307 */     String SQL = "select `Instruction Max` from `" + table + "` where `Account ID`='" + account + "'";
/* 308 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 309 */     while (rs.next()) {
/* 310 */       output = rs.getInt(1);
/*     */     }
/* 312 */     rs.close();
/*     */     
/* 314 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPromptTableCountWithPrice(String table, String scrip, String segment, String side, String type, Double price)
/*     */   {
/* 321 */     int output = 1;
/*     */     try
/*     */     {
/* 324 */       String SQL = "select count(*) from `" + table + "` where `Scrip`='" + scrip + "' and `Segment`='" + segment + 
/* 325 */         "' and `Side`='" + side + "' and `Type`='" + type + "' and `Trigger`=" + price;
/* 326 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 327 */       while (rs.next()) {
/* 328 */         output = rs.getInt(1);
/*     */       }
/* 330 */       rs.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 334 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTableCount(String table)
/*     */   {
/* 340 */     int output = 0;
/*     */     
/* 342 */     int catchFlag = 1;
/* 343 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 346 */         String SQL = "select count(*) from `" + table + "`";
/*     */         
/* 348 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 349 */         while (rs.next()) {
/* 350 */           output = rs.getInt(1);
/*     */         }
/* 352 */         rs.close();
/* 353 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 355 */         catchFlag = 1;
/* 356 */         e.printStackTrace();
/* 357 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 361 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setStringVal(String table, String keyName, String keyValue, String value, String entry)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 369 */     String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
/* 370 */       "'";
/* 371 */     Statement stmt = this.conn.createStatement();
/* 372 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoubleVal(String table, String keyName, String keyValue, String value, double entry)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 381 */     String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
/* 382 */       "'";
/* 383 */     Statement stmt = this.conn.createStatement();
/* 384 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setIntVal(String table, String keyName, String keyValue, String value, int entry)
/*     */     throws Exception
/*     */   {
/* 392 */     String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE `" + keyName + "`='" + keyValue + 
/* 393 */       "'";
/* 394 */     Statement stmt = this.conn.createStatement();
/* 395 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setIntValWithoutKey(String table, String value, int entry)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 403 */     String SQL = "UPDATE `" + table + "` SET `" + value + "`='" + entry + "' WHERE 1";
/* 404 */     Statement stmt = this.conn.createStatement();
/* 405 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoubleValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, double entry)
/*     */     throws Exception
/*     */   {
/* 414 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 415 */       "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/* 416 */     Statement stmt = this.conn.createStatement();
/* 417 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetCheckTable(String checkTable)
/*     */     throws Exception
/*     */   {
/* 425 */     String SQL = "UPDATE `" + checkTable + 
/* 426 */       "` SET `Buy Entry Trade`=0,`Sell Entry Trade`=0,`Confirm Buy Entry Trade`=0,`Confirm Sell Entry Trade`=0,`Confirm Buy Exit Order`=0,`Confirm Sell Exit Order`=0,`Buy Exit Trade`=0,`Sell Exit Trade`=0,`Confirm Buy Exit Trade`=0,`Confirm Sell Exit Trade`=0,`Modify Buy Exit Order`=0,`Modify Sell Exit Order`=0,`Modify Buy Entry Order`=0,`Modify Sell Entry Order`=0,`Confirm Buy Entry Order`=0,`Confirm Sell Entry Order`=0,`Place Buy Entry Order`=0,`Place Sell Entry Order`=0,`Place Buy Exit Order`=0,`Place Sell Exit Order`=0,`Check Buy Entry Order`=0,`Check Sell Entry Order`=0 WHERE 1";
/* 427 */     this.stmt = this.conn.createStatement();
/* 428 */     this.stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetAccountTable(String accountTable)
/*     */     throws Exception
/*     */   {
/* 436 */     String SQL = "UPDATE `" + accountTable + 
/* 437 */       "` SET `State`=0,`NOW Status`=0,`Purge Status`=0,`Client Value`=0,`Client Status`=0,`Client Last Update`='00:00:00',`Tradebook Last Update`='00:00:00',`Instruction Max`=0,`Equity State`=0,`Comex State`=0 WHERE 1";
/* 438 */     Statement stmt = this.conn.createStatement();
/* 439 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetParamTable(String paramsTable)
/*     */     throws Exception
/*     */   {
/* 447 */     String SQL = "UPDATE `" + paramsTable + 
/* 448 */       "` SET `State`=0,`Buy Entry OID`='',`Sell Entry OID`='',`Buy Exit OID`='',`Sell Exit OID`='',`Buy Entry Price`=0,`Sell Entry Price`=0,`Buy Exit Price`=0,`Sell Exit Price`=0,`Buy Entry OStatus`='',`Sell Entry OStatus`='',`Buy Exit OStatus`='',`Sell Exit OStatus`='',`Buy Entry Qty`=0,`Sell Entry Qty`=0,`Buy Exit Qty`=0,`Sell Exit Qty`=0,`Last Purge`='01-Jan-1970 00:00:00',`M2M`=0,`Position`=0 WHERE 1";
/* 449 */     Statement stmt = this.conn.createStatement();
/* 450 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetScripTable(String scripTable)
/*     */     throws Exception
/*     */   {
/* 458 */     String SQL = "UPDATE `" + scripTable + 
/* 459 */       "` SET `Buy Entry Trigger`=0.0,`Sell Entry Trigger`=0.0,`Buy Exit Trigger`=0.0,`Sell Exit Trigger`=0.0,`State`=0,`High`=0,`Low`=1000000,`Lower Limit`=0,`Upper Limit`=1000000 WHERE 1";
/* 460 */     Statement stmt = this.conn.createStatement();
/* 461 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetParamTableForAccScrip(String paramsTable, String account, String scrip, String segment)
/*     */     throws Exception
/*     */   {
/* 469 */     String SQL = "UPDATE `" + paramsTable + 
/* 470 */       "` SET `Buy Entry OID`='',`Sell Entry OID`='',`Buy Exit OID`='',`Sell Exit OID`='',`Buy Entry Price`=0,`Sell Entry Price`=0,`Buy Exit Price`=0,`Sell Exit Price`=0,`Buy Entry OStatus`='',`Sell Entry OStatus`='',`Buy Exit OStatus`='',`Sell Exit OStatus`='',`Buy Entry Qty`=0,`Sell Entry Qty`=0,`Buy Exit Qty`=0,`Sell Exit Qty`=0,`Position`=0,`M2M`=0.0 WHERE `Account ID`='" + 
/* 471 */       account + "' AND `Scrip ID`='" + scrip + "' AND `Segment ID`='" + segment + "'";
/* 472 */     Statement stmt = this.conn.createStatement();
/* 473 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setIntValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, int entry)
/*     */     throws Exception
/*     */   {
/* 481 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 482 */       "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/* 483 */     Statement stmt = this.conn.createStatement();
/* 484 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStringValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value, String entry)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 493 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = '" + entry + "' WHERE `" + keyName1 + "`='" + 
/* 494 */       keyValue1 + "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/* 495 */     Statement stmt = this.conn.createStatement();
/* 496 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStringValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, String entry)
/*     */     throws Exception
/*     */   {
/* 505 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = '" + entry + "' WHERE `" + keyName1 + "`='" + 
/* 506 */       keyValue1 + "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/* 507 */     Statement stmt = this.conn.createStatement();
/* 508 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoubleValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, double entry)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 518 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 519 */       "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/* 520 */     Statement stmt = this.conn.createStatement();
/* 521 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIntValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value, int entry)
/*     */     throws Exception
/*     */   {
/* 530 */     String SQL = "UPDATE `" + table + "` SET `" + value + "` = " + entry + " WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 531 */       "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/* 532 */     Statement stmt = this.conn.createStatement();
/* 533 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getStringVal(String table, String keyName, String keyValue, String value)
/*     */   {
/* 540 */     String output = "";
/*     */     
/* 542 */     int catchFlag = 1;
/* 543 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 546 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
/* 547 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 548 */         while (rs.next()) {
/* 549 */           output = rs.getString(1);
/*     */         }
/* 551 */         rs.close();
/* 552 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 554 */         catchFlag = 1;
/* 555 */         e.printStackTrace();
/* 556 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 560 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStringValwithoutKey(String table, String value)
/*     */   {
/* 566 */     String output = "";
/*     */     
/* 568 */     int catchFlag = 1;
/* 569 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 572 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE 1";
/* 573 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 574 */         while (rs.next()) {
/* 575 */           output = rs.getString(1);
/*     */         }
/* 577 */         rs.close();
/* 578 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 580 */         catchFlag = 1;
/* 581 */         e.printStackTrace();
/* 582 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 586 */     return output;
/*     */   }
/*     */   
/*     */   public int getIntValwithoutKey(String table, String value)
/*     */     throws Exception
/*     */   {
/* 592 */     int output = 0;
/*     */     
/*     */ 
/* 595 */     String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE 1";
/* 596 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 597 */     while (rs.next()) {
/* 598 */       output = rs.getInt(1);
/*     */     }
/* 600 */     rs.close();
/*     */     
/* 602 */     return output;
/*     */   }
/*     */   
/*     */   public double getDoubleVal(String table, String keyName, String keyValue, String value)
/*     */   {
/* 607 */     double output = 0.0D;
/*     */     
/* 609 */     int catchFlag = 1;
/* 610 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 613 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
/* 614 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 615 */         while (rs.next()) {
/* 616 */           output = rs.getDouble(1);
/*     */         }
/* 618 */         rs.close();
/* 619 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 621 */         catchFlag = 1;
/* 622 */         e.printStackTrace();
/* 623 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 627 */     return output;
/*     */   }
/*     */   
/*     */   public int getIntVal(String table, String keyName, String keyValue, String value)
/*     */     throws Exception
/*     */   {
/* 633 */     int output = 0;
/*     */     
/* 635 */     String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName + "`='" + keyValue + "'";
/*     */     
/* 637 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 638 */     while (rs.next()) {
/* 639 */       output = rs.getInt(1);
/*     */     }
/* 641 */     rs.close();
/* 642 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getDoubleValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
/*     */   {
/* 648 */     double output = 0.0D;
/* 649 */     int catchFlag = 1;
/* 650 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 653 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 654 */           "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/*     */         
/* 656 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 657 */         while (rs.next()) {
/* 658 */           output = rs.getDouble(1);
/*     */         }
/* 660 */         rs.close();
/* 661 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 663 */         catchFlag = 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 671 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getIntValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
/*     */   {
/* 677 */     int output = 0;
/* 678 */     int catchFlag = 1;
/* 679 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 682 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 683 */           "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/*     */         
/* 685 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 686 */         while (rs.next()) {
/* 687 */           output = rs.getInt(1);
/*     */         }
/* 689 */         rs.close();
/* 690 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 692 */         catchFlag = 1;
/* 693 */         e.printStackTrace();
/* 694 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/* 697 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getIntValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
/*     */   {
/* 703 */     int output = 0;
/* 704 */     int catchFlag = 1;
/* 705 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 708 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 709 */           "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/*     */         
/* 711 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 712 */         while (rs.next()) {
/* 713 */           output = rs.getInt(1);
/*     */         }
/* 715 */         rs.close();
/* 716 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 718 */         catchFlag = 1;
/* 719 */         e.printStackTrace();
/* 720 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/* 723 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getDoubleValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
/*     */     throws SQLException
/*     */   {
/* 730 */     double output = 0.0D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 735 */     String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + "' AND `" + 
/* 736 */       keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/*     */     
/* 738 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 739 */     while (rs.next()) {
/* 740 */       output = rs.getDouble(1);
/*     */     }
/* 742 */     rs.close();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 752 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getStringValwithThreeKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String keyName3, String keyValue3, String value)
/*     */   {
/* 759 */     String output = "";
/*     */     
/* 761 */     int catchFlag = 1;
/* 762 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 765 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 766 */           "' AND `" + keyName2 + "`='" + keyValue2 + "' AND `" + keyName3 + "`='" + keyValue3 + "'";
/*     */         
/* 768 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 769 */         while (rs.next()) {
/* 770 */           output = rs.getString(1);
/*     */         }
/* 772 */         rs.close();
/* 773 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 775 */         catchFlag = 1;
/* 776 */         e.printStackTrace();
/* 777 */         System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */     
/* 781 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getStringValwithTwoKeys(String table, String keyName1, String keyValue1, String keyName2, String keyValue2, String value)
/*     */   {
/* 788 */     String output = "";
/*     */     
/* 790 */     int catchFlag = 1;
/* 791 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 794 */         String SQL = "SELECT `" + value + "` FROM `" + table + "` WHERE `" + keyName1 + "`='" + keyValue1 + 
/* 795 */           "' AND `" + keyName2 + "`='" + keyValue2 + "'";
/*     */         
/* 797 */         ResultSet rs = this.stmt.executeQuery(SQL);
/* 798 */         while (rs.next()) {
/* 799 */           output = rs.getString(1);
/*     */         }
/* 801 */         rs.close();
/* 802 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 804 */         catchFlag = 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 812 */     return output;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getStringArrayList(String table, String Key)
/*     */     throws Exception
/*     */   {
/* 818 */     ArrayList<String> arrayLst = new ArrayList();
/*     */     
/*     */ 
/* 821 */     String SQL = "SELECT `" + Key + "` FROM `" + table + "` WHERE 1";
/*     */     
/* 823 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 824 */     while (rs.next()) {
/* 825 */       arrayLst.add(rs.getString(1));
/*     */     }
/*     */     
/* 828 */     return arrayLst;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<String[]> getStringArrayArrayList(String table, String[] Key)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/* 835 */     ArrayList<String[]> arrayLst = new ArrayList();
/*     */     
/*     */ 
/* 838 */     String SQL = "SELECT `" + Key[0] + "`,`" + Key[1] + "` FROM `" + table + "` WHERE 1";
/* 839 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 840 */     while (rs.next()) {
/* 841 */       String[] array = new String[2];
/* 842 */       array[0] = rs.getString(1);
/* 843 */       array[1] = rs.getString(2);
/* 844 */       arrayLst.add(array);
/*     */     }
/* 846 */     return arrayLst;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<String[]> getStringArrayArrayListWithThreeKeys(String table, String[] outKeys, String[] inKeys, String[] inKeyVals)
/*     */     throws Exception
/*     */   {
/* 853 */     ArrayList<String[]> arrayLst = new ArrayList();
/*     */     
/*     */ 
/* 856 */     String SQL = "SELECT `" + outKeys[0] + "`,`" + outKeys[1] + "` FROM `" + table + "` WHERE `" + inKeys[0] + "`='" + 
/* 857 */       inKeyVals[0] + "' AND `" + inKeys[1] + "`='" + inKeyVals[1] + "' AND `" + inKeys[2] + "`='" + 
/* 858 */       inKeyVals[2] + "'";
/* 859 */     ResultSet rs = this.stmt.executeQuery(SQL);
/* 860 */     while (rs.next()) {
/* 861 */       String[] array = new String[2];
/* 862 */       array[0] = rs.getString(1);
/* 863 */       array[1] = rs.getString(2);
/* 864 */       arrayLst.add(array);
/*     */     }
/* 866 */     return arrayLst;
/*     */   }
/*     */   
/*     */   public void deleteRowFromTable(String table, String key, String keyVal) {
/* 870 */     int catchFlag = 1;
/* 871 */     while (catchFlag == 1) {
/*     */       try
/*     */       {
/* 874 */         String SQL = "DELETE FROM `" + table + "` WHERE `" + key + "`='" + keyVal + "'";
/* 875 */         this.stmt.executeUpdate(SQL);
/* 876 */         catchFlag = 0;
/*     */       } catch (Exception e) {
/* 878 */         catchFlag = 1;
/* 879 */         e.printStackTrace();
/* 880 */         System.out.println("Exception caught in deleting from SQL. Retrying...");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void sendOrderToDatabase(String table, String[] order, String account, String scrip, String segment)
/*     */     throws Exception
/*     */   {
/* 889 */     String SQL = "INSERT INTO `" + table + 
/* 890 */       "` (`Account ID`, `Scrip ID`, `Segment ID`, `Timestamp`, `Order ID`) VALUES ('" + account + "' ,'" + 
/* 891 */       scrip + "','" + segment + "','" + order[0] + "','" + order[1] + "')";
/* 892 */     Statement stmt = this.conn.createStatement();
/* 893 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */   
/*     */   public int sendInstructionToDatabase(String account, String accountTable, String timeStamp, int type, String params, String scrip, String segment)
/*     */     throws Exception
/*     */   {
/* 899 */     String table = "instructions_" + account.toLowerCase();
/* 900 */     int tableCount = getInstructionTableMax(accountTable, account) + 1;
/* 901 */     setIntVal(accountTable, "Account ID", account, "Instruction Max", tableCount);
/*     */     
/* 903 */     String SQL = "INSERT INTO `" + table + 
/* 904 */       "` (`Timestamp`, `Instruction ID`, `Type`, `Parameters`, `Scrip`, `Segment`) VALUES ('" + timeStamp + 
/* 905 */       "'," + tableCount + ",'" + type + "','" + params + "','" + scrip + "','" + segment + "')";
/* 906 */     Statement stmt = this.conn.createStatement();
/* 907 */     stmt.executeUpdate(SQL);
/* 908 */     return tableCount;
/*     */   }
/*     */   
/*     */   public String[] getOldestInstruction(String account)
/*     */     throws Exception
/*     */   {
/* 914 */     String[] instruction = null;
/* 915 */     SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
/* 916 */     Date minDate = df.parse("01-May-3101 09:10:00");
/*     */     
/*     */     try
/*     */     {
/* 920 */       String SQL = "SELECT `Timestamp`, `Instruction ID`, `Type`, `Parameters` FROM `instructions_" + account + 
/* 921 */         "` WHERE 1";
/* 922 */       ResultSet rs = this.stmt.executeQuery(SQL);
/* 923 */       while (rs.next())
/*     */       {
/* 925 */         Date curDate = df.parse(rs.getString(1));
/* 926 */         if (curDate.before(minDate)) {
/* 927 */           minDate = curDate;
/* 928 */           String[] newInstruction = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) };
/* 929 */           instruction = newInstruction;
/*     */         }
/*     */       }
/* 932 */       rs.close();
/*     */     } catch (Exception e) {
/* 934 */       e.printStackTrace();
/* 935 */       System.out.println("Exception caught in reading from SQL. Retrying...");
/*     */     }
/* 937 */     return instruction;
/*     */   }
/*     */   
/*     */   public void removeInstruction(String account, String instructionID) throws Exception
/*     */   {
/* 942 */     String SQL = "DELETE FROM `instructions_" + account + "` WHERE " + "`Instruction ID`=" + instructionID;
/* 943 */     Statement stmt = this.conn.createStatement();
/* 944 */     stmt.executeUpdate(SQL);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/sql/SQLdata.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */