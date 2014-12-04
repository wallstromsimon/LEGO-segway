package java_segway;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class OpCom{

	public OpCom(final RefGen refGen, final RegulAndIO reg) {
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("EXIT");
				reg.kill();
			}

			@Override
			public void buttonReleased(Button b) {
				
			}
		});
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("Left...");
			}

			@Override
			public void buttonReleased(Button b) {
				
			}
		});
		
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("Right button pushed...");
			}
			
			@Override
			public void buttonReleased(Button b) {
			}
		});
	}
	
		
}
