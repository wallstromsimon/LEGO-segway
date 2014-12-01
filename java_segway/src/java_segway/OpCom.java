package java_segway;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class OpCom{

	public OpCom(final RefGen refGen, final RegulAndIO reg) {
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("EXIT");
				reg.interrupt();
				refGen.interrupt();
			}

			@Override
			public void buttonReleased(Button b) {
				
			}
		});
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("Reset");
//				reset();
			}

			@Override
			public void buttonReleased(Button b) {
				
			}
		});
		
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("New mode, yay!");
//				setMode();
			}
			
			@Override
			public void buttonReleased(Button b) {
			}
		});
	}
	
//	public void reset(){
//		io.reset();
//		refGen.setRef(0);
//		refGen.setMode(0);
//	}
//	
//	public void setMode(){
//		if(refGen.getMode() == 0){
//			refGen.setMode(1);
//		}else if(refGen.getMode() == 1){
//			refGen.setMode(0);
//		}
//	}
		
}
