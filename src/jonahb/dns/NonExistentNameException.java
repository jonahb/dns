package jonahb.dns;

public class NonExistentNameException extends QueryException
{
    public NonExistentNameException( String message )
    {
        super( message );
    }
}
