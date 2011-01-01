package jonahb.dns.client;

import jonahb.dns.resolvers.StubResolver;
import jonahb.dns.resolvers.Resolver;
import jonahb.dns.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Collection;

public class QueryCommand extends Command
{
    private static final String NON_RECURSIVE_OPTION = "-nr";

    private QueryType queryType;
    private QueryClass queryClass;
    private DomainName domainName;
    private boolean recursive = true;

    public QueryCommand( Client client, String[] args ) throws CommandException
    {
        super( client, args );

        if ( args.length < 1 || args.length > 4 )
        {
            throw new CommandException( "Usage: query [options] domain [query-type [query-class]]\nOptions: -nr non-recursive" );
        }

        int i = 0;

        if ( args[ i ].equals( NON_RECURSIVE_OPTION ) )
        {
            this.recursive = false;
            ++i;
        }

        String domainNameString = args[ i++ ];

        try
        {
            this.domainName = DomainName.parse( domainNameString );
        }
        catch ( IllegalArgumentException e )
        {
            throw new CommandException( "Illegal domain name: " + domainNameString );
        }

        this.queryType = ( args.length > i ) ? QueryType.parse( args[ i ] ) : client.getQueryType();
        ++i;

        this.queryClass = ( args.length > i ) ? QueryClass.parse( args[ i ] ) : client.getQueryClass();
        ++i;
    }

    public void execute() throws IOException, DNSException
    {
        PrintStream out = client.out();
        Resolver resolver = new StubResolver( client.getNameServer(), this.recursive );
        Message response = resolver.query( domainName, queryType, queryClass );

        out.print( "Authoritative: " );
        out.println( response.answerAuthoritative() ? "Yes" : "No" );

        out.print( "Recursive: " );
        out.println( this.recursive && response.recursionAvailable() ? "Yes" : "No" );

        if ( response.getQuestions().size() > 0 )
        {
            out.print( "Query Type: " );
            out.println( response.getQuestions().get( 0 ).getQueryType().toString() );
        }

        printResourceRecords( "Answers", response.getAnswers(), out );
        printResourceRecords( "Authorities", response.getAuthorities(), out );
        printResourceRecords( "Additional", response.getAdditionals(), out );
    }

    private void printResourceRecords( String label, Vector<ResourceRecord> records, PrintStream out )
    {
        out.println( label + ":" );

        if ( records.isEmpty() )
        {
            out.println( " [none]");
        }
        else
        {
            for ( ResourceRecord rr : records )
            {
                out.print(" ");
                out.println( rr );
            }
        }
    }
}
