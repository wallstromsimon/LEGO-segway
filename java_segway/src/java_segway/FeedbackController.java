package java_segway;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class FeedbackController extends Thread implements Controller{
	private NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	private NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	private GyroSensor gyro = new GyroSensor(SensorPort.S2);
	private AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

	private double period;
	private boolean run = true;
	private boolean log = true;

	private USBConnection conn;
	private DataOutputStream dOut;

	public FeedbackController(double period){
		this.period = period;

		if(log){
			LCD.drawString("waiting", 0, 0);
			conn = USB.waitForConnection();
			dOut = conn.openDataOutputStream();
		}

		System.out.println("Calibrating...");
		left.stop();
		right.stop();
		left.resetTachoCount();
		right.resetTachoCount();

		gyro.recalibrateOffset();
		LCD.clear();
		System.out.println("Calibrated, hold robot and press enter to balance");
		Button.ENTER.waitForPress();
		LCD.clear();
		System.out.println("\"Balancing\"");
	}

	public void kill(){
		run = false;
	}

	private double limit(double u) {
		if (u < -100) {
			u = -100;
		} else if (u > 100) {
			u = 100;
		} 
		return u;
	}

	public void run() {
		long t = System.currentTimeMillis();
		long duration;

		double phi = 0; //Angle
		double phiDot = 0; //AngleVel
		double theta  = 0;//wheel
		double thetaDot = 0;//wheel rot speed

		double vPhi, vPhidot, vTheta, vThetaDot;
		
		int power;
		double u, lastU = 0;
		double ref = 0;

		int counter = 0;

		double rad2deg = 180/Math.PI;
		double accAng, gyroAng;
		int[] accV = new int[3];
		double[] lVector = {-12, 0, -0.2, 0};// :(

		while (run){
			phiDot = gyro.getAngularVelocity();	
			phiDot = Math.abs(phiDot) < 1 ? 0 : phiDot;
			gyroAng = phiDot * period;

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			acc.getAllAccel(accV, 0);
			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;

			phi = (phi + gyroAng) * 0.98 + accAng * 0.02;

			theta = (left.getTachoCount()+right.getTachoCount())/2.0;
			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;

			// Power sent to the motors
			vPhi = lVector[0]*phi;
			vTheta = lVector[1]*theta;
			vPhidot = lVector[2]*phiDot;
			vThetaDot = lVector[3]*thetaDot;
			
			u = limit(ref + vPhi + vTheta + vPhidot + vThetaDot);

			//Set power and direction
			power = (int)Math.round(Math.abs((u * ((left.getMaxSpeed() + right.getMaxSpeed())/2.0)/100)));
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

			//Save data, ~16ms
			if(log && counter%10==0){
				try {
					dOut.writeBytes(phi + " " + vPhi + " " + phiDot + " " + vPhidot + " " + u + "\n");
					dOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			counter++;

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
		//Stop and save on exit
		left.stop();
		right.stop();
		if(log){
			try {
				dOut.writeBytes("exit\n");
				dOut.flush();
				dOut.close();
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
