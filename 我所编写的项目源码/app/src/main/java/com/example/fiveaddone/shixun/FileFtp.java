package com.example.fiveaddone.shixun;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileFtp extends Thread {
    private String file;
    private String filename;
    private String path;
    private InputStream input;
    private Handler handler;
    private Handler handler1;
    public FileFtp(String file, String filename, String path, BufferedInputStream input, Handler handler,Handler handler1){
        this.file=file;
        this.filename=filename;
        this.path=path;
        this.input=input;
        this.handler=handler;
        this.handler1=handler1;

    }
    @Override
    public void run() {
        String url="118.89.199.187";
        String username="ubuntu";
        String password="cyh962014";
        FTPClient ftpClient=new FTPClient();
        boolean state=true;
        try {
            ftpClient.connect(url);
            ftpClient.login(username,password);
            int reply=ftpClient.getReplyCode();
            if(FTPReply.isPositiveCompletion(reply)) {
                ftpClient.setCopyStreamListener(new CopyStreamListener() {
                    @Override
                    public void bytesTransferred(CopyStreamEvent copyStreamEvent) {

                    }

                    @Override
                    public void bytesTransferred(long l, int i, long l1) {

                        Message msg=Message.obtain(handler,3);
                        msg.obj=l;
                        handler.sendMessage(msg);


                    }
                });
                ftpClient.changeWorkingDirectory(path);
                ftpClient.mkd(file);
                ftpClient.changeWorkingDirectory("./"+file);

                    Log.d("tag", "run: 上传文件---->"+filename);
                ftpClient.setControlEncoding("GBK");
                /*ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);*/
                /*ftpClient.enterLocalPassiveMode();*/
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    state=ftpClient.storeFile(new String(filename.getBytes("GBK"),"iso-8859-1"), input);
                Log.e("tag","上传结果:"+state);
                    input.close();



            }
            ftpClient.logout();
            Message msg=Message.obtain(handler1,3);
            msg.obj=state;
            handler1.sendMessage(msg);
        }catch (IOException e){
            e.printStackTrace();
            Message msg=Message.obtain(handler1,3);
            msg.obj=state;
            handler1.sendMessage(msg);
        }
    }


}

