package java_segway;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;

public class Test {

	public static void main(String[] args) {
		IOMonitor m = new IOMonitor();
		IO io = new IO(5, m);
		io.start();
//		RefGen ref = new RefGen(1000, 3600, 0);
//		Regul r = new Regul(10, ref, m);
//		r.start();
		while(true){
//			m.setAngle(-2);
//			m.setPos(0);
			System.out.println(m.getAngle());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void testThisNextTime(){
//		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		//use heading calc from run method in GyroDirectionFinder to calculate angle in io class! plsWork!
//		GyroDirectionFinder g = new GyroDirectionFinder(gyro);
		//En annan tanke �r att vi kanske ska s�tta hastigheten p� motorn ist�llet f�r vart vi vill att den ska �ka
		//och sen bara s�ga om den ska �ka framm�t eller bak�t.
		
	}

}
