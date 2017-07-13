package com.example.scso.school_social;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by CYH on 2017/7/1.
 */

public class FtpThread extends Thread {
    private String file;
    private ArrayList<String> filename;
    private String path;
    private ArrayList<InputStream> input;
    private Handler handler;
    public FtpThread(String file, ArrayList<String> filename, String path, ArrayList<InputStream> input, Handler handler){
        this.file=file;
        this.filename=filename;
        this.path=path;
        this.input=input;
        this.handler=handler;
    }
    @Override
    public void run() {
        String url="118.89.199.187";
        String username="ubuntu";
        String password="cyh962014";
        FTPClient ftpClient=new FTPClient();
        boolean state=false;
        try {
            ftpClient.connect(url);
            ftpClient.login(username,password);
            int reply=ftpClient.getReplyCode();
            if(FTPReply.isPositiveCompletion(reply)) {
                ftpClient.changeWorkingDirectory(path);
                ftpClient.mkd(file);
                ftpClient.changeWorkingDirectory("./"+file);
                for (int i = 0; i < input.size(); i++) {
                    Log.d("tag", "run: 上传图片---->"+filename.get(i));
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.storeFile(filename.get(i), input.get(i));
                    input.get(i).close();
                }
                state = true;
            }
            ftpClient.logout();
            Message msg=Message.obtain(handler,3);
            msg.obj=state;
            handler.sendMessage(msg);
        }catch (IOException e){
            e.printStackTrace();
            Message msg=Message.obtain(handler,3);
            msg.obj=state;
            handler.sendMessage(msg);
        }
    }
}
