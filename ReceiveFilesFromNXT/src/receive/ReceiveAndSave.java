package receive;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;


public class ReceiveAndSave {	
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

		//		DataInputStream inDat = new DataInputStream(conn.getInputStream());
		//		DataOutputStream outDat = new DataOutputStream(conn.getOutputStream());
		BufferedReader dIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();

		String s;
		boolean run = true;
		while(run){
			try {
				s = dIn.readLine();
				if(!s.equals("exit")){
					sb.append(s + "\n");
				}else{
					run = false;
				}
			} catch (IOException ioe) {
				System.err.println("IO Exception reading");
			}            
		}

		try {
			//			inDat.close();
			//			outDat.close();
			dIn.close();
			System.out.println("Closed data streams");
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

		System.out.println("Saving file...");
		
		String filename = "feedbackData.txt";
//		String filename = "angle.txt";
		File f = new File("C:\\Users\\simon\\Documents\\GitHub\\LEGO-segway\\matlab\\" + filename);
//		File f = new File("C:\\Users\\Jonathan\\Documents\\GitHub\\LEGO-segway\\matlab\\" + filename);

		
		try {
			FileOutputStream fos = new  FileOutputStream(f);
			fos.write(sb.toString().getBytes());
			fos.close();
			System.out.println("Saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}