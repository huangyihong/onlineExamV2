package com.plg.shiro.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

/**
 * File工具类
 * @author CY
 *
 */
public class FileUtils {
    
    public static final int BYTESIZE = 1024;                                        //每次读取的大小 1KB
    public static String TEMP = null;                                                //保存文件的临时目录
    
    private static Logger logger = Logger.getLogger(FileUtils.class);
    
    static
    {
        //获取保存文件的临时目录  WEB-INF/temp/
        try {
            //TEMP = URLDecoder.decode(FileUtils.class.getClassLoader().getResource("../temp").getPath(), "utf-8");
        } catch (Exception e) {
            logger.error("===============>temp directory init error.. please check===============>");
            e.printStackTrace();
        }
    }
    

    /**
     * 将文件流保存在项目WEB-INF/temp目录下，并且返回这个文件；
     * @param is              待转化的文件流
     * @param fileName        临时文件名
     * @return
     * @throws IOException
     */
    public static File saveTempFile(InputStream is, String fileName){
        File temp = null;
        
        if(TEMP!=null && is!=null){
            temp = new File(TEMP + fileName);
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try{
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(new FileOutputStream(temp));                            //把文件流转为文件，保存在临时目录
                int len = 0;
                byte[] buf = new byte[10*BYTESIZE];                                                    //缓冲区
                while((len=bis.read(buf)) != -1){
                    bos.write(buf, 0, len);
                }
                bos.flush();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try {
                    if(bos!=null) bos.close();
                    if(bis!=null) bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }
    
    public static File saveTempFile(InputStream is, String fileName,String tempPath){
        File temp = null;
        File files=new File(tempPath);
        
        if(!files.exists()){
        	files.mkdirs();
        }
        if(tempPath!=null && is!=null){
            temp = new File(tempPath +"/"+ fileName);
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try{
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(new FileOutputStream(temp));                            //把文件流转为文件，保存在临时目录
                int len = 0;
                byte[] buf = new byte[10*BYTESIZE];                                                    //缓冲区
                while((len=bis.read(buf)) != -1){
                    bos.write(buf, 0, len);
                }
                bos.flush();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try {
                    if(bos!=null) bos.close();
                    if(bis!=null) bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }
    
    /**
     * 删除文件  用来删除临时文件
     * @param file
     */
    public static void deleteTempFile(File file){
        logger.warn("===============>begin delete temp file: =====================>" + file.getAbsolutePath());
        boolean result = file.delete();
        logger.warn("===============>delete result :===============>" + result);
    }
    
    
    
}