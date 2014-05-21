package Q5;
import java.net.*; 
import java.io.*; 

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
Cette classe client permet de gèrer les crash/timeout du server auquel le client est connecté. Implémentation de la variable syncro entre
2 serveurs

******************************************************/ 
public class ServerQ5_2ndServer implements Runnable { 
	private Socket connection;
	
	//http://stackoverflow.com/questions/2120248/how-to-synchronize-a-static-variable-among-threads-running-different-instances-o
	private static int nbReq = 0;
	public static synchronized int incrementNbReq() {
		nbReq++;
		return nbReq;
    }
	
	public static void main(String[] args) throws IOException { 
    
		ServerSocket serverSocket = null; 

		try { 
			serverSocket = new ServerSocket(10119); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas écouter au  port: 10118."); 
			System.exit(1); 
        } 

		Socket clientSocket = null; 
		System.out.println ("Le serveur est en marche, Attente de la connexion.....");
		//Boucle qui gère la création de thread pour chaques clients qui se connecte
		try { 
			while(true){
				clientSocket = serverSocket.accept();
				Runnable runnable = new ServerQ5_2ndServer(clientSocket);
		        Thread thread = new Thread(runnable);
		        thread.start();
			}
			
        } 
		catch (IOException e) 
        { 
			System.err.println("Accept a échoué."); 
			System.exit(1); 
        } 	
		serverSocket.close(); 
	}

	public ServerQ5_2ndServer(Socket clientSocket){
		this.connection = clientSocket;
	}
	
	//Chaque thread va exécuter le code ci-dessous.
	@Override
	public void run() {
		System.out.println ("connexion réussie");
		System.out.println ("Attente de l'entrée.....");
		try {
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true); 
		BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream())); 

		String inputLine;

		while ((inputLine = in.readLine()) != null) 
        { 
			System.out.println ("Serveur: " + inputLine);
			//On appelle la methode incrementNbReq() afin d'augementer la variable syncro
			inputLine = "#" +incrementNbReq()+" - "+ inputLine.toUpperCase();
        	out.println(inputLine);
        	if (inputLine.equals("Bye.")) 
        		break; 
        } 
		out.close(); 
		in.close(); 
		connection.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
} 
