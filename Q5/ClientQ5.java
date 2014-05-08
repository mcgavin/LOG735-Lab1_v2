package Q5;
import java.io.*;
import java.net.*;

public class ClientQ5 {
	public static void main(String[] args) throws IOException {

		//10.196.113.186
		String serverHostname = new String ("127.0.0.1");

		if (args.length > 0)
			serverHostname = args[0];
		System.out.println ("Essai de se connecter � l'h�te " +
				serverHostname + " au port 10118.");

		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			echoSocket = new Socket(serverHostname, 10118);
			echoSocket.setSoTimeout(10000);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("H�te inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Ne pas se connecter au serveur: " + serverHostname);
			System.exit(1);
		}


		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try{
			String userInput;
			System.out.print ("Entr�e: ");
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				System.out.print ("Entr�e: ");
			}
		}
		catch (InterruptedIOException iioe)
		{
			System.err.println ("Remote host timed out during read operation");
			echoSocket.close();
			
			//code connection new serveur
			System.out.println ("Essai de se connecter � l'h�te " +
					serverHostname + " au port 10119.");
			try {
				echoSocket = new Socket(serverHostname, 10119);
				out = new PrintWriter(echoSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("H�te inconnu: " + serverHostname);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Ne pas se connecter au serveur: " + serverHostname);
				System.exit(1);
			}

			//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput;
			System.out.print ("Entr�e: ");
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				System.out.print ("Entr�e: ");
			}
		}
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	}
}
