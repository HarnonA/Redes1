package MultiChat;

import java.util.Vector;

public class Unic {

	private static Vector<String> vec;
	
	public static Vector<String> getinstance(){
		if(vec == null) 
		{
			startU();
		}
		return vec;
		
	}
	public void alter(Vector<String> v){
		vec = v;
	}
	
	private static synchronized void startU(){
		if(vec == null)
			vec = new Vector<>();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
