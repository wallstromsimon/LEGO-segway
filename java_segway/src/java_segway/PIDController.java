package java_segway;

public class PIDController {
	private double e, u, y, yOld;
	private double P, I, D, ad, bd;
	private double K, Ti, Tr;
	private double b, h;

	public PIDController(double K, double Ti, double Tr, double Td, double N, double b, double h){
		this.K = K;
		this.Ti = Ti;
		this.Tr = Tr;
		this.b = b;
		this.h = h; //Period time in secounds
		P = 0.0;
		I = 0.0;
		D = 0.0;
		ad = Td / (Td + N * h);
		bd = K * ad * N;
	}

	public synchronized double calculateOutput(double y, double yref){
		this.y = y;
		e = yref - y;
		D = ad * D - bd * (y - yOld);
		P = K * (b * yref - y);
		u = P + I + D;
		return u;
	}

	public synchronized void updateState(double uNew){
		if(Ti != 0 && Tr !=0){
			I += (K * h / Ti) * e + (h / Tr) * (uNew - u);
		}else{
			I = 0.0;
		}
		yOld = y;
	}

	//Methods called when saving data to plot
	public synchronized double getP(){
		return P;
	}

	public synchronized double getI(){
		return I;
	}

	public synchronized double getD(){
		return D;
	}

	public synchronized double getE(){
		return e;
	}

}