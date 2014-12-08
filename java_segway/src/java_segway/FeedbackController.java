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
	private AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);
	
	// The PID control parameters
	private double Kp = 1.25;//1.2;
	private double Ki = 0.25;//0.25;
	private double Kd = 0.10;//0.1;
	// Testing error contributions. -1.0000  -23.6256   -1.3765   -3.5405
	private double K_psi = -1.0000;
	private double K_phi = -23.6256;
	private double K_psidot = -1.3765;
	private double K_phidot = -3.5405;

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

	public void run()
	{
		double int_error = 0.0;
		double prev_error = 0.0;
		double Psi = 0; //Angle
		double PsiDot; //AngleVel
		double Phi;//wheel
		double PhiDot;//wheel rot speed
		
		double error;
		double deriv_error;
		int power, lastPower = 0;
		double direction;
		
		double rad2deg = 180/Math.PI;
		double accAng, gyroAng;
		int[] accV = new int[3];


		while (!Button.ESCAPE.isDown()){
			long t = System.currentTimeMillis();
			long duration;
			
			PsiDot = gyro.getAngularVelocity();	
			PsiDot = Math.abs(PsiDot) < 1 ? 0 : PsiDot;
			gyroAng = (PsiDot * (double)period/1000);
			
			//AccelMindSensor: 9ms getAll, 12ms getX+getY
			//AccelHTSensor: 9ms getAll, 15ms getX+getY
			acc.getAllAccel(accV, 0);
			accAng = -Math.atan2(accV[0], accV[1])*rad2deg + 90;
			
			Psi = (Psi + gyroAng) * 0.92 + accAng * 0.08;
			
			
			Phi = (left.getTachoCount()+right.getTachoCount())/2;
			PhiDot = (left.getRotationSpeed()+right.getRotationSpeed())/2;
			
			// Proportional Error
			error = Psi * K_psi + Phi * K_phi + PsiDot * K_psidot + PhiDot * K_phidot;
			// Integral Error
			int_error += error;
			// Derivative Error
			deriv_error = error - prev_error;
			prev_error = error;
			
			
			// Power sent to the motors
			direction = error * Kp + deriv_error * Kd +	int_error * Ki;
			
			//Set power and direction
			System.out.println("direction: " + direction);
			power = (int)Math.round(Math.abs(direction));
			System.out.println("Power: " + power);
			left.setSpeed(power);
			right.setSpeed(power);

			if(power > 0 && lastPower <= 0){
				left.backward();
				right.backward();
			}else if(power < 0 && lastPower >= 0){
				left.forward();
				right.forward();
			}
			lastPower = power;
			
			
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
}
