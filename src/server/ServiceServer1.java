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

public class ServiceServer1 extends Thread {
	Socket client;
	Scanner fromNet = null;
	Formatter toNet = null;
	static int[] votes = new int[10]; // one instance to accumulate voting results

	public ServiceServer1(Socket client) {
		this.client = client;
	}

	public void run() {
		System.out.println("Voting Service: Serving client ...");
		try {
			fromNet = new Scanner(client.getInputStream());
			toNet = new Formatter(client.getOutputStream());

			int vote = Integer.parseInt(fromNet.nextLine());
			votes[vote]++;
			
			showVotes();
		} catch (IOException ioe) {
		}
	}

	public void showVotes() {
		System.out.printf("%n%10s%8s%n", "Candidates", "Votes");
		System.out.printf("%10s%8s%n", "----------", "-----");
		for (int i = 0; i < votes.length; i++) {
			System.out.printf("%5d%11d%n", i, votes[i]);
		}
		System.out.println();
	}
}
