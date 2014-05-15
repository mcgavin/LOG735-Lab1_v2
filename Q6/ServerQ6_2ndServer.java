package Q6;
import java.net.*; 
import java.io.*; 

public class ServerQ6_2ndServer implements Runnable { 
	private Socket connection;

	//http://stackoverflow.com/questions/2120248/how-to-synchronize-a-static-variable-among-threads-running-different-instances-o
	private static int nbReq = 0;
	public static synchronized int incrementNbReq() {
		nbReq++;
		return nbReq;
	}

	public static synchronized void setNbReq(int nbreq) {
		nbReq = nbreq;
	}
	
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

		try { 
			while(true){
				clientSocket = serverSocket.accept();
				Runnable runnable = new ServerQ6_2ndServer(clientSocket);
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

	public ServerQ6_2ndServer(Socket clientSocket){
		this.connection = clientSocket;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println ("connexion r�ussie");
		System.out.println ("Attente de l'entr�e.....");
		try {
			PrintWriter out = new PrintWriter(connection.getOutputStream(), true); 
			BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream())); 
			String inputLine;
			inputLine = in.readLine();
			
			if(inputLine.equals("Je suis le serveur")){
				//server connection
				
				while((inputLine = in.readLine()) != null){
					setNbReq(Integer.parseInt(inputLine));
				}
			}
			else {
				// client
				System.out.println ("Serveur: " + inputLine);
				//inputLine = inputLine.toUpperCase();
				inputLine = "#" +incrementNbReq()+" - "+ inputLine.toUpperCase();
				out.println(inputLine);
				
				while ((inputLine = in.readLine()) != null) 
				{ 

					System.out.println ("Serveur: " + inputLine);
					//inputLine = inputLine.toUpperCase();
					inputLine = "#" +incrementNbReq()+" - "+ inputLine.toUpperCase();
					out.println(inputLine);
					if (inputLine.equals("Bye.")) 
						break; 
				}
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
