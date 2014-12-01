package java_segway;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.addon.GyroSensor;

public class SensordataToFile {
	static final String fileName = "data2.txt";
	static String text;
	static FileOutputStream fos;
	static File f;
	static StringBuffer sb;

	public static void main(String[] args) throws IOException, InterruptedException{
		GyroSensor gyro = new GyroSensor(SensorPort.S2);
		AccelMindSensor acc = new AccelMindSensor(SensorPort.S3);
		sb = new StringBuffer();
		f = new File(fileName);
		fos = new  FileOutputStream(f);
		gyro.recalibrateOffset();

		double gyroData;
		int[] accData = new int[3];

		System.out.println("Reading data...");
		for(int i = 0; i < 200; i++){
			gyroData = gyro.getAngularVelocity();
			acc.getAllAccel(accData, 0);

			sb.append(gyroData + "   " + (accData[0]) + "   " + (-accData[1]) + "\n");
			Thread.sleep(50);
		}
		System.out.println("Saving file...");
		text = sb.toString();
		
		fos.write(text.getBytes());
		fos.close();
		System.out.println("Saved");
		Thread.sleep(2000);
	}
}
