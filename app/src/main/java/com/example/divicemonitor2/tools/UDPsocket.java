package com.example.divicemonitor2.tools;

import com.example.divicemonitor2.DiviceinformActivity;
import com.example.divicemonitor2.MainPage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;


public class UDPsocket {
    public static DatagramSocket socket;
    public static int bendiport=3023;
    public static int shebeiport=2222;

    static {
        try {
            socket = new DatagramSocket(bendiport);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private byte sendlen;
    public static byte[] Data = new byte[256];     //储存接收数据
    private int errorNum = 0;
    public static int Starting_add=1;                           //设备直连时的起始地址

    public UDPsocket() {

    }



    /**
     * 发送CRC
     *
     * @param message
     */
    public static short GetCRC(byte[] message, int lenth)          //获取CRC校验码
    {
        byte[] crc16_h = {
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40
        };

        byte[] crc16_l = {
                (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04,
                (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8,
                (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,
                (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4,
                (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38,
                (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C,
                (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7, (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,
                (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4,
                (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68,
                (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C,
                (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0,
                (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,
                (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98,
                (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D, (byte) 0x4C, (byte) 0x8C,
                (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40
        };

        int crc = 0x0000ffff;
        int ucCRCHi = 0x00ff;
        int ucCRCLo = 0x00ff;
        int iIndex;
        for (int i = 0; i < lenth; ++i) {
            iIndex = (ucCRCLo ^ message[i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_h[iIndex];
            ucCRCHi = crc16_l[iIndex];
        }

        crc = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        //高低位互换，输出符合相关工具对Modbus CRC16的运算
        crc = ((crc & 0xFF00) >> 8) | ((crc & 0x00FF) << 8);
        System.out.println("CRC校验码为"+crc);
        return (short) crc;
    }

    private int COMFrmVrify(final byte[] message, int DataCount)    //校验码检验
    {
        int verify, temp;
        if (DataCount < 3)    //地址错误
            return 0;

        //校验码 CRC校验
        verify = GetCRC(message, DataCount - 2);

        temp = message[DataCount - 1] | (message[DataCount - 2] << 8);  //异或  左移

        if (verify == temp)
            return 1;
        return 0;
    }

    public int getint(int A) {
        int int1 = Data[A] & 0xff;
        int int2 = (Data[A + 1] & 0xff) << 8;
        int int3 = (Data[A + 2] & 0xff) << 16;
        int int4 = (Data[A + 3] & 0xff) << 24;
        return int1 | int2 | int3 | int4;
    }

    public void getString(int A, byte[] string, int len) {
        int i;
        for (i = 0; i < len; i++) {
            if (Data[A + i] == 0x00) {
                break;
            }
        }
        Arrays.fill(string, (byte) 0);
        System.arraycopy(Data, A, string, 0, i);
    }

    public void getidString(int A, byte[] string, int len) {
        Arrays.fill(string, (byte) 0);
        System.arraycopy(Data, A, string, 0, len);
    }

    public short getshort(int subscript) {
        return (short) ((0xff & Data[subscript]) | (0xff00 & (Data[subscript + 1] << 8)));
    }


    public short getsingleshort(int subscript) {
        return (short) (0xff & Data[subscript]);
    }

    public void  sendinformation(int A,int address) throws SocketException, UnknownHostException {   //发送报文,A代表寄存器数量，address代表数据地址
        try {
            socket.getLocalPort();
            InetAddress dAddress = InetAddress.getByName(DiviceinformActivity.diviceIP);
            byte [] data = getMessage(A,address);   //获取发送的报文
            DatagramPacket send_packet = new DatagramPacket(data,sendlen,dAddress,DiviceinformActivity.diviceport);
            socket.send(send_packet);
            String result = new String(send_packet.getData(), send_packet.getOffset(),
                    send_packet.getLength());
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
        System.out.println("已发送");
        //socket.close();
    }

    //构造发送帧请求数据
    public byte[] getMessage(int A,int address){   //A表示寄存器数量，address表示起始地址
        short usTemp;
        byte[] pComOutBuf=new byte[64];
        pComOutBuf[0]=01;
        pComOutBuf[1]=0x03;
        pComOutBuf[2]= (byte) ((address)>>8);   //起始地址高位
        pComOutBuf[3]=(byte)(address);      //起始地址低位
        if(A<128) {
            pComOutBuf[4] = 0x00;
            pComOutBuf[5] = (byte) A;
        }
        else{
            pComOutBuf[4]=(byte)(A-127);
            pComOutBuf[5]=127;
        }
        sendlen=8;
        usTemp=GetCRC(pComOutBuf,sendlen-2);
        pComOutBuf[sendlen - 1] = (byte)usTemp ;
        pComOutBuf[sendlen - 2] =  (byte)(usTemp >> 8) ;
        return pComOutBuf;
    }

    public void getinformation(int B){          //接收数据,B代表该模块页面的寄存器数量，
        try {
            short usTemp ;
            byte[] data = new byte[128];
            DatagramPacket packet = new DatagramPacket(data, data.length);          //接收字节
            System.out.println("开始接收");
            socket.setSoTimeout(50);   //设置连接超时时间
            socket.receive(packet);
            usTemp = GetCRC(data, data[2] + 3);    //检验CRC
            if(((byte)usTemp) == data[(int)data[2] + 5 - 1] && (byte)(usTemp >> 8) == data[(int)data[2] + 5 - 2]){      //超出索引
                MainPage.isreceivewrong = 0;
            }else{
                MainPage.isreceivewrong = 1;
            }
            System.out.println("接收结束");
            String result = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            System.out.println("返回数据为"+result);
            CassModbusMasterHandle(data,B);        //处理数据
            if(result.length()!=0)
                MainPage.islinksuccess = 1;
        }
        catch (Exception e) {
            errorNum++;
            if(errorNum>10) {
                MainPage.islinksuccess = 0;          //接收错误
                errorNum=0;
            }
            System.out.println("接收错误");

            e.printStackTrace();
        }

    }
    //处理接收的信息
    public void CassModbusMasterHandle( final byte [] message,int C){
        int bytecount=0;
        byte[] pComInBuf;
        pComInBuf=message;
        System.out.println("接收信息为"+message);
        if(((COMFrmVrify(pComInBuf, pComInBuf[2]+3)) == 1))         //校验码错误不处理信息
        {
            pComInBuf[0] = 0 ;
            return ;
        }
        if(pComInBuf[0]!=0){   //操作符
            switch(pComInBuf[1]){
                case 0x03:       //读取单个数据操作
                    bytecount=(int)pComInBuf[2];
                    initData();
                    System.arraycopy(pComInBuf,3,Data,0,bytecount);
                    System.out.println("存储为"+Data);


                    break;
                case 0x04:
                    break;
                default:
                    break;
            }
        }
        else System.out.println("数据抛弃");
    }

    private void initData(){
        for(int i=0;i<Data.length;i++){
            Data[i]=0;
        }
    }

}

