package Q3;
import java.net.*; 
import java.io.*; 
/**
 * Cette classe client permet de g�rer les crash/timeout du server auquel le client est connect�
 * @author Alexandre Richard, Mathieu Lavallee, Mathieu Ferchaud
 *
 */
public class ServerQ3_2ndServer implements Runnable { 
	private Socket connection;
	public static void main(String[] args) throws IOException { 
    
		ServerSocket serverSocket = null; 

		try { 
			serverSocket = new ServerSocket(10119); 
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
				Runnable runnable = new ServerQ3_2ndServer(clientSocket);
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

	public ServerQ3_2ndServer(Socket clientSocket){
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
			inputLine = inputLine.toUpperCase();
        	out.println(inputLine);
        	if (inputLine.equals("Bye.")) 
        		break; 
        } 
		out.close(); 
		in.close(); 
		connection.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
	} 
} 
