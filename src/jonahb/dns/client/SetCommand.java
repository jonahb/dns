package jonahb.dns.client;

import java.io.PrintStream;
import java.net.*;
import jonahb.dns.*;

public class SetCommand extends Command
{
    private String[] args;
    private static final int DEFAULT_DNS_PORT = 53;

    public SetCommand( Client client, String[] args )
    {
        super( client, args );
        this.args = args;
    }

    @Override
    public void execute() throws CommandException
    {
        if ( args.length == 0 )
        {
            printAllSettings();
        }
        else if ( args[ 0 ].equals( "querytype" )  || args[ 0 ].equals( "qt" ) )
        {
            setQueryType();
        }
        else if ( args[ 0 ].equals( "nameserver" ) || args[ 0 ].equals( "ns" ) )
        {
            setNameServer();
        }
        else if ( args[ 0 ].equals( "queryclass" ) || args[ 0 ].equals( "qc") )
        {
            setQueryClass();
        }
        else
        {
            throw new CommandException( "Unrecognized setting: " + args[ 0 ] );
        }
    }

    private void setQueryClass() throws CommandException
    {
        if ( args.length != 2 )
        {
            throw new CommandException( "Usage: set queryclass query-class" );
        }

        client.setQueryClass( QueryClass.parse( args[ 1 ] ) );
    }

    private void setQueryType() throws CommandException
    {
        if ( args.length != 2 )
        {
            throw new CommandException( "Usage: set querytype query-type" );
        }

        client.setQueryType( QueryType.parse( args[ 1 ] ) );        
    }

    private void setNameServer() throws CommandException
    {
        if ( ( args.length < 2 ) || ( args.length > 3 ) )
        {
            throw new CommandException( "Usage: set nameserver ip-address [port]" );
        }

        InetAddress address = Util.parseDottedDecimalInet4Address( args[ 1 ] );
        int port = 0;

        try
        {
            port = ( args.length == 3 ) ? Integer.parseInt( args[ 2 ] ) : DEFAULT_DNS_PORT;
        }
        catch ( NumberFormatException e )
        {
            throw new CommandException( "Port must be a number" );
        }

        client.setNameServer( new NameServer( address, port ) );
    }

    private void printAllSettings()
    {
        PrintStream out = client.out();

        printSetting( out, "Query Type", client.getQueryType() );
        printSetting( out, "Query Class", client.getQueryClass() );
        printSetting( out, "Name Server", client.getNameServer() );
    }

    private void printSetting( PrintStream out, String name, Object value )
    {
        out.println( name + ":\t\t" + value.toString() );
    }
}
