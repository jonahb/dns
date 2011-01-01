package jonahb.dns;

public class ResponseCode
{
    private int code;

    public ResponseCode( int code )
    {
        this.code = code;
    } 

    private static final int NO_ERROR_CODE          = 0;
    private static final int FORMAT_ERROR_CODE      = 1;
    private static final int SERVER_FAILURE_CODE    = 2;
    private static final int NAME_ERROR_CODE        = 3;
    private static final int NOT_IMPLEMENTED_CODE   = 4;
    private static final int REFUSED_CODE           = 5;

    public static final ResponseCode NO_ERROR            = new ResponseCode( NO_ERROR_CODE );
    public static final ResponseCode FORMAT_ERROR        = new ResponseCode( FORMAT_ERROR_CODE );
    public static final ResponseCode SERVER_FAILURE      = new ResponseCode( SERVER_FAILURE_CODE );
    public static final ResponseCode NAME_ERROR          = new ResponseCode( NAME_ERROR_CODE );
    public static final ResponseCode NOT_IMPLEMENTED     = new ResponseCode( NOT_IMPLEMENTED_CODE );
    public static final ResponseCode REFUSED             = new ResponseCode( REFUSED_CODE );

    public int getCode()
    {
        return this.code;
    }

    @Override
    public boolean equals( Object other )
    {
        if ( other != null )
        {
            if ( other.getClass() == this.getClass() )
            {
                return this.getCode() == ((ResponseCode)other).getCode();
            }
        }

        return false;
    }

    public boolean isError()
    {
        return !this.equals( ResponseCode.NO_ERROR );
    }

    public void throwIfError() throws QueryException
    {
        if ( this.isError() )
        {
            throw this.toException();
        }
    }

    private QueryException toException()
    {
        String message = "";

        switch ( this.code )
        {
            case NO_ERROR_CODE:
            {
                throw new IllegalArgumentException( "Can not create NameServerException for response code NO_ERROR" );
            }
            case FORMAT_ERROR_CODE:
            {
                message = "Name server was unable to interpret the query";
                break;
            }
            case SERVER_FAILURE_CODE:
            {
                message = "Name server was unable to process this query due to a problem with the name server";
                break;
            }
            case NAME_ERROR_CODE:
            {
                return new NonExistentNameException( "Domain name does not exist" );
            }
            case NOT_IMPLEMENTED_CODE:
            {
                message = "Name server does not support requested kind of query";
                break;
            }
            case REFUSED_CODE:
            {
                message = "Name server refuses to perform the specified operation for policy reasons";
                break;
            }
            default:
            {
                message = "Unknown name server error (" + code + ")";
                break;
            }

        }

        return new QueryException( message );
    }
}
