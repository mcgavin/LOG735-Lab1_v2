package Q2;
import java.io.*;
import java.net.*;

/****************************************************** 
Cours : LOG735 
Session : �t� 2014 
Groupe : 01 
Projet : Laboratoire #1 
�tudiants : Alexandre Richard 
			Mathieu Lavallee
			Mathieu Ferchaud
Code(s) perm. : RICA10028806
				Lavm04108908 
				FERM23018803
Date cr�ation : 12 MAI 2014
Date dern. modif. : 19 MAI 2014
****************************************************** 
Copie identique � Client.java par d�faut

******************************************************/ 

public class ClientQ2 {
	public static void main(String[] args) throws IOException {

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
        String userInput;
        System.out.print ("Entr�e: ");
        while ((userInput = stdIn.readLine()) != null) {
        	out.println(userInput);
        	System.out.println("echo: " + in.readLine());
            System.out.print ("Entr�e: ");
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

