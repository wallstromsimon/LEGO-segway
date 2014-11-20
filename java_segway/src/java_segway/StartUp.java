package java_segway;


public class StartUp{

	public static void main(String[] args) {
		RefGen refGen = new RefGen(10000, 10);
		IO io = new io();
		OpCom opCom = new OpCom(refGen, io);

	}

}
