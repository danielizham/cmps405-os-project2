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

public class ServiceServer2 extends Thread {// other services in similar manner
	Socket client;
	Scanner fromNet = null;
	Formatter toNet = null;

	public ServiceServer2(Socket client) {
		this.client = client;
	}

	public void run() {
		System.out.println("ServiceServer2: Serving client ...");
		try {
			fromNet = new Scanner(client.getInputStream());
			toNet = new Formatter(client.getOutputStream());

		} catch (IOException ioe) {
		}

	}

}
