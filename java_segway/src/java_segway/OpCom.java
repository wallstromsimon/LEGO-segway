package java_segway;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class OpCom{

	private RefGen refGen;
	private IO io;

	public OpCom(RefGen refGen, IO io) {
		this.refGen = refGen;
		this.io = io;
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				reset();
			}

			@Override
			public void buttonReleased(Button b) {
				
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				setMode();
			}
			
			@Override
			public void buttonReleased(Button b) {
			}
		});
	}
	
	public void reset(){
		io.reset();
		refGen.setRef(0);
		refGen.setMode(0);
	}
	
	public void setMode(){
		if(refGen.getMode() == 0){
			refGen.setMode(1);
		}else if(refGen.getMode() == 1){
			refGen.setMode(0);
		}
	}
		
}
