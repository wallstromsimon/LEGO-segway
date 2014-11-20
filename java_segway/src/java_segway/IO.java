package java_segway;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class IO{
	NXTRegulatedMotor left = new NXTRegulatedMotor(MotorPort.C);
	NXTRegulatedMotor right = new NXTRegulatedMotor(MotorPort.B);
	GyroSensor gyro = new GyroSensor(SensorPort.S2);
	
	public IO() {
		gyro.recalibrateOffset();
	}

	public synchronized double getPos(){
		return left.getTachoCount();
	}
	
	public synchronized double getAngle(){
		return gyro.getAngularVelocity();
	}
	
	public synchronized void setMotor(double diff){
		int pos = (int)Math.round(diff);
		left.rotate(pos, true);
		right.rotate(pos, true);
	}
	
	public synchronized void reset() {
		left.resetTachoCount();
		right.resetTachoCount();
	}

}
