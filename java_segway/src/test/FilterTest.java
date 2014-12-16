package test;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class FilterTest {

	public static void main(String[] args){
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);
		
		LCD.drawString("waiting", 0, 0);
		USBConnection conn = USB.waitForConnection();
		DataOutputStream dOut = conn.openDataOutputStream();
		
		double period = 0.02;
		long t = System.currentTimeMillis();
		long duration;
		int count = 0;
		
		double ang = 0, angVel;
		double gyroF = 0, lastGyroF = 0, gyroAngle = 0, lastGyroAngle = 0;
		double accF = 0, lastAccF = 0, accAngle = 0, lastAccAngle = 0;
		
		gyro.recalibrateOffset();
		while(!Button.ESCAPE.isDown()){
			angVel = gyro.getAngularVelocity();	
			gyroAngle += angVel * period;

			//Hardcoded HP for h = 0.02 
			gyroF = 0.8182 * lastGyroF + 0.9091 * gyroAngle - 0.9091 * lastGyroAngle;
			
			accAngle = acc.getYTilt();

			//Hardcoded LP for h = 0.02 
			accF = 0.9802 * lastAccF + 0.009901 * accAngle + 0.009901 * lastAccAngle;
			
			ang = gyroF + accF;
			
			lastGyroF = gyroF;
			lastGyroAngle = gyroAngle;
			lastAccF = accF;
			lastAccAngle = accAngle;
			
			if(count%10 == 0){
				try {
					dOut.writeBytes(gyroAngle + " " + gyroF + " " + accAngle + " " + accF + " " + ang + "\n");
					dOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			count++;
			t = t + (long)(period*100);
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else{
//				System.out.println(":(");
			}
		}
		try {
			dOut.writeBytes("exit\n");
			dOut.flush();
			dOut.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
