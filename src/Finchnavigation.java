
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 *
 * @author thoma
 */
public class Finchnavigation {
	private static Object move;
	private int wheelSpeed = 50;
	private static Finch myF = new Finch();
	private static Scanner in = new Scanner(System.in);
	private static ArrayList<Character> instructions = new ArrayList<Character>();
	private static ArrayList<Float> durationsList = new ArrayList<Float>();
	private static ArrayList<Integer> speedList = new ArrayList<Integer>();

	Finchnavigation() {
		runProgram();
	}

	public static void main(String args[]) {
		while (true) {
			runProgram();
		}
	}

	public static void runProgram() {
		while (true) {
			System.out.println("What would you like to do (f/b/r/l/t/q): ");
			char direction = in.next().charAt(0);// in.nextLine().charAt(0);
			System.out.println("Duration to move for ( 1 - 6): "); // The time range for finch moving
			float timeDuration = in.nextFloat();
			if(timeDuration > 6) timeDuration = 6;
			else if(timeDuration < 1) timeDuration = 1;
			System.out.println("duration set to " + timeDuration);
			System.out.println("Speed of the finch (0 - 225): "); // The Speed range for the wheels to move
			int speedFinch = in.nextInt();
			if(speedFinch > 225) speedFinch = 225;
			else if(speedFinch < 0) speedFinch = 0;
			System.out.println("speed set to " + speedFinch);
			
			selectFunction(direction, timeDuration, speedFinch);
		}
	}

	public static void selectFunction(char direction, float timeDuration, int speedFinch) {
		switch (direction) {
		case 'f':
		case 'F':
			forward(timeDuration, speedFinch);
			System.out.println("selected forward");
			break;
		case 'b':
		case 'B':
			backward(timeDuration, speedFinch);
			System.out.println("selected backward");
			break;
		case 'r':
		case 'R':
			turnRight(timeDuration);
			System.out.println("selected right");
			break;
		case 'l':
		case 'L':
			turnLeft(timeDuration);
			System.out.println("left");
			break;
		case 't':
			if (instructions.isEmpty()) {
				System.out.println("unavailable");
				return;
			}
			reTracing();
		case 'q':
		case 'Q':
			quit();
			break;
		default:
			System.out.println("Input something else");
			return;
		}
		
		instructions.add(direction);
		durationsList.add(timeDuration);
		speedList.add(speedFinch);
	}

	public static boolean forward(float timeDuration, int finchSpeed) {
		// myF.setWheelVelocities(wheelSpeed,wheelSpeed);
		// Start time of the method being called
		long start = System.nanoTime();
		// initializing elapsedTime.
		double elapsedTimeInSeconds = 0;
		while (elapsedTimeInSeconds < timeDuration) {
			myF.setWheelVelocities(finchSpeed, finchSpeed);
			// getting the current time to compare with start time.
			long end = System.nanoTime();
			elapsedTimeInSeconds = end - start;
			// This converts the elapsed time int seconds
			elapsedTimeInSeconds = (double) elapsedTimeInSeconds / 1_000_000_000;
		}
		// after while loop has broken the wheels will stop.
		myF.stopWheels();
		return true;
	}

	public static boolean backward(float timeDuration, int finchSpeed) {

		// myF.setWheelVelocities(-wheelSpeed, -wheelSpeed);
		long start = System.nanoTime();
		// initializing elapsedTime.
		double elapsedTimeInSeconds = 0;
		while (elapsedTimeInSeconds < timeDuration) {
			myF.setWheelVelocities(-finchSpeed, -finchSpeed); // corresponding to going backward
			// getting the current time to compare with start time.
			long end = System.nanoTime();
			elapsedTimeInSeconds = end - start;
			// This converts the elapsed time int seconds
			elapsedTimeInSeconds = (double) elapsedTimeInSeconds / 1_000_000_000;
		}
		// after while loop has broken the wheels will stop.
		myF.stopWheels();
		return true;
	}

	public static boolean turnRight(float timeDuration) {

		long start = System.nanoTime();
		// initializing elapsedTime.
		double elapsedTimeInSeconds = 0;
		while (elapsedTimeInSeconds < timeDuration) {
			myF.setWheelVelocities(90, 0); // Left wheel turns, right wheel stay puts
			// getting the current time to compare with start time.
			long end = System.nanoTime();
			elapsedTimeInSeconds = end - start;
			// This converts the elapsed time int seconds
			elapsedTimeInSeconds = (double) elapsedTimeInSeconds / 1_000_000_000;
		}
		// after while loop has broken the wheels will stop.
		myF.stopWheels();
		return true;
	}

	public static boolean turnLeft(float timeDuration) {

		long start = System.nanoTime();
		// initializing elapsedTime.
		double elapsedTimeInSeconds = 0;
		while (elapsedTimeInSeconds < timeDuration) {
			myF.setWheelVelocities(0, 90); // right wheel turns, left wheel stay puts
			// getting the current time to compare with start time.
			long end = System.nanoTime();
			elapsedTimeInSeconds = end - start;
			// This converts the elapsed time int seconds
			elapsedTimeInSeconds = (double) elapsedTimeInSeconds / 1_000_000_000;
		}
		// after while loop has broken the wheels will stop.
		myF.stopWheels();
		return true;

	}

	public static void reTracing() {
		System.out.println("how many steps do you want to retrace?");
		int steps = 0;
		try {
			steps = Integer.parseInt(in.next());
		} catch (Exception e) {
			System.out.println("put in an integer you fool");
		}
		int lSize = instructions.size();
		for (int i = lSize - 2; i >= lSize - steps - 1; i++) {
			selectFunction(instructions.get(i), durationsList.get(i), speedList.get(i));
		}
	}

	public void Writetotextfile() {
		String lSep = System.getProperty("line.separator");
		String output = "";
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		output += df.format(date) + lSep;
		System.out.println(output);
		
		for (int i = 0; i < instructions.size(); i++) {
			output += instructions.get(i) + " ";
			output += durationsList.get(i) + " ";
			output += speedList.get(i) + lSep;
		}
		try {
			FileWriter writer = new FileWriter("C:\\tempStats.txt", true);
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			System.out.println("error writing stats");
			e.printStackTrace();
		}
	}

	public static void quit() {
		myF.quit();
		System.exit(0);
	}

}
