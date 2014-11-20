package java_segway;

import se.lth.cs.realtime.PeriodicThread;

public class Regul extends PeriodicThread{
	
	double h = 0.01;
	PIDParam paramInner = new PIDParam(-21.842902173143, -145.039919974658, -0.0179008201129659, 132.805666822822, 1, 1, h);
	PIDParam paramOuter = new PIDParam(0.000500351283690184, 5.25273715419508e-06, 0.00554912489042775, 2.9025731973979, 1, 1, h);

	public Regul(long arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void setRefGen(){
		
	}
	
	public void setOpCom(){
		
	}
	
	public void setIO(){
		
	}
	

}
