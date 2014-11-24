package java_segway;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;

public class IO extends Thread{
	NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	private int speed;
	private long period;
	IOMonitor ioM;
	GyroDirectionFinder gdf;

	public IO(long period, IOMonitor ioM) {
		this.period = period;
		this.ioM = ioM;
		gdf = new GyroDirectionFinder(gyro, true);
//		gyro.recalibrateOffset();
		speed = 720;
		left.setSpeed(speed);
		right.setSpeed(speed);
		this.setPriority(7);
	}

	public synchronized void reset() {
		left.resetTachoCount();
		right.resetTachoCount();
	}

	public void run(){
		long t = System.currentTimeMillis();
		while(true){
			//Set motor
			int pos = (int)Math.round(ioM.getMotor());
			left.rotate(pos, true);
			right.rotate(pos, true);
			
			//Update pos
			ioM.setPos((left.getTachoCount() + right.getTachoCount())/2);
			
			//Calc and update angle
//			ioM.setAngle((gyro.readValue()-gyro.getAngularVelocity()) * (period/1000));
			ioM.setAngle(gdf.getDegrees());
			
			t = t + period;
			long duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}