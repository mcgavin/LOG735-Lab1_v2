package Q5;
import java.net.*; 
import java.io.*; 

public class ServerQ5 implements Runnable { 
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
			serverSocket = new ServerSocket(10118); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas écouter au  port: 10118."); 
			System.exit(1); 
        } 

		Socket clientSocket = null; 
		System.out.println ("Le serveur est en marche, Attente de la connexion.....");

		try { 
			while(true){
				clientSocket = serverSocket.accept();
				Runnable runnable = new ServerQ5(clientSocket);
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

	public ServerQ5(Socket clientSocket){
		this.connection = clientSocket;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println ("connexion réussie");
		System.out.println ("Attente de l'entrée.....");
		try {
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true); 
		BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream())); 

		String inputLine;

		while ((inputLine = in.readLine()) != null) 
        { 
			incrementNbReq();
			System.out.println ("Serveur: " + inputLine);
			inputLine = "#" +incrementNbReq()+" - "+ inputLine.toUpperCase();
			//Le serveur ne va pas répondre
        	//out.println(inputLine);
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
