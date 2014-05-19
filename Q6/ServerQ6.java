package Q6;
import java.net.*; 
import java.io.*; 
/**
 * Server impl�mentant Runnable afin de pouvoir accepter plusieurs client � la fois + impl�mentation de la variable syncro entre 2 serveurs
 * + Connection vers 2nd serveur
 * @author Alexandre Richard, Mathieu Lavallee, Mathieu Ferchaud
 *
 */
public class ServerQ6 implements Runnable { 
	private Socket connection;
	
	//http://stackoverflow.com/questions/2120248/how-to-synchronize-a-static-variable-among-threads-running-different-instances-o
	private static int nbReq = 0;
	private static PrintWriter interServerWriter;
	public static synchronized int incrementNbReq() {
		nbReq++;
		interServerWriter.println(nbReq);
		return nbReq;
    }
	
	public static void main(String[] args) throws IOException { 
    
		ServerSocket serverSocket = null; 
		//On se connecte au serveur 2 comme si on �tait un client "sp�cial"
		interServerWriter = interServerConnection();

		try { 
			serverSocket = new ServerSocket(10118); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas �couter au  port: 10118."); 
			System.exit(1); 
        } 

		Socket clientSocket = null; 
		System.out.println ("Le serveur est en marche, Attente de la connexion.....");
		
		//Boucle qui g�re la cr�ation de thread pour chaques clients qui se connecte
		try { 
			while(true){
				clientSocket = serverSocket.accept();
				Runnable runnable = new ServerQ6(clientSocket);
		        Thread thread = new Thread(runnable);
		        thread.start();
			}
			
        } 
		catch (IOException e) 
        { 
			System.err.println("Accept a �chou�."); 
			System.exit(1); 
        } 	
		serverSocket.close(); 
	}

	public ServerQ6(Socket clientSocket){
		this.connection = clientSocket;
	}
	
	//Chaque thread va ex�cuter le code ci-dessous.
	@Override
	public void run() {
		System.out.println ("connexion r�ussie");
		System.out.println ("Attente de l'entr�e.....");
		try {
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true); 
		BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream())); 

		String inputLine;

		while ((inputLine = in.readLine()) != null) 
        { 
			System.out.println ("Serveur: " + inputLine);
			if(nbReq == 3){
			//Le serveur ne va pas r�pondre apres 2 messages
			
				//out.println(inputLine); do nothing and crash
				}
			else{
				inputLine = "#" +incrementNbReq()+" - "+ inputLine.toUpperCase();
				out.println(inputLine);
			}
			
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
	
	//On se connecte au serveur 2 comme si on �tait un client "sp�cial" afin d'envoit les num�ros de transactions
	private static PrintWriter interServerConnection(){
		//10.196.113.186
		String serverHostname = new String ("127.0.0.1");

		System.out.println ("Essai de se connecter � l'h�te " +
				serverHostname + " au port 10119.");

		Socket echoSocket = null;
		PrintWriter out = null;

		try {
			echoSocket = new Socket(serverHostname, 10119);
			echoSocket.setSoTimeout(10000);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			
		} catch (UnknownHostException e) {
			System.err.println("H�te inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Ne pas se connecter au serveur: " + serverHostname);
			System.exit(1);
		}
		out.println("Je suis le serveur");
		return out;
	}
} 
