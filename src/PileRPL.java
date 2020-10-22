public class PileRPL{

	private ObjEmp[] pile;
	private int nbObj = 0;
	private int stack_size;

	public PileRPL(int stack_size){

		this.pile = new ObjEmp[stack_size];
		this.stack_size = stack_size;
	}

	public void push(ObjEmp obj){
		this.pile[this.nbObj] = obj;
		this.nbObj ++;
	}

	public ObjEmp pop(){
		this.nbObj --;
		return this.pile[nbObj];
	}

	public void operation(String operande){
		ObjEmp obj1 = pop();		// pop du premier objet
		ObjEmp obj2 = pop();		// pop du deuxieme objet
		obj1.operation(operande,obj2);
		push(obj1);	// push du resultat dans la pile
	}

	public String toString(){
		String string = "";

		for (int i= stack_size-1;i>=0;i--) 
		{
			string += (i< nbObj?i+": "+pile[i]+"\n":i+": "+"\n");
		}		
			return string;
	}
}
