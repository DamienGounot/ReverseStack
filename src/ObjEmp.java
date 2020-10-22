public class ObjEmp
{
	private int[] vecteur;

	public ObjEmp(int[] vecteur){

		this.vecteur = vecteur; 

	}

	public boolean compareTo(ObjEmp obj){

		if(obj.vecteur.length == this.vecteur.length){
			return true;
		}else{
			return false;
		}
	}

	public ObjEmp operation(String operation, ObjEmp obj2){

		switch(operation){
			case "add":

				for(int i =0 ; i < this.vecteur.length; i++){
					this.vecteur[i] += obj2.vecteur[i];
				}
				break;
			case "min":

				for(int i =0 ; i < this.vecteur.length; i++){
					this.vecteur[i] -= obj2.vecteur[i];
				} 
				break;
			case "mul":

				for(int i =0 ; i < this.vecteur.length; i++){
					this.vecteur[i] *= obj2.vecteur[i];
				}

				break;
			case "div":

				for(int i =0 ; i < this.vecteur.length; i++){
					this.vecteur[i] /= obj2.vecteur[i];
				}
				break;
			default:
			 //outputUser.println("Erreur operande"); // pourquoi pas d'accès a outputUser sachant qu'il est public ?
			break;
		}

		return this;
	}

	        public String toString(){
                String string = "";

                for (int i= vecteur.length-1;i>=0;i--)
                {
                       string += vecteur[i] + " ";
                }
                        return string;
        }

}
