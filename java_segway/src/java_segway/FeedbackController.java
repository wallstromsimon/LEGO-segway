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

	private float period;
	private boolean run;
	private boolean log;
	
	private float uMin;
	private float uMax;

	private USBConnection conn;
	private DataOutputStream dOut;

	public FeedbackController(float period){
		this.period = period;
		this.setPriority(MAX_PRIORITY);
		
		run = true;
		log = false;
		
		uMin = -100;
		uMax = 100;
		
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

	private float limit(float u) {
		if (u < uMin) {
			u = uMin;
		} else if (u > uMax) {
			u = uMax;
		} 
		return u;
	}

	public void run() {
		long duration;

		float phi = 0; //Angle
		float phiDot = 0; //AngleVel
		float theta  = 0;//wheel
		float thetaDot = 0;//wheel rot speed, uppdateras aldrig.

		float vPhi, vPhidot, vTheta, vThetaDot;
		
		int power;
		float u, lastU = 0;
		float ref = 0;

		int counter = 0;

		float[] lVector = {-8.7f,0,-0.038f,0};// bäst värden so far {-8.7,0,-0.038,0} utan setAcceleration
		
		float gyroF = 0, lastGyroF = 0, gyroAngle = 0, lastGyroAngle = 0;
		float accF = 0, lastAccF = 0, accAngle = 0, lastAccAngle = 0;
		
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
		
		long t = System.currentTimeMillis();
		while (run){
			//Gyro calc wieh hardcoded HP for h = 0.02 
			phiDot = gyro.getAngularVelocity();	
			gyroAngle += phiDot * period;
			gyroF = 0.8182f * lastGyroF + 0.9091f * gyroAngle - 0.9091f * lastGyroAngle;

			//Acc calc with hardcoded LP for h = 0.02 
			accAngle = acc.getYTilt();
			accF = 0.9802f * lastAccF + 0.009901f * accAngle + 0.009901f * lastAccAngle;
			
			phi = gyroF + accF;

			theta = (float) ((left.getTachoCount()+right.getTachoCount())/2.0);
//			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;

			// Power sent to the motors
			vPhi = lVector[0]*phi;
			vTheta = lVector[1]*theta;
			vPhidot = lVector[2]*phiDot;
			vThetaDot = lVector[3]*thetaDot;
			
			u = limit(ref + vPhi + vTheta + vPhidot + vThetaDot);

			//Set power and direction
			power = (int)Math.round(Math.abs(u)+3);

			left.setPower(power);
			right.setPower(power);
			
			if (u > 0 && lastU <= 0){
				left.backward();
				right.backward();
			} else if (u < 0 && lastU >= 0){
				left.forward();
				right.forward();
			}
			
			//Update stuff
			lastGyroF = gyroF;
			lastGyroAngle = gyroAngle;
			lastAccF = accF;
			lastAccAngle = accAngle;
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
