package Q3;
import java.net.*; 
import java.io.*; 

public class ServerQ3_2ndServer implements Runnable { 
	private Socket connection;
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
			System.err.println("Accept a échoué."); 
			System.exit(1); 
        } 	
		serverSocket.close(); 
	}

	public ServerQ3_2ndServer(Socket clientSocket){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
} 
