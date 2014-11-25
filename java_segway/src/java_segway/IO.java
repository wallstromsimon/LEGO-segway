package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class IO extends Thread{
	NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	private int speed;
	private long period;
	IOMonitor ioM;
	double angle;

	public IO(long period, IOMonitor ioM) {
		this.period = period;
		this.ioM = ioM;
		System.out.println("Calibrating");
		gyro.recalibrateOffset();
		speed = 400;
//		left.setAcceleration(10000);
//		right.setAcceleration(10000);
		left.setSpeed(speed);
		right.setSpeed(speed);
		left.resetTachoCount();
		right.resetTachoCount();
		System.out.println("Calibrated, hold robot and press any key to balance");
		Button.waitForAnyPress();
		System.out.println("\"Balancing\"");
	}

	public synchronized void reset() {
		left.resetTachoCount();
		right.resetTachoCount();
	}

	public void run(){
		long t = System.currentTimeMillis();
		long duration;
		double degreesPerSecond, secondsSinceLastReading, smoothedValue;
		long lastUpdate;
		long now = System.currentTimeMillis();
		
		int last = 0, curr;
		
		double EMAOFFSET = 0.0005;
		double gOffset = 1;
		angle = 0;
		smoothedValue = 0;

		int pos;
		while(true){
			//Set motor
			pos = (int)(Math.round(ioM.getMotor()));
//			speed = Math.abs(pos/23);
//			left.setSpeed(speed);
//			right.setSpeed(speed);
			left.rotate(pos, true);
			right.rotate(pos, true);

			//Update pos
			ioM.setPos((left.getTachoCount() + right.getTachoCount())/2);

			//Calc and update angle
			//			ioM.setAngle((gyro.readValue()-gyro.getAngularVelocity()) * (period/1000));
			//			ioM.setAngle(gdf.getDegrees()+90);
			//			if(gdf.getAngularVelocity()<1){
			//				gdf.setDegrees(0);
			//			}

			lastUpdate = now;
			now = System.currentTimeMillis();

			degreesPerSecond=gyro.getAngularVelocity();

			if (Math.abs(degreesPerSecond)<1.0f) degreesPerSecond=0.0f; 

			// Integration

			gOffset = EMAOFFSET * degreesPerSecond + (1-EMAOFFSET) * gOffset;
			degreesPerSecond = degreesPerSecond - gOffset; // Angular velocity (degrees/sec)

			secondsSinceLastReading = (now - lastUpdate) * .001f;
			
			//lowpass?
//			smoothedValue += (degreesPerSecond - smoothedValue)/111;
//			smoothedValue += (newValue - smoothedValue) / smoothing
			
			
			angle += degreesPerSecond * secondsSinceLastReading;
			
			curr = (int)Math.round(angle*10);
			angle = curr==last ? 0 : angle;
			last = curr;
			
			ioM.setAngle(angle);

			t = t + period;
			duration = t - System.currentTimeMillis();
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