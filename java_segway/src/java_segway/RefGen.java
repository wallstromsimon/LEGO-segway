package java_segway;

import se.lth.cs.realtime.PeriodicThread;

public class RefGen extends PeriodicThread{
	private double ref;
	private int mode;
	private int rad;

	public RefGen(long period, int rad) {
		super(period);
		this.rad = rad;
	}
	
	public synchronized double getRef(){
		return ref;
	}
	
	public synchronized void setRef(double ref){
		this.ref = ref;
	}
	
	public synchronized void setMode(int mode){
		this.mode = mode;
	}
	
	public synchronized int getMode(){
		return mode;
	}
	
	public void perform(){
		if(mode == 0){
			setRef(0);
		}else if(mode == 1){
			if(getRef() == 0){
				setRef(rad);
			}else{
				setRef(0);
			}
		}
	}

}
