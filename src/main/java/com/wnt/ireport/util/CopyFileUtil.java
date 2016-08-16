package com.wnt.ireport.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileUtil {

	  /**
	  * 复制单个文件
	  * @param srcFileName 待复制的文件名 
	  * @param descFileName 目标文件名
	  * @param overlay 如果目标文件存在，是否覆盖
	  * @return 如果复制成功返回true，否则返回false
	  */
	  public static boolean copyFile(String srcFileName, String destFileName) {
	     File srcFile = new File(srcFileName);

	     // 判断源文件是否存在
	     if (!srcFile.exists()) {
	      System.out.println("复制文件失败，源文件：" + srcFileName + "不存在！");
	      return false;
	     } else if (!srcFile.isFile()) {
	      System.out.println("复制文件失败，源文件：" + srcFileName + "不是一个文件！");
	      return false;
	     }

	     // 判断目标文件是否存在
	     File destFile = new File(destFileName);
	     if (destFile.exists()) {
	      // 如果目标文件存在并允许覆盖
	       // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
	       if (!destFile.delete()) {
	        System.out.println("复制文件失败：删除目标文件" + destFileName + "失败！");
	        return false;
	      }
	     }

	     // 复制文件
	     int byteread = 0; // 读取的字节数
	     InputStream in = null;
	     OutputStream out = null;

	     try {
	      in = new FileInputStream(srcFile);
	      out = new FileOutputStream(destFile);
	      byte[] buffer = new byte[1024];

	      while ((byteread = in.read(buffer)) != -1) {
	       out.write(buffer, 0, byteread);
	      }
	      return true;

	     } catch (FileNotFoundException e) {
	      System.out.println("复制文件失败：" + e.getMessage());
	      return false;
	     } catch (IOException e) {
	      System.out.println("复制文件失败：" + e.getMessage());
	      return false;
	     } finally {
	      try {
	       if (out != null)
	        out.close();
	       if (in != null)
	        in.close();
	      } catch (IOException e) {
	       e.printStackTrace();
	      }
	     }
	  }
}
