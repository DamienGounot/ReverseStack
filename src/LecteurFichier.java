import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class LecteurFichier {

	private BufferedReader lecteur = null;
        private String ligne;
        
        public void afficheFichier(String nomDuFichier) throws IOException {

		try {
			lecteur = new BufferedReader( new FileReader( nomDuFichier ) );
		} catch( FileNotFoundException exc ) {
			System.out.println( "le fichier n'existe pas" );
			System.exit( 1 );
		}

		while( (ligne=lecteur.readLine()) != null)
			System.out.println( ligne );
		lecteur.close();
	
    }

}
