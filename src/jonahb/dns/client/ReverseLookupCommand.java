package jonahb.dns.client;

import jonahb.dns.resolvers.StubResolver;
import jonahb.dns.resolvers.Resolver;
import jonahb.dns.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

public class ReverseLookupCommand extends Command
{
    private InetAddress address;

    public ReverseLookupCommand( Client client, String[] args ) throws CommandException
    {
        super( client, args );

        if ( args.length != 1 )
        {
            throw new CommandException( "Usage: reverse ip4-address" );
        }

        String ipAddressString = args[ 0 ];

        try
        {
            this.address = Util.parseDottedDecimalInet4Address( ipAddressString );
        }
        catch ( IllegalArgumentException e )
        {
            throw new CommandException( "Invalid IPv4 address: " + ipAddressString );
        }
    }

    public void execute() throws IOException, DNSException
    {
        PrintStream out = client.out();
        Resolver resolver = new StubResolver( client.getNameServer() );

        DomainName name = resolver.hostAddressToHostName( address );
        out.println( name.toString() );
    }
}
