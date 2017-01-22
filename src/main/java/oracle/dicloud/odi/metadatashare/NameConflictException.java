package oracle.dicloud.odi.metadatashare;

public class NameConflictException extends Exception{
	

 
 public NameConflictException(String name){
	 super(String.format("The name \"%s\" is already used, please change another name.", name));
 }
 

}
