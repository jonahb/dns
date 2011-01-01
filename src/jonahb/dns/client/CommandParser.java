package jonahb.dns.client;

public class CommandParser
{
    private static final String OPTION_PREFIX = "-";

    public static Command parse( Client client, String commandString ) throws CommandException
    {
        String[] args = commandString.split( " " );

        if ( args.length < 1 )
        {
            throw new CommandException( "Empty command" );
        }

        String command = args[ 0 ];
        String[] rest = java.util.Arrays.copyOfRange( args, 1, args.length );

        if ( command.equals( "set" ) || command.equals( "s" ) )
        {
            return new SetCommand( client, rest );
        }
        if ( command.equals( "resolve" ) || command.equals( "r") )
        {
            return new ResolveCommand( client, rest );
        }
        if ( command.equals( "query" ) || ( command.equals( "q" ) ) )
        {
            return new QueryCommand( client, rest );
        }
        if ( command.equals( "trace" ) || command.equals( "t" ) )
        {
            return new TraceCommand( client, rest );
        }
        if ( command.equals( "reverse" ) || command.equals( "v" ) )
        {
            return new ReverseLookupCommand( client, rest );
        }
        if ( command.equals( "quit") || command.equals( "exit") )
        {
            return new QuitCommand( client, rest );
        }
        else if ( args.length == 1 )
        {
            return new ResolveCommand( client, args );
        }
        else
        {
            throw new CommandException( "Unrecognized command: " + commandString );
        }
    }
}