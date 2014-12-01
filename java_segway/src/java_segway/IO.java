package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelHTSensor;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;


public class IO extends Thread{
	NXTMotor left = new NXTMotor(MotorPort.C);
	NXTMotor right = new NXTMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

//	private int speed;
	private long period;
	IOMonitor ioM;

	public IO(long period, IOMonitor ioM) {
		this.period = period;
		this.ioM = ioM;
		System.out.println("Calibrating");

//		speed = 350;
//		left.setAcceleration(8000);
//		right.setAcceleration(8000);
//		left.setSpeed(speed);
//		right.setSpeed(speed);
		left.stop();
		right.stop();
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

		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		double angle = 0;
		int accX, accY;
		int pos, power;

		double rad2deg = 180/Math.PI;
		double deg2rad = Math.PI/180;

		while(!Button.ESCAPE.isDown()){
			
			//Set motor
			pos = (int)(Math.round(ioM.getMotor()));
			power = Math.abs(pos);
			
			if(power < 5){
				left.flt();
				right.flt();
			}
			else if(pos < 0){
				left.backward();
				right.backward();
			}else{
				left.forward();
				right.forward();
			}
			left.setPower(power);
			right.setPower(power);

//			left.rotate(pos, true);
//			right.rotate(pos, true);

			//Update pos
			ioM.setPos((left.getTachoCount()+right.getTachoCount())/2*deg2rad);

			//Calc and update angle
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * (double)period/1000;
			accX = -acc.getXAccel();
			accY = -acc.getYAccel();
			accAng = -Math.atan2(accY, accX)*rad2deg;
			angle = (angle + gyroAng) * 0.92 + accAng * 0.08;
			ioM.setAngle(angle*deg2rad);

			
			t = t + period;
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else{
				System.out.println("oops" + (duration-period));
			}
		}

	}
}