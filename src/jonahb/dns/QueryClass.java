package jonahb.dns;

import java.util.Locale;

public class QueryClass
{
    private int code;

    public QueryClass( int code )
    {
        this.code = code;
    }

    private static final int INTERNET_CODE    = 1;
    private static final int CHAOS_CODE       = 3;
    private static final int HESIOD_CODE      = 4;
    private static final int ANY_CODE         = 255;

    public static final QueryClass INTERNET   = new QueryClass( INTERNET_CODE );
    public static final QueryClass CHAOS      = new QueryClass( CHAOS_CODE );
    public static final QueryClass HESIOD     = new QueryClass( HESIOD_CODE );
    public static final QueryClass ANY        = new QueryClass( ANY_CODE );

    public int getCode()
    {
        return code;
    }

    @Override
    public boolean equals( Object other )
    {
        if ( other != null )
        {
            if ( other.getClass() == this.getClass() )
            {
                return this.getCode() == ((QueryClass)other).getCode();
            }
        }

        return false;
    }    

    @Override
    public String toString()
    {
        switch ( code )
        {
            case 1: return "Internet";
            case 3: return "CHAOS";
            case 4: return "Hesiod";
            case 255: return "Any";
            default:
            {
                return "Unknown (" + code + ")";
            }
        }
    }

    public static QueryClass parse( String queryClass )
    {
        queryClass = queryClass.toUpperCase( Locale.ENGLISH );

        if ( queryClass.equals( "INTERNET" ) )
            return QueryClass.INTERNET;
        else if ( queryClass.equals( "CHAOS" ) )
            return QueryClass.CHAOS;
        else if ( queryClass.equals( "HESIOD" ) )
            return QueryClass.HESIOD;
        else if ( queryClass.equals( "ANY") )
            return QueryClass.ANY;
        else if ( queryClass.equals( "*") )
            return QueryClass.ANY;
        else
        {
            throw new IllegalArgumentException( "Unrecognized query class: " + queryClass );
        }
    }
}