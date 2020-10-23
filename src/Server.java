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
	private static PrintStream [] outputMulti = new PrintStream[CLIENT_MAX];

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
			outputMulti[id] = outputUser;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void displayStack() {
		outputUser.println(pile);
	}

	public void diffusionStack(){
		for(int i = 0; i < CLIENT_MAX; i++ ){
			try{
		outputMulti[i].println(pile);
			}catch(Exception e){
				continue;
			}
		}
	}
	public String getCommand() {

		String cmd;

		try {
			cmd = inputUser.readLine();
			return cmd;
		} catch (IOException e) {
			e.printStackTrace();
			return " Erreur IO Exception " + e;
		}

	}

	public void runCommand(String cmd) {
		StringTokenizer st;
		st = new StringTokenizer(cmd, " ");
		while (st.hasMoreTokens()) {
			String arg = st.nextToken();
			switch (arg) {
				// faire case help
				case "quit":
					// fermeture de fichier et autre ici
					loop = false;
					break;
				case "push":
					int array[] = new int[dimension];
					try {
						for (int i = 0; i < dimension; i++) {
							array[i] = Integer.parseInt(st.nextToken());
							;
						}
						ObjEmp value = new ObjEmp(array);
						pile.push(value);
					} catch (Exception E) {
						outputUser.println("Erreur la taille de la pile a été dépassé : ");
					}
					break;
				case "help":
					outputUser.println(
							"Commande utilisable sont les suivantes :\npush permet de rajouter des elements dans la pile\nquit permet de quitter le programme\n Permet de realiser les operations : add, sub, mul, div");
					break;
				default:
					pile.operation(arg);

			}
		}
	}

	public void mainLoop() {
		displayStack();
		while (loop) {
			String cmd = getCommand();
			runCommand(cmd);
			diffusionStack();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 1234;
		ServerSocket serv;
		Socket socket;
		int id =0;
		try {
			serv = new ServerSocket(port);
			while(id <= CLIENT_MAX){
			System.out.println("waiting... ");
			socket = serv.accept();
			System.out.println("Connexion !!!");
			new Server(socket,id);
			id++;
			}
		} catch (Exception e1) {
		}
	}
}
