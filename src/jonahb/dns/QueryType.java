package jonahb.dns;

import java.util.Locale;

public class QueryType
{
    private int code;

    public QueryType( int code )
    {
        this.code = code;
    }

    public static final QueryType A     = new QueryType( 1 );
    public static final QueryType NS    = new QueryType( 2 );
    public static final QueryType CNAME = new QueryType( 5 );
    public static final QueryType SOA   = new QueryType( 6 );
    public static final QueryType WKS   = new QueryType( 11 );
    public static final QueryType PTR   = new QueryType( 12 );
    public static final QueryType HINFO = new QueryType( 13 );
    public static final QueryType MINFO = new QueryType( 14 );
    public static final QueryType MX    = new QueryType( 15 );
    public static final QueryType TXT   = new QueryType( 16 );
    public static final QueryType AAAA  = new QueryType( 28 );
    public static final QueryType All   = new QueryType( 255 );

    @Override
    public boolean equals( Object other )
    {
        if ( other != null )
        {
            if ( other.getClass() == this.getClass() )
            {
                return this.getCode() == ((QueryType)other).getCode();
            }
        }

        return false;
    }

    public int getCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        switch ( code )
        {
            case 1: return "A";
            case 2: return "NS";
            case 5: return "CNAME";
            case 6: return "SOA";
            case 11: return "WKS";
            case 12: return "PTR";
            case 13: return "HINFO";
            case 14: return "MINFO";
            case 15: return "MX";
            case 16: return "TXT";
            case 28: return "AAAA";
            case 255: return "All";
            default:
            {
                return "Unknown (" + code + ")";
            }      
        }
    }

    public static QueryType parse( String type )
    {
        type = type.toUpperCase( Locale.ENGLISH );

        if ( type.equals( "A" ) )
            return QueryType.A;
        else if ( type.equals( "NS") )
            return QueryType.NS;
        else if ( type.equals( "CNAME") )
            return QueryType.CNAME;
        else if ( type.equals( "SOA") )
            return QueryType.SOA;
        else if ( type.equals( "WKS") )
            return QueryType.WKS;
        else if ( type.equals( "PTR") )
            return QueryType.PTR;
        else if ( type.equals( "HINFO") )
            return QueryType.HINFO;
        else if ( type.equals( "MINFO") )
            return QueryType.MINFO;
        else if ( type.equals( "MX") )
            return QueryType.MX;
        else if ( type.equals( "TXT") )
            return QueryType.TXT;
        else if ( type.equals( "AAAA") )
            return QueryType.AAAA;
        else if ( type.equals( "ALL") )
            return QueryType.All;
        else if ( type.equals( "*") )
            return QueryType.All;
        else
        {
            throw new IllegalArgumentException( "Unrecognized query type: " + type );
        }
    }
 }