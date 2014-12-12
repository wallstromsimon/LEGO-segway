package java_segway;

public class PIDController {
	private float e, u, y, yOld;
	private float P, I, D, ad, bd;
	private float K, Ti, Tr;
	private float b, h;

	public PIDController(float K, float Ti, float Tr, float Td, float N, float b, float h){
		this.K = K;
		this.Ti = Ti;
		this.Tr = Tr;
		this.b = b;
		this.h = h; //Period time in secounds
		P = 0.0f;
		I = 0.0f;
		D = 0.0f;
		ad = Td / (Td + N * h);
		bd = K * ad * N;
	}

	public synchronized float calculateOutput(float y, float yref){
		this.y = y;
		e = yref - y;
		D = ad * D - bd * (y - yOld);
		P = K * (b * yref - y);
		u = P + I + D;
		return u;
	}

	public synchronized void updateState(float uNew){
		if(Ti != 0 && Tr !=0){ //If Ti == 0 -> I =0
			I += (K * h / Ti) * e + (h / Tr) * (uNew - u);
		}else{
			I = 0.0f;
		}
		yOld = y;
	}

	//Methods called when saving data to plot
	public synchronized float getP(){
		return P;
	}

	public synchronized float getI(){
		return I;
	}

	public synchronized float getD(){
		return D;
	}

	public synchronized float getE(){
		return e;
	}

}