package java_segway;

public class PIDController {
	private PIDParam p;
	private double e, ei, ed, u;
	
	private double dg, filt, ig;

	public PIDController(PIDParam param){
		this.p = (PIDParam)param.clone();
		filt = 0;
		ig = 0;
	}
	//??
	public void reset(){
		
	}
	
	public double calculateOutputSimulink(double y, double yref){
		e = yref - y;
		u = p.P * p.b * e + p.I * ((double)p.H / 1000.0) * e + p.D * p.N/(1 + p.N * ((double)p.H / 1000.0)) * p.c * e;
		return u;
	}
	
	public double calculateOutputMatlab(double y, double yref){
		e = p.b*yref - y;
		ei = yref - y;
		ed = p.c*yref - y;
		
		dg = (ed*p.D - filt)*p.N;
		
		u = p.P * e + ig + dg;
		return u;
	}
	
	public void updateStateMatlab(){
		ig += ei * p.I * (double)p.H/1000;
		filt += dg * (double)p.H/1000;
	}
	
	public long getHMillis(){
		return (long)(p.H * 1000.0);
	}
}

///* Update for DiscreteIntegrator: '<S1>/Filter' */
//Discrete_DW.Filter_DSTATE += Discrete_P.Filter_gainval * rtb_FilterCoefficient;
