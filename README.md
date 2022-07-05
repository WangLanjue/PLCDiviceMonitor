# PLCDiviceMonitor
刚开始学android的时候利用实验室设备做的一个小APP监控PLC设备，通过viewpager+fragment实现滑动窗口，使用sharedPreferences存储设备数据，采用UDP通讯，Modbus协议接受设备数据，只具备读取功能，不支持写入。

## 实际效果：
打开APP可以提示是否连接上次设备
<img src="https://user-images.githubusercontent.com/98386278/177317418-f0f0ce17-b26f-42c7-8b66-d6cc9a4876b5.jpg" width="100px">

编辑界面，编辑和保存设备信息：
![S20702-100712](https://user-images.githubusercontent.com/98386278/177317561-759eda6a-b7b2-401c-afe4-8c51c79df729.jpg) 
![S20705-193812](https://user-images.githubusercontent.com/98386278/177320684-109ef823-6ac9-42bf-894e-1b775c2dbbbe.jpg)

各个模块界面
![S20702-100741](https://user-images.githubusercontent.com/98386278/177317643-02f160da-6dfd-4ee3-91f0-232c50673992.jpg)
![S20702-100741](https://user-images.githubusercontent.com/98386278/177317667-9c921ba3-e4f1-4391-83d5-4fe9589d7a71.jpg)
![S20702-100801](https://user-images.githubusercontent.com/98386278/177317691-6c5ffe13-d9f1-41da-8a25-b503a0f6b4a7.jpg)

界面设计挺糟糕，采用4个不同的xml文件存储4个不同设备信息，可以通过滑动切换不同PLC模块，根据不同模块的不同地址区间发送请求帧，首先连接服务器取得设备模块型号，跟保存的配置文件进行匹配，随后进行设备监控。
导入了github.maning0303制作的二维码扫描库，可采用扫描二维码的方法获得设备型号信息。
![S20705-194607](https://user-images.githubusercontent.com/98386278/177320721-4ab59b06-effa-42e8-bbd8-70a712d2ae94.jpg)
