package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;

public class FeedbackController extends Thread{
	private NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	private NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	private GyroSensor gyro = new GyroSensor(SensorPort.S2);
	//	private AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

	private double period;
	private boolean run = true;

	public FeedbackController(double period){
		this.period = period;

		System.out.println("Calibrating...");
		left.stop();
		right.stop();
		left.resetTachoCount();
		right.resetTachoCount();

		gyro.recalibrateOffset();
		System.out.println("Calibrated, hold robot and press enter to balance");
		Button.ENTER.waitForPress();
		System.out.println("\"Balancing\"");
	}

	public void kill(){
		run = false;
	}

	public void run() {
		double phi = 0; //Angle
		double phiDot; //AngleVel
		double theta;//wheel
		double thetaDot;//wheel rot speed

		int power;
		double u, lastU = 0;
		double ref = 0;

		//		double rad2deg = 180/Math.PI;
		double accAng, gyroAng;
		//		int[] accV = new int[3];
		double[] lVector = {-1.0000, -23.6256, -1.3765, -3.5405};

		while (!Button.ESCAPE.isDown()){
			long t = System.currentTimeMillis();
			long duration;

			phiDot = gyro.getAngularVelocity();	
			phiDot = Math.abs(phiDot) < 1 ? 0 : phiDot;
			gyroAng = phiDot * (double)period;

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			//			acc.getAllAccel(accV, 0);
			//			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;

			//want phi --> 0
			phi = (phi + gyroAng); //* 0.92 + accAng * 0.08;
			System.out.println("Angle = " + phi);

			theta = (left.getTachoCount()+right.getTachoCount())/2.0;
			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;

			// Power sent to the motors
			u = ref - ((lVector[0]*phi) + (lVector[1]*theta) + (lVector[2]*phiDot) + (lVector[3]*thetaDot));

			//Set power and direction
			power = (int)Math.round(Math.abs(limit(u)));

			left.setSpeed(power);
			right.setSpeed(power);

			if (u > 0) {
				left.backward();
				right.backward();
			} else if (u < 0) {
				left.forward();
				right.forward();
			} else {
				left.stop();
				right.stop();
			}
			lastU = u;


			//sleep
			t = t + (long)(period*1000);
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else{
				System.out.println("oops: " + (duration-period));
			}
		}
	}

	//Limit to cap u
	private double limit(double u) {
		if (u < -100) {
			u = -100;
		} else if (u > 100) {
			u = 100;
		} 
		return u;
	}
}