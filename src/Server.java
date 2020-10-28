import java.util.*;
import java.io.*;
import java.net.*;

public class Server extends Thread {

	private BufferedReader inputUser;
	private PrintStream outputUser;
	private boolean loop;
	private static int size = 4;
	private static PileRPL pile = new PileRPL(size);
	private int dimension = 2;
	private Socket socket;
	private int id;
	private static int CLIENT_MAX = 5;
	private static PrintStream [] outputUsers = new PrintStream[CLIENT_MAX];

	public Server(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
		this.start();
	}

	public void run() {
		initRemote();
		mainLoop();
	}


	public void initRemote() {
		this.loop = true;

		try {
			inputUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputUser = new PrintStream(socket.getOutputStream());
			outputUsers[id] = outputUser;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void displayStack(){
		outputUser.println(pile);	
	}

	public void diffusionStack(){
		for(int i = 0; i < CLIENT_MAX; i++ )
		{
			try{
				outputUsers[i].println(pile);
			}catch(Exception e){
				continue;
			}
		}
	}

	private String getCommand(){
		outputUser.println("Entrez votre commande");
		String command="";
		try {
			command = inputUser.readLine();
		} catch (IOException e) {
			outputUser.println("[ERROR] I/O Exception");
		}
		return command;
	}

	private void runCommand(String command){
		// on considère qu'un commande est : "operande obj1 obj2 " ? 
		StringTokenizer st = new StringTokenizer(command, " ");

		while(st.hasMoreTokens()){
			String arg = st.nextToken();

			switch(arg){
				case "quit":
					this.loop = false;
					break;
				case "push":
						int[] vecteur = new int[this.dimension];

						for (int i = 0; i < vecteur.length; i++) { // on connait la dimension, --> boucler sur les args pour remplir
							arg = st.nextToken();
							vecteur[i] = Integer.parseInt(arg);
						}

						ObjEmp objEmp = new ObjEmp(vecteur); // creation de l'objet empilable avec notre vecteur comme attribut
						pile.push(objEmp);  // on push cet objEmp

				break;

				case "pop":
						pile.pop();
				break;

				default:
					pile.operation(arg);	
				break;
			}
		}


	}

	private void mainLoop(){
		displayStack();
		while(this.loop){
			
			String cmd = getCommand();
			runCommand(cmd);
			diffusionStack();

		}

			try {
				socket.close();
				
			} catch (Exception e) {
			}
	}



	public static void main(String[] args) {
		int port = 1234;
		ServerSocket serveSocket;
		Socket socket;
		int id = 0;
		try {
			serveSocket = new ServerSocket(port);
			while(id <= CLIENT_MAX){
			System.out.println("waiting for user... ");
			socket = serveSocket.accept();
			System.out.println("new user !");
			new Server(socket,id);
			id++;
			}
		} catch (Exception e1) {
		}
	}
}