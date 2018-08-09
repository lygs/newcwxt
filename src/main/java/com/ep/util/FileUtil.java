package com.ep.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

/**
 * @author Zhaoxh
 * @version 创建时间：2018年8月9日 上午10:27:31 文件操作
 */
public class FileUtil {

	public static boolean saveFile(String savePath,  InputStream inputStream) {
		
		FileOutputStream os = null;
		File tempFile = new File(savePath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}

		try {
			os = new FileOutputStream(savePath+System.currentTimeMillis());
			byte buffer[] = new byte[1024];
			int len;

			while ((len = inputStream.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			if(os != null) {
				
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			if(inputStream != null ) {
				try {
					inputStream.close();
					
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				
			}
		}

		return true;

	}
	
}
