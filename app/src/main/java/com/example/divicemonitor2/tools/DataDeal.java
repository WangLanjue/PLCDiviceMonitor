package com.example.divicemonitor2.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

//数据处理类
public class DataDeal {

    //颠倒一个长度2的byte数组，小端存储
    public static byte[] reverse_array(byte[] A){
        int len=A.length;
        byte[] temp=new byte[len];
        for(int i=0;i<len;i++){
            temp[i]=A[A.length-1-i];
        }
        return temp;
    }

    //字节转为string
    public static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        String strContent = "";
        strContent = new String(bytes);
        return strContent;
    }

    //4字节转为时间
    public static String bytestotime(byte[] bytes){
        String year,moon,day;
        String time;
        year=Integer.toHexString(bytes[0]);
        year=year+Integer.toHexString(bytes[1]);
        moon=Integer.toHexString(bytes[2]);
        day=Integer.toHexString(bytes[3]);
        time=year+"年"+moon+"月"+day+"日";
        return time;
    }

    //4字节码解析成ip地址
    public static String bytesToIp(byte[] bytes) {
        return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF).append('.').append(bytes[3] & 0xFF).toString();
    }

    //2位字节码解析端口号
    public static int bytetoport(byte[] bytes){
        int port1=0;
        port1=(short) ((0xff &  bytes[0]) | (0xff00 & ( bytes[1] << 8)));
        return port1;
    }

    //模块转为二进制int数组010101等,高位补0,D为长度为2的字节数组,转为长度为16的二进制编码，高位补0
    public static int [] bytetobinary(byte[] D){
        int [] num=new int[16];
        byte temp1=D[0];
        byte temp2=D[1];
        int i;
        String tstring1=Integer.toBinaryString((temp1&0xFF)+0x100).substring(1);
        String tstring2=Integer.toBinaryString((temp2&0xFF)+0x100).substring(1);
        for(i=0;i<8;i++){
            num[i]=Integer.parseInt(tstring1.substring(i,i+1));
        }
        for(i=8;i<16;i++){
            num[i]=Integer.parseInt(tstring2.substring(i-8,i-7));
        }
        return num;
    }

    //4字节组成float
    public static float bytetofloat(byte A1,byte A2,byte A3,byte A4){
        float x=0.0f;
        byte[] b={A4,A3,A2,A1};
        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(b));
        try {
            x=dis.readFloat();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    //4字节转double
    public static double bytetodouble(byte a1,byte a2,byte a3,byte a4,byte a5,byte a6,byte a7,byte a8){       //8字节组成double
        double x=0.0;
        byte[] c={a8,a7,a6,a5,a4,a3,a2,a1};
        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(c));
        try {
            x=dis.readDouble();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    //将有符号整数转为无符号数
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long intsigntounsign(int x){
        String c=Integer.toUnsignedString(x);
        long y;
        y=Long.parseLong(c);
        return y;
    }
    public static int bytesigntounsign(int x){
        return x&0XFF;
    }
    public static int shortsigntounsign(int x){
        return x&0XFFFF;
    }

}
