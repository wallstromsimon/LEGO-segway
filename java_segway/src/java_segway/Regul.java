package java_segway;


public class Regul extends Thread{

	long period;
	PIDParam paramInner;
	PIDParam paramOuter;
	PIDController inner;
	PIDController outer;

	private RefGen refGen;
	private IOMonitor ioM;

	double uMin = -100000;
	double uMax = 100000;
	double youter, yinner, ref, uouter, uinner;

	public Regul(long period, RefGen refGen, IOMonitor ioM) {
		this.period = period;
		this.refGen = refGen;
		this.ioM = ioM;
		period = period;
		paramInner = new PIDParam(-21.842902173143, -145.039919974658, -0.0179008201129659, 132.805666822822, 1, 1, period);
		paramOuter = new PIDParam(0.000500351283690184, 5.25273715419508e-06, 0.00554912489042775, 2.9025731973979, 1, 1, period);
		inner = new PIDController(paramInner);
		outer = new PIDController(paramOuter);

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
		while(true){
			youter = ioM.getPos();
			ref = refGen.getRef();
			yinner = ioM.getAngle();

			synchronized (outer) {
				synchronized (inner) {
					uouter = limit(outer.calculateOutput(youter, ref));
					uinner = limit(inner.calculateOutput(yinner, uouter));
					ioM.setMotor(uinner);
				}
			}
			t = t + period;
			long duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}