package java_segway;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelHTSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.Accelerometer;

public class Test2 {

	public static void main(String[] args) throws InterruptedException {
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelHTSensor acc = new AccelHTSensor(SensorPort.S3);
		int[] a = new int[3];
		gyro.recalibrateOffset();
		int accOff = 0;
		int count = 0;
		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		double accAngOld = 0;
		double ang = 90;
		
		while(!Button.ESCAPE.isDown()){
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * 0.003;
			acc.getAllAccel(a, accOff);
			accAng = Math.atan2(a[2], a[0])*180/Math.PI;
//			accAng = acc.getXAccel();
//			accAng = accAng - accAngOld;
//			accAngOld = accAng;
			
			ang = (ang + gyroAng) * 0.92 + accAng * 0.08;
			
			if(count%100==0){
//				System.out.println("gyro: " + gyroAng);
//				System.out.println("acc: " + accAng);
				
				System.out.println(ang);
			}
			count++;
			Thread.sleep(3);
		}
	}

}
