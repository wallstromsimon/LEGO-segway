package java_segway;

public class PIDController {
	private PIDParam p;
	private double e, v;

	public PIDController(PIDParam param){
		this.p = (PIDParam)param.clone();
		
	}
	//??
	public void reset(){
		
	}
	
	public double calculateOutput(double y, double yref){
		e = yref - y;
		v = p.P * p.b * e + p.I * p.H * 1000.0 * e + p.D * p.N/(1 + p.N * p.H * 1000.0) * p.c * e;
		return v;
	}
	
	public long getHMillis(){
		return (long)(p.H * 1000.0);
	}
}
