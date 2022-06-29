package com.example.divicemonitor2.Threads;

import android.os.Build;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.example.divicemonitor2.DiviceinformActivity;
import com.example.divicemonitor2.MainPage;
import com.example.divicemonitor2.MonitorActivity;
import com.example.divicemonitor2.tools.DataDeal;
import com.example.divicemonitor2.tools.ServerLink;
import com.example.divicemonitor2.tools.UDPsocket;

import java.util.Arrays;

import static com.example.divicemonitor2.MonitorActivity.datatype;
import static com.example.divicemonitor2.MonitorActivity.moderoadnum;


//设备直连时，在udpsocket.Data中存放了64字节数据，连接服务器时可以通过服务器获取设备模块数量，存放512字节数据，存储大小可以修改
public class Thread_main extends Thread{

    public static byte[] receBuf = new byte[256];   //连接设备监控数据时存储接收字节

    public boolean isSleep = false;  //线程开始后是否暂停的判别
    public boolean isStop = false;

    private byte[] temp2 = new byte[32];
    private int error = 0;
    private int jicunqi_count = 1;//当前监控模块需要获取的寄存器数量
    private int tempx;
    private long tempy;
    private byte[] tempz=new byte[256];
    public static UDPsocket socket = new UDPsocket();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        Looper.prepare();
        while (!isStop){
            if (MainPage.connecttype == 0) {   //连接设备请求配置数据
                try {
                    if (isSleep) {
                        Thread.sleep(1000);
                    } else {
                        System.out.println("开始发送设备请求");
                        ServerLink.sendinformation();
                        Thread.sleep(500);
                        System.out.println("开始接受");
                        ServerLink.getinforamation();           //接受的信息在serverlink的Data里
                        DiviceinformActivity.divicettcount = ServerLink.Data[87];
                        System.out.println("模块总数" + DiviceinformActivity.divicettcount);
                        System.arraycopy(ServerLink.Data, 89 , MonitorActivity.modeinform, 0, 12*DiviceinformActivity.divicettcount);    //复制设备信息共12*modetotalcount个  ,Data[88]号为保留null
                        System.out.println("设备总信息" + MonitorActivity.modeinform);
                        System.arraycopy(ServerLink.Data,89+DiviceinformActivity.divicettcount*12,tempz,0,256);      //复制注释信息
                        MonitorActivity.zhushi=tempz.toString();
                        System.out.println("设备注释" + MonitorActivity.zhushi);
                        MonitorActivity.modeinfdispose(MonitorActivity.modeinform,DiviceinformActivity.divicettcount);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("InterruptedException");
                    error++;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (error >= 10) {
                    error = 0;
                }
            }
            else if (MainPage.connecttype == 1) {        //连接服务器请求配置信息
                try {
                    if (isSleep) {
                        Thread.sleep(1000);
                    } else {
                        System.out.println("开始发送服务器请求");
                        ServerLink.sendinformation();
                        Thread.sleep(300);
                        System.out.println("开始接受");
                        ServerLink.getinforamation();           //接受的信息在serverlink的Data里
                        DiviceinformActivity.diviceonline = ServerLink.Data[0];
                        System.out.println("在线" + DiviceinformActivity.diviceonline);

                        System.arraycopy(ServerLink.Data, 1, temp2, 0, 32);
                        DiviceinformActivity.divicename = DataDeal.byteToString(temp2);
                        System.out.println("设备名称" + DiviceinformActivity.divicename);
                        for (int i = 0; i < 32; i++) {
                            temp2[i] = 0;
                        }
                        System.arraycopy(ServerLink.Data, 33, temp2, 0, 32);
                        DiviceinformActivity.usecompany = DataDeal.byteToString(temp2);
                        System.out.println("使用单位" + DiviceinformActivity.usecompany);
                        for (int i = 0; i < 32; i++) {
                            temp2[i] = 0;
                        }
                        System.arraycopy(ServerLink.Data, 65, temp2, 0, 4);
                        DiviceinformActivity.registime = DataDeal.bytestotime(temp2);
                        System.out.println("注册时间" + DiviceinformActivity.registime);
                        for (int i = 0; i < 32; i++) {
                            temp2[i] = 0;
                        }
                        System.arraycopy(ServerLink.Data, 69, temp2, 0, 16);
                        DiviceinformActivity.diviceIP = DataDeal.bytesToIp(temp2);
                        System.out.println("设备IPV4" + DiviceinformActivity.diviceIP);
                        for (int i = 0; i < 32; i++) {
                            temp2[i] = 0;
                        }
                        System.arraycopy(ServerLink.Data, 85, temp2, 0, 2);
                        DiviceinformActivity.diviceport = DataDeal.bytetoport(temp2);
                        System.out.println("设备端口" + DiviceinformActivity.diviceport);
                        for (int i = 0; i < 32; i++) {
                            temp2[i] = 0;
                        }
                        DiviceinformActivity.divicettcount = ServerLink.Data[87];
                        System.out.println("模块总数" + DiviceinformActivity.divicettcount);
                        System.arraycopy(ServerLink.Data, 89 , MonitorActivity.modeinform, 0, 12*DiviceinformActivity.divicettcount);    //复制设备信息
                        System.out.println("设备总信息" + MonitorActivity.modeinform);
                        System.arraycopy(ServerLink.Data,89+DiviceinformActivity.divicettcount*12,tempz,0,256);      //复制注释信息
                        MonitorActivity.zhushi=tempz.toString();
                        System.out.println("设备注释" + MonitorActivity.zhushi);
                        MonitorActivity.modeinfdispose(MonitorActivity.modeinform,DiviceinformActivity.divicettcount);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("InterruptedException");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            else if (MainPage.connecttype == 2) {  //设备连接监控IO流,通道数乘以数据类型占据的大小等于需要的寄存器数量构造发送帧
                try {
                    if (isSleep) {         //
                        Thread.sleep(1000);
                    } else {
                        try {
                            Thread.sleep(1000);
                            System.out.println("设备直连模式");
                            System.out.println("准备发送数据");
                            //获取监控的模块长度
                            if(MonitorActivity.modeltype=="CPU"){
                                jicunqi_count=8;        //假设CPU模块存8个寄存器数据即16字节
                                System.out.println("CPU模块");
                            }
                            else {
                                if (datatype.equals("bit")) jicunqi_count = 1;
                                else if (datatype.equals("byte"))
                                    jicunqi_count = moderoadnum / 2;
                                else if (datatype.equals("short")) jicunqi_count = moderoadnum;
                                else if (datatype.equals("int"))
                                    jicunqi_count = moderoadnum * 2;
                                else if (datatype.equals("float"))
                                    jicunqi_count = moderoadnum * 2;
                                else if (datatype.equals("double"))
                                    jicunqi_count = moderoadnum * 4;
                                System.out.println("其他模块");
                            }
                            socket.sendinformation(jicunqi_count, MonitorActivity.data_address);
                            Thread.sleep(200);
                            System.out.println("接收数据");
                            socket.getinformation(jicunqi_count);
                            System.out.println("接收数据为" + socket.Data);
                            System.out.println("完成一次读取");
                            Thread.sleep(500);
                            receBuf = UDPsocket.Data;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("InterruptedException");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception");
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //数据处理
                        if(MonitorActivity.isdataunsign==false) {  //有符号数据
                            System.out.println("有符号数据");
                            if (MonitorActivity.modeltype == "CPU") {
                                MonitorActivity.cpudata=receBuf;
                            }
                            else {
                                if (datatype.equals("bit")) {
                                    MonitorActivity.lightdata = DataDeal.bytetobinary(receBuf);    //化为2进制码，每一灯代表1位,bit数据类型下只能获取2字节数据,即16通道。
                                    System.out.println("接收的数据为bit型" + Arrays.toString(MonitorActivity.lightdata));
                                } else if (datatype.equals("byte")) {   //字节
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata[i] = receBuf[i];
                                    }
                                    System.out.print("接收的数据为byte型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata[i] + ",");
                                    }
                                } else if (datatype.equals("short")) {    //short型数据
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata[i] = (short) ((0xff &  receBuf[i*2]) | (0xff00 & ( receBuf[i*2+1] << 8)));       //两字节构成一位short，小端存储,
                                    }
                                    System.out.print("接受的数据为short型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata[i] + ",");
                                    }
                                } else if (datatype.equals("int")) {     //int型数据,则需要4字节构成一个Int
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata[i] = ((int) receBuf[i * 4]) + ((int) receBuf[i * 4 + 1] << 8) + ((int) receBuf[4 * i + 2] << 16) + ((int) receBuf[i * 4 + 3] << 24);
                                    }
                                    System.out.print("接收的数据为int型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata[i] + ",");
                                    }
                                } else if (datatype.equals("float")) {       //float型数据，4字节构成float
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata2[i] = DataDeal.bytetofloat(receBuf[i * 4], receBuf[i * 4 + 1], receBuf[i * 4 + 2], receBuf[i * 4 + 3]);
                                    }
                                    System.out.print("接收的数据为float型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata2[i] + ",");
                                    }
                                } else if (datatype.equals("double")) {       //double型数据，8字节构成
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata3[i] = DataDeal.bytetodouble(receBuf[i * 4], receBuf[i * 4 + 1], receBuf[i * 4 + 2], receBuf[i * 4 + 3], receBuf[i * 4 + 4], receBuf[i * 4 + 5], receBuf[i * 4 + 6], receBuf[i * 4 + 7]);
                                    }
                                    System.out.print("接受的数据为double型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata3[i] + ",");
                                    }
                                }
                            }
                        }
                        else{          //无符号型数据
                            System.out.println("无符号数据");
                            if(MonitorActivity.modeltype=="CPU"){
                                MonitorActivity.cpudata=receBuf;
                            }
                            else {
                                if (datatype.equals("bit")) {
                                    MonitorActivity.lightdata = DataDeal.bytetobinary(receBuf);    //化为2进制码，每一灯代表1位,bit数据类型下只能获取2字节数据,即16通道。
                                    System.out.println("接收的数据为bit型" + Arrays.toString(MonitorActivity.lightdata));
                                } else if (datatype.equals("byte")) {   //无符号字节
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata[i] = DataDeal.bytesigntounsign(receBuf[i]);
                                    }
                                    System.out.print("接收的数据为byte型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata[i] + ",");
                                    }
                                } else if (datatype.equals("short")) {    //short型数据
                                    for (int i = 0; i < moderoadnum; i++) {
                                        tempx = (((int) receBuf[i * 2]) + ((int) receBuf[i * 2 + 1] << 8));       //两字节构成一位short，大小端存储反过来,
                                        MonitorActivity.lightdata[i] = DataDeal.shortsigntounsign(tempx);
                                    }
                                    System.out.print("接受的数据为short型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata[i] + ",");
                                    }
                                } else if (datatype.equals("int")) {     //int型数据,则需要4字节构成一个Int
                                    for (int i = 0; i < moderoadnum; i++) {
                                        tempx = (receBuf[i * 4]) + (receBuf[i * 4 + 1] << 8) + (receBuf[4 * i + 2] << 16) + (receBuf[i * 4 + 3] << 24);
                                        MonitorActivity.lightdatalong[i] = DataDeal.intsigntounsign(tempx);
                                    }
                                    System.out.print("接收的数据为int型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdatalong[i] + ",");
                                    }
                                } else if (datatype.equals("float")) {       //float型数据，4字节构成float
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata2[i] = DataDeal.bytetofloat(receBuf[i * 4], receBuf[i * 4 + 1], receBuf[i * 4 + 2], receBuf[i * 4 + 3]);
                                    }
                                    System.out.print("接收的数据为float型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata2[i] + ",");
                                    }
                                } else if (datatype.equals("double")) {       //double型数据，8字节构成
                                    for (int i = 0; i < moderoadnum; i++) {
                                        MonitorActivity.lightdata3[i] = DataDeal.bytetodouble(receBuf[i * 4], receBuf[i * 4 + 1], receBuf[i * 4 + 2], receBuf[i * 4 + 3], receBuf[i * 4 + 4], receBuf[i * 4 + 5], receBuf[i * 4 + 6], receBuf[i * 4 + 7]);
                                    }
                                    System.out.print("接受的数据为double型");
                                    for (int i = 0; i < moderoadnum; i++) {
                                        System.out.print(MonitorActivity.lightdata3[i] + ",");
                                    }
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setstop(boolean isstop){
        this.isStop = isstop;
    }
    public void setSleep(boolean issleep){
        this.isSleep = issleep;
    }


}
