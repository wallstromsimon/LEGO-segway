package java_segway;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelHTSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.Accelerometer;

public class Test2 {

	public static void main(String[] args) throws InterruptedException {
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelHTSensor acc = new AccelHTSensor(SensorPort.S3);
		int a;
		gyro.recalibrateOffset();
		int accOff = 0;
		
		double gyroAng = 0;
		
		while(true){
			gyroAng += gyro.getAngularVelocity() * 0.005;			
			a = acc.getXAccel();
			System.out.println("gyro " + gyroAng);
			System.out.println("acc " + a);
			Thread.sleep(5);
		}
	}

}
