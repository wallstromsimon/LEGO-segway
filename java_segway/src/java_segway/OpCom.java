package java_segway;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class OpCom extends Thread{
	private USBConnection conn;
	private DataOutputStream dOut;
	private BufferedReader dIn;
	private Controller c;
	private boolean run;

	public OpCom(final RefGen refGen, final Controller reg) {
		this.c = reg;
		run = true;
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.out.println("EXIT");
				run = false;
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
	
	public void run() {
		LCD.drawString("waiting", 0, 0);
		conn = USB.waitForConnection();
		dOut = conn.openDataOutputStream();
		dIn = new BufferedReader(new InputStreamReader(conn.openInputStream()));
		
		float L1, L2, L3, L4;
		String s = null;
		int i;
		LCD.drawString("live     ", 0, 0);
		while(run){
			try {
				s = dIn.readLine();
				if(s == null){
					
				}else{
					i = s.indexOf(' ');
					L1 = Float.parseFloat(s.substring(0, i));
					s = s.substring(i).trim();
					
					i = s.indexOf(' ');
					L2 = Float.parseFloat(s.substring(0, i));
					s = s.substring(i).trim();
					
					i = s.indexOf(' ');
					L3 = Float.parseFloat(s.substring(0, i));
					s = s.substring(i).trim();
					
					L4 = Float.parseFloat(s);
					c.updateL(L1, L2, L3, L4);
				}
				sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
//			dOut.writeBytes("exit\n");
//			dOut.flush();
			dOut.close();
			dIn.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
