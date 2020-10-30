package client;

import java.net.*;
import java.util.Formatter;
import java.util.Scanner;
import java.io.*;

// Name       : Mohamed Daniel Bin Mohamed Izham
// QUID       : 201802738
// Course     : CMPS 405 - Operating Systems
// Assessment : Homework 2
// Instructor : Mohammad Saleh Mustafa Saleh

public class Client {
	Socket server;
	int port;
	Formatter toNet = null;
	Scanner fromNet = null;
	Scanner fromUser = new Scanner(System.in);
	int MAX_ATTEMPTS = 3;

	public Client() {
		try {
			String response = login();
			int attemptsRemaining = MAX_ATTEMPTS - 1;
			while (response.equals("invalid") && attemptsRemaining > 0) {
				System.out.printf("Invalid login. Please re-enter your credentials. Attempts remaining: %d%n%n",
						attemptsRemaining--);

				response = login();
			}

			// if login is successful, vote at local host port 4001
			if (response.equals("valid")) {
				server = new Socket("localhost", 4001);
				toNet = new Formatter(server.getOutputStream());

				System.out.print("Enter a number that you want to vote for [0-9]: ");
				String vote = fromUser.nextLine();
				toNet.format("%s\n", vote);
				toNet.flush();
				System.out.println("Thank you for participating in the voting process. Goodbye...");
			} else {
				System.out.println("You have reached the maximum number of attempts. "
						+ "Please contact your local administration to resolve the issue.");
				System.out.println("Thank you for using our service.");
			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	private String login() { // login at server at local host port 4000
		// get from the user the inputs
		System.out.print("Enter username: ");
		String user = fromUser.nextLine();
		System.out.print("Enter password: ");
		String pass = fromUser.nextLine();

		// define a new instance of the login server and its i/o streams
		try {
			server = new Socket("localhost", 4000);
			toNet = new Formatter(server.getOutputStream());
			fromNet = new Scanner(server.getInputStream());
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

		// write to the server
		toNet.format("%s\n", user);
		toNet.flush();
		toNet.format("%s\n", pass);
		toNet.flush();

		// return the server's response
		return fromNet.nextLine();
	}

	public static void main(String[] args) {
		new Client();
	}
}
