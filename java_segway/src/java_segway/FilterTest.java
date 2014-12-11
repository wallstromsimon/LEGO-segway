package java_segway;

import lejos.nxt.Button;
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
		
		double ang = 0, angVel, gyroAng, accX;
		double rad2deg = 180/Math.PI;
		double period = 0.015;
		long t = System.currentTimeMillis();
		long duration;

		while(!Button.ESCAPE.isDown()){
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * period;

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			accX = acc.getXAccel() * rad2deg;
			//accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;

			ang = (ang + gyroAng) * 0.92 + accX * 0.08;
			System.out.println(gyroAng + "  " + accX + "  " + ang);
			t = t + (long)(period*1000);
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else{
				System.out.println(":(");
			}
		}
	}
}
