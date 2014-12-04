package java_segway;

public class PIDController {
	private double e, u, y, yOld;
	private double P, I, D, ad, bd;
	private double K, Ti, Tr;
	private double b, H;

	public PIDController(double K, double Ti, double Tr, double Td, double N, double b, double H){
		this.K = K;
		this.Ti = Ti;
		this.Tr = Tr;
		this.b = b;
		this.H = H;
		
		ad = Td / (Td + N * H);
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
		I = I + (K * H / Ti) * e + (H / Tr) * (uNew - u);
		yOld = y;
	}

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