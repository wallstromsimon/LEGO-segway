package java_segway;

import se.lth.cs.realtime.PeriodicThread;

public class Regul extends PeriodicThread{

	double h;
	PIDParam paramInner;
	PIDParam paramOuter;
	PIDController inner;
	PIDController outer;

	private RefGen refGen;
	private IO io;

	double uMin = -0.5;
	double uMax = 0.5;
	double youter, yinner, ref, uouter, uinner;
	
	public Regul(long period, RefGen refGen, IO io) {
		super(period);
		this.refGen = refGen;
		this.io = io;
		h = period/1000;
		paramInner = new PIDParam(-21.842902173143, -145.039919974658, -0.0179008201129659, 132.805666822822, 1, 1, h);
		paramOuter = new PIDParam(0.000500351283690184, 5.25273715419508e-06, 0.00554912489042775, 2.9025731973979, 1, 1, h);
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

	public void perform(){
		youter = io.getPos();
		ref = refGen.getRef();
		yinner = io.getAngle();

		synchronized (outer) {
			synchronized (inner) {
				uouter = limit(outer.calculateOutput(youter, ref));
				uinner = limit(inner.calculateOutput(yinner, uouter));
				io.setMotor(uinner);
			}
		}

	}
}