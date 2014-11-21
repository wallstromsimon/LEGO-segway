package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class Test {

	public static void main(String[] args) {
		IOMonitor m = new IOMonitor();
		IO io = new IO(10, m);
		io.start();
		while(true){
			System.out.println(m.getAngle());
			Button.waitForAnyPress();
		}
	}

}
