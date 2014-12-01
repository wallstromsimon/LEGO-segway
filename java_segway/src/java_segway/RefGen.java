package java_segway;

public class RefGen extends Thread{
	private double ref;
	private int mode;
	private int deg;
	private long period;
	private boolean run = true;

	public RefGen(long period, int deg, int mode) {
		this.period = period;
		this.deg = deg;
		this.mode = mode;
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
	
	public void kill(){
		run = false;
	}

	public void run(){
		long t = System.currentTimeMillis();
		while(run){
			if(mode == 0){
				setRef(0);
			}else if(mode == 1){
				if(getRef() == deg){
					setRef(0);
				}else{
					setRef(deg);
				}
			}
			t = t + period;
			long duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
