package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelHTSensor;
import lejos.nxt.addon.GyroSensor;

public class IO extends Thread{
	NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	AccelHTSensor acc = new AccelHTSensor(SensorPort.S3);
	
	private int speed;
	private long period;
	IOMonitor ioM;

	public IO(long period, IOMonitor ioM) {
		this.period = period;
		this.ioM = ioM;
		System.out.println("Calibrating");

		speed = 800;
		left.setAcceleration(8000);
		right.setAcceleration(8000);
		left.setSpeed(speed);
		right.setSpeed(speed);
		left.resetTachoCount();
		right.resetTachoCount();

		gyro.recalibrateOffset();
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
		
//		double a = 0.9999999;
//		double lastOffset = 0;
//		boolean first = true;
		
		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		double angle = 0;
		int[] accData = new int[3];
		int pos;
		
		double rad2deg = 180/Math.PI;
				
		while(!Button.ESCAPE.isDown()){
			//Set motor
			//			double angle2 = ioM.getAngle();
			//			left.setAcceleration((int)Math.round(angle2 * 1000));
			//			right.setAcceleration((int)Math.round(angle2 * 1000));

			pos = (int)(Math.round(ioM.getMotor()));
			System.out.println(pos);
			left.rotate(pos, true);
			right.rotate(pos, true);
			
			//Update pos
			ioM.setPos((left.getTachoCount() + right.getTachoCount())/2);

			//Calc and update angle
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * (double)period/1000;
			acc.getAllAccel(accData, 0);
			accAng = Math.atan2(accData[2], accData[0])*rad2deg;
			angle = (angle + gyroAng) * 0.92 + accAng * 0.08;
			ioM.setAngle(-angle);
			
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

	//	private int updateAng(){
	//		
	//kanske inte behövs
//	double degreesPerSecond, secondsSinceLastReading, smoothedValue;
//	long lastUpdate;
//	long now = System.currentTimeMillis();
	//		angle = 0;
	//		smoothedValue = 0;
	//		//Calc and update angle
	//		//			ioM.setAngle((gyro.readValue()-gyro.getAngularVelocity()) * (period/1000));
	//		//			ioM.setAngle(gdf.getDegrees()+90);
	//		//			if(gdf.getAngularVelocity()<1){
	//		//				gdf.setDegrees(0);
	//		//			}
	//
	//		lastUpdate = now;
	//		now = System.currentTimeMillis();
	//		secondsSinceLastReading = (now - lastUpdate) * .001;
	//
	//		degreesPerSecond=gyro.getAngularVelocity();
	//
	////		if (Math.abs(degreesPerSecond)<1.0) degreesPerSecond=0.0; 
	//
	//		// Integration
	//
	//		lastOffset = (1-a) * degreesPerSecond + a * lastOffset;
	//		degreesPerSecond = degreesPerSecond - lastOffset; // Angular velocity (degrees/sec)
	//
	//		
	//		//lowpass?
	////		smoothedValue += (degreesPerSecond - smoothedValue)/111;
	////		smoothedValue += (newValue - smoothedValue) / smoothing
	//		
	//		
	//		angle += degreesPerSecond * secondsSinceLastReading;
	//		ioM.setAngle(angle);
	//		return 0;
	//	}
}