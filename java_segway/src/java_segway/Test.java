package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class Test {

	public static void main(String[] args) {
		NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
		GyroSensor gyro = new GyroSensor(SensorPort.S2);

		gyro.recalibrateOffset();
		while(true){
			System.out.println(gyro.getAngularVelocity());
			System.out.println(gyro.readValue());
			Button.waitForAnyPress();
		}
		//left.setSpeed(720);

		//		System.out.println(left.getTachoCount());
		//		left.rotate(360);
		//		System.out.println(left.getTachoCount());
		//		left.rotate(-360);
		//		System.out.println(left.getTachoCount());
		//		left.rotate(460);
		//		System.out.println(left.getTachoCount());
		
	}

}
