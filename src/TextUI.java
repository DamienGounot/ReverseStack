import java.io.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import java.io.InputStreamReader;
import java.util.*;

public class TextUI{

	private BufferedReader inputUser;
	private PrintStream outputUser;
	private PrintStream outputLog; // pout le mode enregistrement
	private Boolean loop = true;
	private PileRPL pile;
	private final int pileSize = 4;
	private int dimension; // dimension des ObjEmp
	private boolean log; // pour le log, noLog
	private boolean local; // pour le local ou remote
	private final int port = 1234;
	private Socket socket;
	private ServerSocket serverSocket;

	public TextUI(String[] args) throws IOException{
		initStream(args);
		this.pile = new PileRPL(this.pileSize);
		dimension = initDimension();
		mainLoop();

	}

	public static void main(String[] args) throws IOException{
		try {
			TextUI textui = new TextUI(args);
		} catch (Exception e) {
			System.out.println("[ERROR] Saisie des arguments obligatoire");
		}	
		

	}

	private void mainLoop() throws IOException{

		while(this.loop){

			displayStack();
			String cmd = getCommand();
			runCommand(cmd);
		}
			if (!local) {
				try {
					socket.close();
					
				} catch (Exception e) {
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

		if(this.log){ // si Log
			log(command);
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

	private void initConsole(){
		this.local = true;
		this.inputUser = new BufferedReader( new InputStreamReader(System.in));
		this.outputUser = new PrintStream(System.out);
	}
	private void initRemote() throws IOException{
		this.local = false;
		this.serverSocket = new ServerSocket(this.port);
		try {
				this.socket = this.serverSocket.accept(); // NB : accept est bloquant
				this.inputUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.outputUser = new PrintStream(this.socket.getOutputStream());
		} catch( IOException e ) {
		}
	}

	private void initNoLog() throws IOException {
		this.log = false;
	}
	private void initLog() throws IOException {
		this.log = true;;
		try {
			this.outputLog = new PrintStream(new File("log.txt"));
		} catch (Exception e) {
			this.outputUser.println("Erreur, impossible de creer le fichier de log");
		}
	}
	private void initReplay(){
		try {
			this.inputUser = new BufferedReader(new FileReader("log.txt"));
		} catch (Exception e) {
			this.outputUser.println("Erreur, impossible de lire dans le fichier de log");
		}
	}

	public
	
	void displayStack(){
		outputUser.println(this.pile);	
	}

	private int initDimension() throws IOException {
		int dimension;
		this.outputUser.println(("Indiquez la dimension des objets empilables"));
		String saisie = this.inputUser.readLine();
		try {
			dimension = Integer.parseInt(saisie);
		} catch (Exception e) {
			dimension = 2; // si l'utilisateur se trompe dans la saisie de la dimension --> dimension 2 saisie par defaut.
			outputUser.println("[ERROR]Dimension saisie invalide, la dimension est fixée à la dimension par défaut: "+ dimension);
		}
			outputUser.println("Dimension choisie: "+dimension);
		
		if(this.log){ // si Log
			outputLog.println(dimension);
		}

		return dimension;
	}

	private void initStream(String[] arguments) throws IOException {

			switch (arguments[0]) {
				case "0":	// session locale
				initConsole();					
					break;
				case "1":	//session remote
				initRemote();
					break;
			
				default:
				initConsole(); // si saisie erronnée, on lance en mode local par defaut
					break;
			}

			switch (arguments[1]) {
				case "0":	
					initNoLog(); //pas de log
					break;
				case "1": // enregistrement de session
					initLog();
					break;

				case "2": // rejeu de session
					initReplay();
					break;

				default:
					initNoLog();// si saisie erronnée, on lance en mode noLog
					break;
			}

	}

	public void log(String string){
		try {
			outputLog.println(string + " ");
	} catch (Exception e) {
	}
	}

}