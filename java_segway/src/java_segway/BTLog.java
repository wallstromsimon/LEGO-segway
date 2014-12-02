package java_segway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.comm.*;

public class BTLog extends Thread{
	
private long period;

private int pos;
private int angle;
private int tacho;

public BTLog(long period){
	this.period = period;

}

public void run(){
//	File file = new File("C://Desktop/file");
//	try{
//	FileOutputStream fos = new FileOutputStream(file);
//	}catch(FileNotFoundException e){		
//	}
//	DataOutputStream dos2 = new DataOutputStream(fos);
//	DataInputStream dis = connection.openDataInputStream();
	
	System.out.println("Trying to connect..");
//	RemoteDevice rd = Bluetooth.getKnownDevice("NXT");
//	BTConnection btc = Bluetooth.connect(rd);
	BTConnection connection = Bluetooth.waitForConnection();
	System.out.println("connected");
	DataOutputStream dos = connection.openDataOutputStream();
	
	System.out.println("after conn estbl.");
	
	for(int i = 0; i < 10; i++){
		System.out.println("Logging started");
		pos = 1;
		try {
			dos.writeInt(pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dos.flush();
//			dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		angle = 2;
		try {
			dos.writeInt(angle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tacho = 3;
		try {
			dos.writeInt(tacho);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(period);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	try {
		dos.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	connection.close();
	System.out.println("Logging stopped");
}


	
}
