package jonahb.dns.client;

import jonahb.dns.*;
import jonahb.dns.resolvers.*;
import java.io.IOException;
import jonahb.dns.resourcerecords.*;
import java.util.List;
import java.util.Collection;
import java.io.PrintStream;

public class TraceCommand extends Command
{
    private DomainName domainName;

    public TraceCommand( Client client, String[] args ) throws CommandException
    {
        super( client, args );

        if ( args.length != 1 )
        {
            throw new CommandException( "Usage: trace domain" );
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

    public void execute() throws IOException, DNSException, CommandException
    {
        PrintStream out = client.out();
        DomainName name = this.domainName;
        Resolver resolver = new StubResolver( client.getNameServer() );

        CNAMEResourceRecord cname = queryCanonicalName( name, resolver );

        if ( cname != null )
        {
            printCNAME( cname, out );
            name = cname.getCanonicalName();
        }

        List<DomainName> zones = name.getZones();

        if ( zones.size() < 1 )
        {
            throw new CommandException( "Domain name contains no zones" );
        }

        // nameservers for each zone
        for ( int i = 0; i < zones.size() - 1; ++i )
        {
            DomainName zone = zones.get( i );
            NSResourceRecord ns = queryNameServer( zone, resolver );

            if ( ns == null )
            {
                out.print( zone );
                out.print( " " );
                out.println( "No NS records found" );
            }
            else
            {
                printNS( ns, out );
            }
        }

        // address for the host
        AResourceRecord a = queryAddress( name, resolver );

        if ( a == null )
        {
            out.print( name );
            out.print( " " );
            out.println( "No A records found" );
        }
        else
        {
            printA( a, out );
        }
    }

    private void printA( AResourceRecord a, PrintStream out )
    {
        out.print( a.getDomainName() );
        out.print( " [");
        out.print( a.getAddress() );
        out.print( "]");
        out.println( " (Address)");
    }

    private void printNS( NSResourceRecord ns, PrintStream out )
    {
        out.print( ns.getDomainName() );
        out.print( " [");
        out.print( ns.getNameServerDomainName() );
        out.print( "]" );        
        out.println( " (Name Server)" );
    }

    private void printCNAME( CNAMEResourceRecord cname, PrintStream out )
    {
        out.print( cname.getDomainName() );
        out.print( " [");
        out.print( cname.getCanonicalName() );
        out.print( "]" );
        out.println( " (Canonical Name)" );
    }

    private AResourceRecord queryAddress( DomainName name, Resolver resolver )  throws IOException, DNSException
    {
        Message response = resolver.query( name, QueryType.A, QueryClass.INTERNET );

        Collection<AResourceRecord> as = Util.getElementsByClass( response.getAnswers(), AResourceRecord.class );

        return as.isEmpty() ? null : as.iterator().next();
    }

    private NSResourceRecord queryNameServer( DomainName name, Resolver resolver )  throws IOException, DNSException
    {
        Message response = resolver.query( name, QueryType.NS, QueryClass.INTERNET );

        Collection<NSResourceRecord> nss = Util.getElementsByClass( response.getAnswers(), NSResourceRecord.class );

        // according to assignment, drop all but first nameserver
        return nss.isEmpty() ? null : nss.iterator().next();
    }

    private CNAMEResourceRecord queryCanonicalName( DomainName name, Resolver resolver ) throws IOException, DNSException
    {
        Message response = resolver.query( name, QueryType.CNAME, QueryClass.INTERNET );

        Collection<CNAMEResourceRecord> cnames = Util.getElementsByClass( response.getAnswers(), CNAMEResourceRecord.class );

        // we drop all CNAMES but the first (but there should be only one)
        return cnames.isEmpty() ? null : cnames.iterator().next();
    }
}