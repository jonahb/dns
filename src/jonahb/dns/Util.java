package jonahb.dns;
import java.util.*;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.Inet4Address;

public class Util
{
    public static final int DATAGRAM_PACKET_MAX_BYTES = 512;
    public static final int DEFAULT_DNS_PORT = 53;

    public static Collection getElementsByClass( Iterable elements, Class klass )
    {
        Vector results = new Vector();

        for ( Object element : elements )
        {
            if ( klass.isAssignableFrom( element.getClass() ) )
            {
                results.add( element );
            }
        }

        return results;
    }

    public static InetAddress parseDottedDecimalInet4Address( String address )
    {
        if ( address == null )
        {
            throw new NullPointerException();
        }

        if ( !isDottedDecimalInet4Address( address ) )
        {
            throw new IllegalArgumentException( "Address is not a valid IPv4 address in dotted-decimal form" );
        }

        try
        {
            return InetAddress.getByName( address );
        }
        catch ( UnknownHostException e )
        {
            // this should not happen because InetAddress.getByName does not
            // perform reverse ip address resolution unless passed a host name
            return null;
        }
    }

    // we need this because InetAddress.getByName translates the address
    // to a domain name automatically when the address is in dotted-decimal form
    public static boolean isDottedDecimalInet4Address( String address )
    {
        if ( address == null )
        {
            throw new NullPointerException();
        }

        String[] parts = address.split( "\\." );

        if ( parts.length != 4 )
        {
            return false;
        }

        for ( String part : parts )
        {
            try
            {
                int octet = Integer.parseInt( part );
                if ( ( octet < 0 ) || ( octet > 0xff ) )
                {
                    return false;
                }
            }
            catch ( NumberFormatException e )
            {
                return false;
            }
        }

        return true;
    }
}
