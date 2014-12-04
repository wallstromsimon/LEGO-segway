package java_segway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;

public class RegulAndIO extends Thread{
	NXTMotor left = new NXTMotor(MotorPort.C);
	NXTMotor right = new NXTMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);

	long period;
	PIDParam paramInner;
	PIDParam paramOuter;
	PIDController inner;
//	PIDController outer;

	private RefGen refGen;

	double uMin = -100;
	double uMax = 100;
	
	private boolean run = true;
	
	private StringBuffer Pb = new StringBuffer();
	private StringBuffer Ib = new StringBuffer();
	private StringBuffer Db = new StringBuffer();
	private StringBuffer eb = new StringBuffer();
	private StringBuffer yb = new StringBuffer();
	private StringBuffer ub = new StringBuffer();

	

	public RegulAndIO(long period, RefGen refGen) {
		this.period = period;
		this.refGen = refGen;
		this.period = period;
		//small wheels
//		paramInner = new PIDParam(-7.34137741596504, -44.6834487503629, -0.0504302228504053, 28.7822625226348, 1, 1, period);
//		paramOuter = new PIDParam(0.00205773648435991, 3.50732954404154e-05, 0.00998848510035523, 5.41093442969671, 1, 1, period);
		//large wheeeeeels
		paramInner = new PIDParam(15, 0.1, 0.05, 10, 1, 1, period);
//		paramOuter = new PIDParam(0.00206187887900374, 2.09062324049099e-05, 0.0191551192258251, 2.67761009467029, 1, 1, period);
		inner = new PIDController(paramInner);
//		outer = new PIDController(paramOuter);

		System.out.println("Calibrating...");
		left.stop();
		right.stop();
		left.resetTachoCount();
		right.resetTachoCount();

		gyro.recalibrateOffset();
		System.out.println("Calibrated, hold robot and press any key to balance");
		Button.waitForAnyPress();
		System.out.println("\"Balancing\"");
	}
	
	private double limit(double u) {
		if (u < uMin) {
			u = uMin;
		} else if (u > uMax) {
			u = uMax;
		} 
		return u;
	}
	
	public synchronized void kill(){
		run = false;
	}
	
	private void saveToFile(){
		FileOutputStream fos;
		try {
			fos = new  FileOutputStream(new File("p.txt"));
			fos.write(Pb.toString().getBytes());
			fos.close();
			
			fos = new  FileOutputStream(new File("i.txt"));
			fos.write(Ib.toString().getBytes());
			fos.close();
			
			fos = new  FileOutputStream(new File("d.txt"));
			fos.write(Db.toString().getBytes());
			fos.close();
			
			fos = new  FileOutputStream(new File("e.txt"));
			fos.write(eb.toString().getBytes());
			fos.close();
			
			fos = new  FileOutputStream(new File("y.txt"));
			fos.write(yb.toString().getBytes());
			fos.close();
			
			fos = new  FileOutputStream(new File("u.txt"));
			fos.write(ub.toString().getBytes());
			fos.close();
			
			System.out.println("Saved");
			Thread.sleep(2000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		long t = System.currentTimeMillis();
		long duration;
		
		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		int[] accV = new int[3];
		int power;
		
		double youter, yinner = 0, ref, uouter;
		int uinner;

		double rad2deg = 180/Math.PI;
		double deg2rad = Math.PI/180;
		long counter = 0;
		
		while(run){
//			youter = (left.getTachoCount()+right.getTachoCount())/2*deg2rad;
			ref = 0;//refGen.getRef();
//			uouter = outer.calculateOutput(youter, ref);
			
			//1ms read gyro
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * (double)period/1000;
//			if(counter%4 == 0){
//			AccelMindSensor: 9ms getAll, 12ms getX+getY
//			AccelHTSensor: 9ms getAll, 15ms getX+getY
				acc.getAllAccel(accV, 0);
				accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;
//			}
			
			yinner = (yinner + gyroAng) * 0.92 + accAng * 0.08;
			
			uinner = (int)(Math.round(limit(inner.calculateOutput(yinner*deg2rad, ref))));//uouter
//			uinner = (int)(Math.round(limit(inner.calculateOutput(yinner*deg2rad, ref))));//uouter
			
			
			power = Math.abs(uinner);
			left.setPower(power);
			right.setPower(power);
			
//			if(power < 0){
//				left.flt();
//				right.flt();
//			}else 
				if(uinner < 0){
				left.backward();
				right.backward();
			}else{
				left.forward();
				right.forward();
			}
			
			inner.updateState(uinner);
//			outer.updateStateMatlab();
			
//			tar tid, bara för att spara data till fil
			if(counter%10==0 && counter <= 1000){
				Pb.append(inner.getP() + "\n");
				Ib.append(inner.getI() + "\n");
				Db.append(inner.getD() + "\n");
				eb.append(inner.getE() + "\n");
				yb.append(yinner + "\n");
				ub.append(uinner + "\n");
			}
			counter++;
			
			t = t + period;
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
		left.stop();
		right.stop();
		saveToFile();
	}
}
