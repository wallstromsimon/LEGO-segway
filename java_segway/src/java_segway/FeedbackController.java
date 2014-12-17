package java_segway;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
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
	
	private Lvalues l;

	public FeedbackController(float period){
		this.period = period;
		this.setPriority(MAX_PRIORITY);
		
		run = true;
		log = false;
		
		uMin = -1000;
		uMax = 1000;
		
		left = new NXTMotor(MotorPort.C);
		right = new NXTMotor(MotorPort.B);
		gyro = new GyroSensor(SensorPort.S2);
		acc = new AccelMindSensor(SensorPort.S3);
		
		l = new Lvalues();
		l.updateL(-9.5f, -0.350f, -0.500f, -0.215f);
		
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
		float phiDot = 0; //Angle Velocity
		float theta  = 0;//Wheel Angle
		float thetaDot = 0;//Wheel rotation speed, uppdateras aldrig.

		float vPhi, vPhidot, vTheta, vThetaDot;
		
		int power;
		float u, lastU = 0;
		float ref = 0;

		int counter = 0;

//		float[] lVector = {-30.0f, 0f, -0.030f, -0.025f};
		
		float gyroF = 0, lastGyroF = 0, gyroAngle = 0, lastGyroAngle = 0;
		float accF = 0, lastAccF = 0, accAngle = 0, lastAccAngle = 0;
		float lastTheta = 0;
		
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
			
			//Gyro calc with hardcoded HP for h = 0.01 resp 0.02 
			phiDot = gyro.getAngularVelocity();	
			gyroAngle += phiDot * period;
			gyroF = 0.9048f * lastGyroF + 0.9524f * gyroAngle - 0.9524f * lastGyroAngle;//10ms period
//			gyroF = 0.8182f * lastGyroF + 0.9091f * gyroAngle - 0.9091f * lastGyroAngle;//20ms period
			
			//Acc calc with hardcoded LP for h = 0.01 resp 0.02 
			accAngle = acc.getYTilt();
			accF = 0.99f * lastAccF + 0.004975f * accAngle + 0.004975f * lastAccAngle;//10ms period
//			accF = 0.9802f * lastAccF + 0.009901f * accAngle + 0.009901f * lastAccAngle;//20ms period
			
			phi = gyroF + accF + 0.375f; // minus om framåtvikt.. plus bakåt; innan var det 0.38

			theta = (float) ((left.getTachoCount()+right.getTachoCount())/2.0);
			thetaDot = (theta - lastTheta) / period;
			lastTheta = theta;
//			thetaDot = (left.getRotationSpeed()+right.getRotationSpeed())/2.0;
			
			
			// Power sent to the motors
			vPhi = l.getL1()*phi;
			vTheta = l.getL2()*theta;
			vPhidot = l.getL3()*phiDot;
			vThetaDot = l.getL4()*thetaDot;
			
			u = limit(ref + vPhi + vTheta + vPhidot + vThetaDot);

			//Set power and direction
			power = (int)Math.round(Math.abs(u)+2);

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
//				System.out.println("oops: " + (duration-(period*1000)));
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
	
	public synchronized void updateL(float L1, float L2, float L3, float L4){
		l.updateL(L1, L2, L3, L4);
	}
	
	private class Lvalues{
		private float L1; 
		private float L2;
		private float L3;
		private float L4;
		
		private synchronized void updateL(float L1, float L2, float L3, float L4){
			this.L1 = L1;
			this.L2 = L2;
			this.L3 = L3;
			this.L4 = L4;
		}
		
		private synchronized float getL1(){
			return L1;
		}
		
		private synchronized float getL2(){
			return L2;
		}
		
		private synchronized float getL3(){
			return L3;
		}
		
		private synchronized float getL4(){
			return L4;
		}
	}
}
