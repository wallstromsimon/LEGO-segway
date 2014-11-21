package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
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
	
	public void testThisNextTime(){
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		//use heading calc from run method in GyroDirectionFinder to calculate angle in io class! plsWork!
		GyroDirectionFinder g = new GyroDirectionFinder(gyro);
		//En annan tanke är att vi kanske ska sätta hastigheten på motorn istället för vart vi vill att den ska åka
		//och sen bara säga om den ska åka frammåt eller bakåt.
		
	}

}
