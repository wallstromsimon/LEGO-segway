package java_segway;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class FeedbackController extends Thread implements Controller{
	private NXTMotor left;
	private NXTMotor right;
	private GyroSensor gyro;
	private AccelMindSensor acc;

	private double period;
	private boolean run = true;
	private boolean log = false;

	private USBConnection conn;
	private DataOutputStream dOut;

	public FeedbackController(double period){
		this.period = period;
  
		this.setPriority(MAX_PRIORITY);
		
		left = new NXTMotor(MotorPort.C);
		right = new NXTMotor(MotorPort.B);
		gyro = new GyroSensor(SensorPort.S2);
		acc = new AccelMindSensor(SensorPort.S3);
		
		if(log){
			LCD.drawString("waiting", 0, 0);
			conn = USB.waitForConnection();
			dOut = conn.openDataOutputStream();
		}
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

		double[] lVector = {-8.7,0,-0.038,0};// :(   bäst värden so far {-8.7,0,-0.038,0} utan setAcceleration
													// med setAcceleration 2100 {-13.8,-0.075,0}
		
		System.out.println("Calibrating...");
		left.stop();
		right.stop();
		left.resetTachoCount();
		right.resetTachoCount();
//		left.setAcceleration(2050);
//		right.setAcceleration(2050);


		gyro.recalibrateOffset();
		LCD.clear();
		System.out.println("Calibrated, hold robot and press enter to balance");
		Button.ENTER.waitForPress();
		LCD.clear();
		System.out.println("\"Balancing\"");
		
		long t = System.currentTimeMillis();
		while (run){
			phiDot = gyro.getAngularVelocity();	
			phiDot = Math.abs(phiDot) < 1 ? 0 : phiDot;
			gyroAng = phiDot * period;

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			acc.getAllAccel(accV, 0);
			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 88.578;

			phi = (phi + gyroAng) * 0.965 + accAng * 0.035;

			theta = (left.getTachoCount()+right.getTachoCount())/2.0;
//			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;

			// Power sent to the motors
			vPhi = lVector[0]*phi;
			vTheta = lVector[1]*theta;
			vPhidot = lVector[2]*phiDot;
			vThetaDot = lVector[3]*thetaDot;
			
			u = limit(ref + vPhi + vTheta + vPhidot + vThetaDot);

			//Set power and direction
			//Power = u% of max speed of the motor.
			power = (int)Math.round(Math.abs((limit(u+3))));

			left.setPower(power);
			right.setPower(power);
			
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
//			System.out.println(duration);
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
