package test;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class SendTest {

	public static void main(String [] args) throws Exception{
		
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);
		
		double gyroData;
		int[] accData = new int[3];
		
		LCD.drawString("waiting", 0, 0);
		USBConnection conn = USB.waitForConnection();
		DataOutputStream dOut = conn.openDataOutputStream();
//		DataInputStream dIn = conn.openDataInputStream();
		
		long time;
		
		for(int i = 0; i <10; i++){
			gyroData = gyro.getAngularVelocity();
			acc.getAllAccel(accData, 0);
			time = System.currentTimeMillis();
			dOut.writeBytes(gyroData + "   " + (accData[0]) + "   " + (-accData[1]) + "12.3" + "13245.99" + "12345.6" + "1234"+ "\n");
			dOut.flush();
			System.out.println(System.currentTimeMillis() - time);
			Thread.sleep(100);
		}
		dOut.writeBytes("exit\n");
		dOut.flush();
        dOut.close();
//        dIn.close();
        conn.close();
	}
}

