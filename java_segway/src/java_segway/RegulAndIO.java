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

public class RegulAndIO extends Thread{
	private NXTMotor left = new NXTMotor(MotorPort.C);
	private NXTMotor right = new NXTMotor(MotorPort.B);
	private GyroSensor gyro = new GyroSensor(SensorPort.S2);
	private AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

	private double period;
	private PIDController inner;
	//private PIDController outer;

	//private RefGen refGen;

	private double uMin = -100;
	private double uMax = 100;

	private boolean run = true;
	private boolean log = true;

	private USBConnection conn;
	private DataOutputStream dOut;

	public RegulAndIO(double period) {
		this.period = period;
		//this.refGen = refGen; //Use 0 as ref

		//K,Ti,Tr,Td,N,b,H   6.3, 3, 0.9, 0.3, 10, 1, period
		inner = new PIDController(8.6, 3, 0.7, 0.3, 10, 1, period);
		//outer = new PIDController(6.3, 3, 0.9, 0.3, 10, 1, period); //tuning inner loop right now

		//Set up USB contact to send files
		if(log){
			LCD.drawString("waiting", 0, 0);
			conn = USB.waitForConnection();
			dOut = conn.openDataOutputStream();
		}

		//Starting and calibrating
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

	//Limit to cap u
	private double limit(double u) {
		if (u < uMin) {
			u = uMin;
		} else if (u > uMax) {
			u = uMax;
		} 
		return u;
	}

	//Kill robot when ESC is pressed, called from OpCom
	public synchronized void kill(){
		run = false;
	}

	public void run(){
		long t = System.currentTimeMillis();
		long duration;

		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		int[] accV = new int[3];

		//		double youter,uouter;
		double yinner = 0, ref;
		int power, uinner, lastUinner = 0;

		double rad2deg = 180/Math.PI;
		//		double deg2rad = Math.PI/180;
		long counter = 0;//used when saving data

		while(run){//12ms with accelerometer
			ref = 0;//refGen.getRef();

			//Calculate outer loop
			//youter = (left.getTachoCount()+right.getTachoCount())/2*deg2rad;
			//uouter = outer.calculateOutput(youter, ref);

			//Calculate inner loop
			//1ms read gyro
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = (angVel * (double)period/1000);

			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			acc.getAllAccel(accV, 0);
			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;

			yinner = (yinner + gyroAng) * 0.92 + accAng * 0.08;
			uinner = (int)(Math.round(limit(inner.calculateOutput(yinner, ref))));//uouter

			//Set power and direction
			power = Math.abs(uinner);
			left.setPower(power);
			right.setPower(power);

			if(uinner > 0 && lastUinner <= 0){
				left.backward();
				right.backward();
			}else if(uinner < 0 && lastUinner >= 0){
				left.forward();
				right.forward();
			}
			lastUinner = uinner;

			//Save data, ~16ms
			if(log && counter%10==0){
				try {
					dOut.writeBytes(inner.getP() + " " + inner.getI() + " " + inner.getD() + " " + inner.getE() + " " + uinner + " " + yinner + "\n");
					dOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			counter++;

			//Update controller states
			inner.updateState(uinner);
			//			outer.updateState();

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
//				System.out.println("oops: " + (duration-period));
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
