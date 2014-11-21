package java_segway;

public class IOMonitor {
	private double pos;
	private double ang;
	private double motor;
	
	public IOMonitor(){
		pos = 0;
		ang = 0;
		motor = 0;
	}

	public synchronized double getPos() {
		return pos;
	}
	
	public synchronized void setPos(double pos) {
		this.pos = pos;
	}

	public synchronized double getAngle() {
		return ang;
	}
	
	public synchronized void setAngle(double ang) {
		this.ang += ang;
	}

	public synchronized double getMotor() {
		return motor;
	}
	
	public synchronized void setMotor(double diff) {
		this.motor = diff;
	}

}
