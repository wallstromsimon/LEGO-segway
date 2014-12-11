package java_segway;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;

public class FeedbackController extends Thread implements Controller{
	private NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	private NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	private GyroSensor gyro = new GyroSensor(SensorPort.S2);
	private AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

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

	private double limit(double u) {
		if (u < -right.getMaxSpeed()) {
			u = -right.getMaxSpeed();
		} else if (u > right.getMaxSpeed()) {
			u = right.getMaxSpeed();
		} 
		return u;
	}

	public void run() {
		double phi = 0; //Angle
		double phiDot = 0; //AngleVel
		double theta  = 0;//wheel
		double thetaDot = 0;//wheel rot speed

		int power;
		double u, lastU = 0;
		double ref = 0;

		double rad2deg = 180/Math.PI;
		double accAng, gyroAng;
		int[] accV = new int[3];
		double[] lVector = {-80, 0, -2.20, 0};// :(

		while (run){
			long t = System.currentTimeMillis();
			long duration;

			phiDot = gyro.getAngularVelocity();	
			phiDot = Math.abs(phiDot) < 1 ? 0 : phiDot;
			gyroAng = phiDot * period;

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			acc.getAllAccel(accV, 0);
			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;

			phi = (phi + gyroAng) * 0.985 + accAng * 0.015;

			theta = (left.getTachoCount()+right.getTachoCount())/2.0;
			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;

			// Power sent to the motors
			u = ref + ((lVector[0]*phi) + (lVector[1]*theta) + (lVector[2]*phiDot) + (lVector[3]*thetaDot));

			//Set power and direction
			System.out.println(u);
			power = (int)Math.round(Math.abs((limit(u))));
			left.setSpeed(power);
			right.setSpeed(power);
			
			if (u > 0 && lastU <= 0){
				left.backward();
				right.backward();
			} else if (u < 0 && lastU >= 0){
				left.forward();
				right.forward();
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
				System.out.println("oops: " + (duration-(period*1000)));
			}
		}
		System.exit(0);
	}
}
