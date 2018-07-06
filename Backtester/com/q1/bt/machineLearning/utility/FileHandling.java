package com.q1.bt.machineLearning.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHandling
{
  public static void copyFolder(File src, File dest) throws java.io.IOException
  {
    if (src.isDirectory())
    {

      if (!dest.exists()) {
        dest.mkdir();
        System.out.println("Directory copied from " + src + "  to " + 
          dest);
      }
      

      String[] files = src.list();
      String[] arrayOfString1;
      int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
        
        File srcFile = new File(src, file);
        File destFile = new File(dest, file);
        
        copyFolder(srcFile, destFile);
      }
      
    }
    else
    {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dest);
      
      byte[] buffer = new byte['Ѐ'];
      
      int length;
      
      while ((length = in.read(buffer)) > 0) { int length;
        out.write(buffer, 0, length);
      }
      
      in.close();
      out.close();
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/FileHandling.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */