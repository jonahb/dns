package jonahb.dns.client;

import jonahb.dns.Util;
import jonahb.dns.NameServer;
import java.net.InetAddress;

public class Program
{
    public static void main( String[] args ) throws Exception
    {
        if ( args.length != 2 )
        {
            System.err.println( "Required arguments: nameserver-ip-address nameserver-port" );
            System.exit( 1 );
        }

        InetAddress address = null;
        int port = 0;

        try
        {
            address = Util.parseDottedDecimalInet4Address( args[ 0 ] );
            port = Integer.parseInt( args[ 1 ] );
        }
        catch ( NumberFormatException e )
        {
            System.err.println( "nameserver-port must be an integer" );
            System.exit( 1 );
        }
        catch ( IllegalArgumentException e )
        {
            System.err.println( "nameserver-ip-address must be an IPv4 address in dotted-decimal notation" );
            System.exit( 1 );
        }

        new Client( new NameServer( address, port ) ).run();
    }
}