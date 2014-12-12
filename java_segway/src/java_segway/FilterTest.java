package java_segway;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;

public class FilterTest {

	public static void main(String[] args){
//		NXTMotor left = new NXTMotor(MotorPort.C);
//		NXTMotor right = new NXTMotor(MotorPort.B);
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

		gyro.recalibrateOffset();
		
		double ang = 0, angVel;
		double rad2deg = 180/Math.PI;
		double period = 0.02;
		long t = System.currentTimeMillis();
		long duration;
		int count = 0;
		int[] accV = new int[3];
		
		double gyroF = 0, lastGyroF = 0, gyroAngle = 0, lastGyroAngle = 0;
		double accF = 0, lastAccF = 0, accAngle = 0, lastAccAngle = 0;
		
		double accX;
		
		long time;

		while(!Button.ESCAPE.isDown()){
			time = System.currentTimeMillis();
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAngle += angVel * period;

			//Hardcoded HP for h = 0.02 
			gyroF = 0.8182 * lastGyroF + 0.9091 * gyroAngle - 0.9091 * lastGyroAngle;
			accAngle = acc.getYTilt();
//			acc.getAllAccel(accV, 0);
//			accAngle = -Math.atan2(accV[0], accV[1])*rad2deg; //+90 for standing straight

			//Hardcoded LP for h = 0.02 
			accF = 0.9802 * lastAccF + 0.009901 * accAngle + 0.009901 * lastAccAngle;
			
			ang = gyroF + accF;
			
			lastGyroF = gyroF;
			lastGyroAngle = gyroAngle;
			lastAccF = accF;
			lastAccAngle = accAngle;
			
			if(count%10 == 0){
				LCD.clear();
				LCD.drawString("Ang: " + ang, 0, 0);
//				LCD.drawString("AccX:  " + accX, 0, 2);
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
	}
}
