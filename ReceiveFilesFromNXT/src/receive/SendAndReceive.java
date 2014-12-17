package receive;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;


public class SendAndReceive {	
	public static void main(String[] args) {
		NXTConnector conn = new NXTConnector();

		conn.addLogListener(new NXTCommLogListener(){
			public void logEvent(String message) {
				System.out.println("USBSend Log.listener: "+message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("USBSend Log.listener - stack trace: ");
				throwable.printStackTrace();
			}
		});

		if (!conn.connectTo("usb://")){
			System.err.println("No NXT found using USB");
			System.exit(1);
		}

		DataOutputStream dOut = new DataOutputStream(conn.getOutputStream());
		BufferedReader dIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();

		String s;
		String s2 = null;
		boolean run = true;
		Scanner scan = new Scanner(System.in);
		while(run){
			try {
				s2 = dIn.readLine();
				if(s2.equals("exit")){
					run = false;
					break;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			if(scan.hasNextLine()){
				s = scan.nextLine();
				try {
					dOut.writeBytes(s + "\n");
					dOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}   
			}
		}

		try {
			dOut.close();
			dIn.close();
			System.out.println("Closed data streams");
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

//		System.out.println("Saving file...");
//
//		//		String filename = "feedbackData.txt";
//		String filename = "angle.txt";
//		File f = new File("C:\\Users\\simon\\Documents\\GitHub\\LEGO-segway\\matlab\\" + filename);
//		//		File f = new File("C:\\Users\\Jonathan\\Documents\\GitHub\\LEGO-segway\\matlab\\" + filename);
//
//
//		try {
//			FileOutputStream fos = new  FileOutputStream(f);
//			fos.write(sb.toString().getBytes());
//			fos.close();
//			System.out.println("Saved");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}