package java_segway;

import se.lth.cs.realtime.PeriodicThread;

public class Regul extends PeriodicThread{

	double h = 0.01;
	PIDParam paramInner = new PIDParam(-21.842902173143, -145.039919974658, -0.0179008201129659, 132.805666822822, 1, 1, h);
	PIDParam paramOuter = new PIDParam(0.000500351283690184, 5.25273715419508e-06, 0.00554912489042775, 2.9025731973979, 1, 1, h);
	PIDController inner = new PIDController(paramInner);
	PIDController outer = new PIDController(paramOuter);

	private RefGen refGen;
	private IO io;

	double uMin = -0.5;
	double uMax = 0.5;
	double youter, yinner, ref, uouter, uinner;
	
	public Regul(long period, RefGen refGen, IO io) {
		super(period);
		this.refGen = refGen;
		this.io = io;
	}

	private double limit(double u, double umin, double umax) {
		if (u < umin) {
			u = umin;
		} else if (u > umax) {
			u = umax;
		} 
		return u;
	}

	public void perform(){
		youter = io.getPos();
		ref = refGen.getRef();
		yinner = io.getAngle();

		synchronized (outer) {
			synchronized (inner) {
				uouter = limit(outer.calculateOutput(youter, ref), uMin, uMax);
				uinner = limit(inner.calculateOutput(yinner, uouter), uMin, uMax);
				io.setMotor(uinner);
			}
		}

	}
}