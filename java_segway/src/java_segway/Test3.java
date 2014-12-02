package java_segway;

public class Test3 {

	public static void main(String[] args) {
		long period = 1000;
		
		BTLog bt = new BTLog(period);
		
		bt.start();
		System.out.println("död åt Tengil");
	}

}
