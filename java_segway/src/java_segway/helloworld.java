package java_segway;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class helloworld {
	public static void main(String[] args){
		System.out.println("Hello robot");
		Button.waitForAnyPress();
		System.out.println("Hello human");
		Button.waitForAnyPress();
	}
}