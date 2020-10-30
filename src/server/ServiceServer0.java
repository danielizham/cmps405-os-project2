package server;

import java.net.*;
import java.util.Formatter;
import java.util.Scanner;
import java.io.*;

// Name       : Mohamed Daniel Bin Mohamed Izham
// QUID       : 201802738
// Course     : CMPS 405 - Operating Systems
// Assessment : Homework 2
// Instructor : Mohammad Saleh Mustafa Saleh

public class ServiceServer0 extends Thread {// login multithreaded service
	Socket client;
	Scanner fromNet = null;
	Formatter toNet = null;
	private String login[][] = { { "user1", "pass1" }, { "user2", "pass2" }, { "user3", "pass3" }, { "user4", "pass4" },
			{ "user5", "pass5" }, { "user6", "pass6" }, { "user7", "pass7" }, { "user8", "pass8" },
			{ "user9", "pass9" }, };

	public ServiceServer0(Socket client) {
		this.client = client;
	}

	public void run() {
		System.out.println("Login Service: Serving client ...");
		try {
			fromNet = new Scanner(client.getInputStream());
			toNet = new Formatter(client.getOutputStream());

			String user = fromNet.nextLine();
			String pass = fromNet.nextLine();

			// check if login name and password combination exist
			String response = "";
			boolean found = false;
			for (int i = 0; i < login.length; i++) {
				if (login[i][0].equalsIgnoreCase(user) && login[i][1].equals(pass)) {
					response = "valid";
					found = true;
					break;
				}
			}

			if (!found) {
				response = "invalid";
			}

			toNet.format("%s\n", response);
			toNet.flush();

		} catch (IOException ioe) {
		}
	}
}
