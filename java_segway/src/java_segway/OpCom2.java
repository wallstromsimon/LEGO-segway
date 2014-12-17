package java_segway;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class OpCom2 extends Thread{
	private USBConnection conn;
//	private DataOutputStream dOut;
	private BufferedReader dIn;
	private FeedbackController c;
	
	public OpCom2(FeedbackController c){
		this.c = c;
	}

	public void run() {

		LCD.drawString("waiting", 0, 0);
		conn = USB.waitForConnection();
//		dOut = conn.openDataOutputStream();
		dIn = new BufferedReader(new InputStreamReader(conn.openInputStream()));
		
		float L1, L2, L3, L4;
		String s = null;
		int i;
		LCD.drawString("live     ", 0, 0);
		while(!Button.ESCAPE.isDown()){
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
					LCD.drawString(L1 + "   \n" + L2 + "   \n" + L3 + "   \n" + L4 + "   ",0,0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
