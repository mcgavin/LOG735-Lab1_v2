package Q6;
import java.io.*;
import java.net.*;

/****************************************************** 
Cours : LOG735 
Session : Été 2014 
Groupe : 01 
Projet : Laboratoire #1 
Étudiants : Alexandre Richard 
			Mathieu Lavallee
			Mathieu Ferchaud
Code(s) perm. : RICA10028806
				Lavm04108908 
				FERM23018803
Date création : 12 MAI 2014
Date dern. modif. : 19 MAI 2014
****************************************************** 
Cette classe client permet de gèrer les crash/timeout du server auquel le client est connecté

******************************************************/ 
public class ClientQ6 {
	public static void main(String[] args) throws IOException {
		//Connection normal vers le server 1 (initial)
		//10.196.113.186
		String serverHostname = new String ("127.0.0.1");

		if (args.length > 0)
			serverHostname = args[0];
		System.out.println ("Essai de se connecter à l'hôte " +
				serverHostname + " au port 10118.");

		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String userInput = "";
		try {
			echoSocket = new Socket(serverHostname, 10118);
			//Si on recoit pas de message d'ici 10 secondes, on timeout le socket.
			echoSocket.setSoTimeout(10000);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Hôte inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Ne pas se connecter au serveur: " + serverHostname);
			System.exit(1);
		}

		//Exécution normal tant que le serveur 1 reste fonctionnel.
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try{
			
			System.out.print ("Entrée: ");
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				System.out.print ("Entrée: ");
			}
		}
		//IOException permet de faire la gestion de toutes les erreurs dû a un serveur qui crash/timeout.
		catch (IOException iioe)
		{
			//On ferme la connection
			System.err.println ("Remote host timed out during read operation");
			echoSocket.close();
			
			//On se connecte au nouveau serveur
			System.out.println ("Essai de se connecter à l'hôte " +
					serverHostname + " au port 10119.");
			try {
				echoSocket = new Socket(serverHostname, 10119);
				out = new PrintWriter(echoSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				//On refait la commande du serveur qui a crash sur le nouveau serveur
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
			} catch (UnknownHostException e) {
				System.err.println("Hôte inconnu: " + serverHostname);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Ne pas se connecter au serveur: " + serverHostname);
				System.exit(1);
			}

			//Éxécution normal sur le nouveau serveur.
			
			System.out.print ("Entrée: ");
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				System.out.print ("Entrée: ");
			}
		}
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	}
	
}

