package java_segway;


public class RefGen extends Thread{
	private double ref;
	private int mode;
	private int deg;
	long period;

	public RefGen(long period, int deg) {
		this.period = period;
		this.deg = deg;
		mode = 0;
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

	public void run(){
		long t = System.currentTimeMillis();
		while(true){
			if(mode == 0){
				setRef(0);
			}else if(mode == 1){
				if(getRef() == 0){
					setRef(deg * 360);
				}else{
					setRef(0);
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
