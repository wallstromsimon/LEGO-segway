package java_segway;

public class PIDParam implements Cloneable{
	public double P;
	public double I;
	public double D;
	public double N;
	public double b;
	public double c;
	public double H;
	public double ad;
	public double bd;
	
	public PIDParam(double P, double I, double D, double N, double b, double c, double H){
		this.P = P;
		this.I = I;
		this.D = D;
		this.N = N; //5-10
		this.b = b;
		this.c = c;
		this.H = H; //0.02-0.1
		ad = 0;
		bd = 0;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
