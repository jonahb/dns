package jonahb.dns.client;

import jonahb.dns.resolvers.StubResolver;
import jonahb.dns.resolvers.Resolver;
import jonahb.dns.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

public class ResolveCommand extends Command
{
    private DomainName domainName;

    public ResolveCommand( Client client, String[] args ) throws CommandException
    {
        super( client, args );

        if ( args.length != 1 )
        {
            throw new CommandException( "Usage: resolve domain" );
        }

        String domainNameString = args[ 0 ];

        try
        {
            this.domainName = DomainName.parse( domainNameString );
        }
        catch ( IllegalArgumentException e )
        {
            throw new CommandException( "Illegal domain name: " + domainNameString );
        }
    }

    public void execute() throws IOException, DNSException
    {
        PrintStream out = client.out();
        Resolver resolver = new StubResolver( client.getNameServer() );

        for ( InetAddress address : resolver.hostNameToHostAddress( this.domainName ) )
        {
            out.println( address.toString() );
        }
    }
}
