package com.wnt.controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/file")
public class FileController {
	
	
	Logger logger = Logger.getLogger(FileController.class);
	
	
	public String getUploadPath(){
		
		 String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		 
         try {
			path=URLDecoder.decode(path,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         path=path.replace('/', '\\'); // 将/换成\ 
         
         path=path.replace("file:", ""); //去掉file:  
         
         path=path.replace("classes\\", ""); //去掉class\
         
         path=path.replace("WEB-INF\\", ""); //去掉WEB-INF\  
         path=path.substring(1); //去掉第一个\,如 \D:\JavaWeb.
         path = path + "uploadFiles";
         logger.info(path);
		return path;
	}
	
	
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws IOException{
		if (!file.isEmpty()) {
            InputStream in = null;
            OutputStream out = null;

            try {
                File dir = new File(this.getUploadPath());
                
                if (!dir.exists())
                    dir.mkdirs();
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                in = file.getInputStream();
                out = new FileOutputStream(serverFile);
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) > 0) {
                    out.write(b, 0, len);
                }
                out.close();
                in.close();
                logger.info("Server File Location=" + serverFile.getAbsolutePath());

                Map<String, String> map = new HashMap<String, String>();
        		
        		map.put("success", "true");
        		
        		return map;
        		
            } catch (Exception e) {
            	Map<String, String> map = new HashMap<String, String>();
        		
        		map.put("failed", "true");
        		
        		return map;
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }

                if (in != null) {
                    in.close();
                    in = null;
                }
            }
        } else {
        	Map<String, String> map = new HashMap<String, String>();
    		
    		map.put("failed", "true");
    		
    		return map;
        }
	}

}
