package java_segway;

public class PIDControllerAlternative {

	private PIDParam p;
	private double e, v, yOld,y;

	public PIDControllerAlternative(PIDParam param){
		this.p = param;
		yOld = 0;
		y = 0;
		
		p.ad = p.Td / (p.Td + p.N * p.H);
		p.bd = p.P * p.ad * p.N;
	}


	public synchronized double calculateOutput(double y, double yref){
		this.y = y;
		e = yref - y;
		p.D = p.ad * p.D - p.bd * (y - yOld);
		v = p.P * (p.b * yref - y) + p.I + p.D;
		return v;
	}

	public synchronized void updateState(double u){
		p.I = p.I + (p.P * p.H / p.Ti) * e + (p.H / p.Tr) * (u - v);
		yOld = y;
	}
}
