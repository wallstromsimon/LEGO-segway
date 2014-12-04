package java_segway;

public class PIDParam implements Cloneable{
	public double K;
	public double Ti;
	public double Tr;
	public double Td;
	public double N;
	public double b;
	public double H;
	
		
	public PIDParam(double K, double Ti, double Td, double N, double b, double Tr, double H){
		this.K = K;
		this.Ti = Ti;
		this.Td = Td;
		this.N = N; //5-10
		this.b = b;
		this.H = H; //0.02-0.1
		this.Tr = Tr;

	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
