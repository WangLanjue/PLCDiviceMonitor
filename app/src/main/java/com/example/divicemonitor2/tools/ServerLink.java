package com.example.divicemonitor2.tools;

import com.example.divicemonitor2.DiviceinformActivity;
import com.example.divicemonitor2.MainPage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ServerLink {

    private static int sendlen;    //发送帧长度
    public static int sendmessagetype=0;   //发送数据类型

    public static byte[] Data=new byte[2048];   //存放收到的信息
    public static byte kong;     //保留1字节
    public static byte[] username=new byte[32];   //用户名
    public static byte[] usercode=new byte[32];    //用户密码
    public static byte[] authcode=new byte[4];     //授权码
    public static byte[] diviceid=new byte[12];   //设备ID
    public static byte[] divicexulie=new byte[4];  //设备序号

    public static int diviceonline_address=3;
    public static int divicename_address=4;
    public static int usecompany_address=36;
    public static int registime_address=68;
    public static int diviceIP_address=72;
    public static int port_address=88;
    public static int modetttnum_address=90;
    public static int modeinform_address=92;

    public static byte getinstallationdateruslt;//绑定 返回值当回复“绑定”和“解除绑定”请求时成功为0，失败为1，已绑定为2；当读取时为数据的数量
    public static byte [] getinstallationdate= new byte[1024];//读取用户绑定数据时 返回的设备数量，设备上限为5，每168个字节存储一个设备的数据

    public static int diviceonline;   //设备状态，0离线，1在线
    public static String divicename;   //设备名称
    public static String usecompany;  //使用单位
    public static String registime;  //注册时间
    public static String serverIP;  //服务器IP
    public static int serverport=2223;  //服务器端口
    public static int PLCport=2222;

    public static int modetotal;  //模块总数
    public static String modeinform;   //总的模块信息
    private static int i;

    private static int errorNum=0;

    //获取APP向服务器发送的信息
    public static byte[] getMessage(){
        byte[] pComOutBuf=new byte[128];
        byte[] user_name=new byte[32];
        byte[] user_code=new byte[32];
        byte[] auth_code=new byte[4];
        byte[] divice_id=new byte[12];
        Arrays.fill(pComOutBuf, (byte)0x00);  //清0
        username="wlj12340".getBytes();
        usercode="12345670".getBytes();

        authcode="0x12345678".getBytes();

        //diviceid="AM701210700001".getBytes();
        //APP向服务器请求数据帧结构：2字节MB+1字节帧类型01+1字节空nll+32字节用户名+32字节密码+4字节授权码+12字节设备ID
        //
        //
        pComOutBuf[0]=0x4D;
        pComOutBuf[1]=0x42;
        pComOutBuf[2]=01;
        pComOutBuf[3]= 0;
        System.arraycopy(username,0,pComOutBuf,4,username.length);
        System.arraycopy(usercode,0,pComOutBuf,36,usercode.length);
        pComOutBuf[68]=0x78;
        pComOutBuf[69]=0x56;
        pComOutBuf[70]=0x34;
        pComOutBuf[71]=0x12;
        System.arraycopy(diviceid,0,pComOutBuf,72,diviceid.length);
        System.arraycopy(divicexulie,0,pComOutBuf,80,divicexulie.length);
        sendlen=86;
        System.out.println(pComOutBuf);
        return pComOutBuf;
    }
//接收信息处理

    //服务器对01请求设备信息的应答为
    //SV2字节代表服务器+1字节01代表读取操作+1字节设备在线离线+32字节设备名称+32字节使用单位
    //+4字节首次注册时间+16字节设备IP+2字节端口号+1字节模块总数+1字节空+8字节CPU型号+4字节设备ID+8字节CPU型号+4字节ID。。。。。
    //模块数给出，可以直接推导出，每12字节为一个模块命名ID
    public static void CassModbusMasterHandle(final byte[] message){    //
        byte [] pComInBuf;
        pComInBuf=message;
        System.out.println(message);
        if(pComInBuf[0]==0x53&&pComInBuf[1]==0x56){//来自服务器数据SV,
            System.out.println("来自服务器数据SV");
            switch (pComInBuf[2]){
                case 01:       //01表示EPLC构成信息
                    System.out.println("01设备信息");
                    System.arraycopy(pComInBuf,diviceonline_address,Data,diviceonline_address-3,1);  //复制设备在线状态
                    System.arraycopy(pComInBuf,divicename_address,Data,divicename_address-3,32);  //复制设备名称
                    System.arraycopy(pComInBuf,usecompany_address,Data,usecompany_address-3,32);  //复制使用单位
                    System.arraycopy(pComInBuf,registime_address,Data,registime_address-3,4);  //复制注册时间
                    System.arraycopy(pComInBuf,diviceIP_address,Data,diviceIP_address-3,16);  //复制设备IP
                    System.arraycopy(pComInBuf,port_address,Data,port_address-3,2);   //复制设备端口
                    System.arraycopy(pComInBuf,modetttnum_address,Data,modetttnum_address-3,1);   //复制模块数
                    //服务器返回帧第91字节为保留空，不管
                    for(i=1;i<=Data[87];i++){
                        if((92+(i-1)*12+12)<1024){
                            System.arraycopy(pComInBuf, modeinform_address + (i - 1) * 12, Data, modeinform_address-3 + (i - 1) * 12, 12);     //复制每个模块信息
                        }
                    }
                    System.arraycopy(pComInBuf,modeinform_address+i*12,Data,modeinform_address-3+i*12,256);   //复制注释信息
                    MainPage.islinksuccess=1;
                    break;
                case (byte) 254:
                    DiviceinformActivity.errtext.setText("校验信息不通过");
                    break;
                case (byte) 255:
                    DiviceinformActivity.errtext.setText("设备不存在或者ID错误");
                    break;
                default:break;
            }
        }

        else if(pComInBuf[0]==0x44&&pComInBuf[1]==0x56){       //来自设备的信息DV
            System.out.println("来自设备的信息DV");
            switch(pComInBuf[2]){
                case 01:
                    System.out.println("01设备信息");
                    System.arraycopy(pComInBuf,3,Data,modetttnum_address-3,1);   //复制模块数到Data[87]
                    for(i=1;i<=Data[modetttnum_address-3];i++){
                        if((modetttnum_address+(i-1)*12+12)<1024){
                            System.arraycopy(pComInBuf, 4 + (i - 1) * 12, Data, modeinform_address-3 + (i - 1) * 12, 12);     //复制每个模块ID和序号信息
                        }
                    }
                    System.arraycopy(pComInBuf,4+i*12,Data,modeinform_address-3+i*12,256);   //复制注释信息
                    MainPage.islinksuccess=1;
                    break;
                case 02:
                    break;

                default:break;

            }
        }
        else System.out.println("数据抛弃");

    }
    //向服务器发送请求数据帧
    public static void sendinformation(){
        try {
            UDPsocket.socket.getLocalPort();
            InetAddress serveraddress = InetAddress.getByName(serverIP);
            byte[] data = getMessage();
            System.out.println(Arrays.toString(data));
            DatagramPacket send_packet = new DatagramPacket(data, sendlen, serveraddress,serverport);
            UDPsocket.socket.send(send_packet);
            System.out.println("已发送请求");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("发送失败");
        }
    }
    //接收服务器发来的数据帧
    public static void getinforamation(){
        try {
            byte[] data = new byte[1024];
            DatagramPacket get_packet = new DatagramPacket(data, data.length);
            UDPsocket.socket.setSoTimeout(200);
            UDPsocket.socket.receive(get_packet);
            System.out.println("返回信息为" + Arrays.toString(data));
            MainPage.testservercon=1;
            CassModbusMasterHandle(data);
            System.out.println("接收成功");
        }
        catch (SocketException e){
            e.printStackTrace();
            System.out.println("接收失败，socket出错");
            errorNum++;
            if(errorNum>8) {
                MainPage.islinksuccess = 0;
                errorNum=0;
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("接收失败，返回错误");
            errorNum++;
            if(errorNum>8) {
                MainPage.islinksuccess = 0;
                errorNum=0;
            }
        }
    }


    //关闭socket
    public static void socketclose(){
        UDPsocket.socket=null;
        UDPsocket.socket.close();
        UDPsocket.socket.disconnect();
    }

    //16进制转byte
    public static byte[] sixteentobyte(String hexstring){
        byte[] destByte=new byte[hexstring.length()/2];
        int j=0;
        for(int i=0;i<destByte.length;i++){
            byte high = (byte) (Character.digit(hexstring.charAt(j), 16) & 0xff);
            byte low = (byte) (Character.digit(hexstring.charAt(j + 1), 16) & 0xff);
            destByte[i] = (byte) (high << 4 | low);
            j+=2;
        }
        return destByte;
    }


    //char变为byte,java中一个char占2字节
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

}
