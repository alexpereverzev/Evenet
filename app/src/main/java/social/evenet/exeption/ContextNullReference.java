package social.evenet.exeption;

public class ContextNullReference extends Exception{

	private static final long serialVersionUID = -6037473295423189831L;
	
	public ContextNullReference() {}

    public ContextNullReference(String message)
    {
       super(message);
    }	

}
