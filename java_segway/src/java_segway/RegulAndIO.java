package java_segway;

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
	PIDController outer;

	private RefGen refGen;

	double uMin = -100;
	double uMax = 100;
	

	public RegulAndIO(long period, RefGen refGen) {
		this.period = period;
		this.refGen = refGen;
		this.period = period;
		paramInner = new PIDParam(-7.34137741596504, -44.6834487503629, -0.0504302228504053, 28.7822625226348, 1, 1, period);
		paramOuter = new PIDParam(0.00205773648435991, 3.50732954404154e-05, 0.00998848510035523, 5.41093442969671, 1, 1, period);
		inner = new PIDController(paramInner);
		outer = new PIDController(paramOuter);

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

	public void run(){
		long t = System.currentTimeMillis();
		long duration;
		
		double angVel = 0;
		double gyroAng = 0;
		double accAng = 0;
		int accX, accY;
		int power;
		
		double youter, yinner = 0, ref, uouter;
		int uinner;

		double rad2deg = 180/Math.PI;
		double deg2rad = Math.PI/180;

		while(!isInterrupted()){
			youter = (left.getTachoCount()+right.getTachoCount())/2*deg2rad;
			ref = 0; //refGen.getRef();
			uouter = outer.calculateOutput(youter, ref);
			
			angVel = gyro.getAngularVelocity();	
			angVel = Math.abs(angVel) < 1 ? 0 : angVel;
			gyroAng = angVel * (double)period/1000;
			accX = acc.getXAccel();
			accY = -acc.getYAccel();
			accAng = -Math.atan2(accY, accX)*rad2deg+92;
			yinner = (yinner + gyroAng) * 0.92 + accAng * 0.08;
			uinner = (int)(Math.round(limit(inner.calculateOutput(yinner*deg2rad, uouter)*1.5)));
			
			power = Math.abs(uinner);

			if(power<4){
				left.flt();
				right.flt();
			}else if(uinner < 0){
				left.backward();
				right.backward();
			}else{
				left.forward();
				right.forward();
			}
			left.setPower(power);
			right.setPower(power);
			
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
	}
}
