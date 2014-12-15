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

public class RegulAndIO extends Thread implements Controller{
	private NXTMotor left;
	private NXTMotor right;
	private GyroSensor gyro;
	private AccelMindSensor acc;

	private float period;
	private PIDController inner;
	//private PIDController outer;

	//private RefGen refGen;

	private float uMin;
	private float uMax;

	private boolean run;
	private boolean log;

	private USBConnection conn;
	private DataOutputStream dOut;

	public RegulAndIO(float period) {
		this.setPriority(MAX_PRIORITY);
		this.period = period;
		//this.refGen = refGen; //Use 0 as ref

		//K,Ti,Tr,Td,N,b,H   6.3, 3, 0.9, 0.3, 10, 1, period
		inner = new PIDController(9f, 5f, 1f, 0.2f, 10f, 1f, period);
		//outer = new PIDController(6.3, 3, 0.9, 0.3, 10, 1, period); //tuning inner loop right now

		left = new NXTMotor(MotorPort.C);
		right = new NXTMotor(MotorPort.B);
		gyro = new GyroSensor(SensorPort.S2);
		acc = new AccelMindSensor(SensorPort.S3);

		run = true;
		log = false;
		uMin = -100;
		uMax = 100;

		//Set up USB contact to send files
		if(log){
			LCD.drawString("waiting", 0, 0);
			conn = USB.waitForConnection();
			dOut = conn.openDataOutputStream();
		}
	}

	//Limit to cap u
	private float limit(float u){
		if (u < uMin) {
			u = uMin;
		} else if (u > uMax){
			u = uMax;
		} 
		return u;
	}

	//Kill robot when ESC is pressed, called from OpCom
	public synchronized void kill(){
		run = false;
	}

	public void run(){
		long duration;

		float angVel = 0;
		
		//float youter,uouter;
		
		int power, uinner, lastUinner = 0;
		float yinner = 0;
		float ref = 0;
		
		float gyroF = 0, lastGyroF = 0, gyroAngle = 0, lastGyroAngle = 0;
		float accF = 0, lastAccF = 0, accAngle = 0, lastAccAngle = 0;

		int counter = 0;//used when saving data

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
		
		long t = System.currentTimeMillis();
		
		while(run){
//			ref = 0;
			
			//Gyro calc wieh hardcoded HP for h = 0.01 resp 0.02 
			angVel = gyro.getAngularVelocity();	
			gyroAngle += angVel * period;
			gyroF = 0.9048f * lastGyroF + 0.9524f * gyroAngle - 0.9524f * lastGyroAngle;//10ms period
//			gyroF = 0.8182f * lastGyroF + 0.9091f * gyroAngle - 0.9091f * lastGyroAngle;//20ms period
			
			//Acc calc with hardcoded LP for h = 0.01 resp 0.02 
			accAngle = acc.getYTilt();
			accF = 0.99f * lastAccF + 0.004975f * accAngle + 0.004975f * lastAccAngle;//10ms period
//			accF = 0.9802f * lastAccF + 0.009901f * accAngle + 0.009901f * lastAccAngle;//20ms period
			
			yinner = gyroF + accF;

			uinner = (int)(Math.round(limit(inner.calculateOutput(yinner, ref))));//uouter som ref

			//Set power and direction
			power = Math.abs(uinner) + 1;
			left.setPower(power);
			right.setPower(power);

			if(uinner > 0 && lastUinner <= 0){
				left.backward();
				right.backward();
			}else if(uinner < 0 && lastUinner >= 0){
				left.forward();
				right.forward();
			}
			//Update stuff
			lastGyroF = gyroF;
			lastGyroAngle = gyroAngle;
			lastAccF = accF;
			lastAccAngle = accAngle;
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
								System.out.println("oops: " + (duration-period));
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
