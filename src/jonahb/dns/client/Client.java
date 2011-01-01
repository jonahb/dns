package jonahb.dns.client;

import jonahb.dns.*;
import java.io.*;
import java.net.*;

public class Client
{
    private boolean quitting = false;

    // settings
    private NameServer nameServer = null;
    private QueryType queryType = QueryType.A;
    private QueryClass queryClass = QueryClass.INTERNET;

    public Client( NameServer nameServer )
    {
        this.nameServer = nameServer;
    }

    public void run()
    {
        BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

        while ( !this.quitting )
        {
            try
            {
                out().print( String.format( "dns %1$s > ",
                        nameServer.getAddress().toString() + ":" + nameServer.getPort() ) );

                String commandString = in.readLine();
                CommandParser.parse( this, commandString ).execute();
            }
            catch ( Exception e )
            {
                out().println( e.getMessage() );
            }
        }
    }

    public void quit()
    {
        this.quitting = true;
    }

    public PrintStream out()
    {
        return System.out;
    }

    public QueryType getQueryType()
    {
        return queryType;
    }

    public void setQueryType( QueryType queryType )
    {
        this.queryType = queryType;
    }

    public QueryClass getQueryClass()
    {
        return queryClass;
    }

    public void setQueryClass( QueryClass queryClass )
    {
        this.queryClass = queryClass;
    }

    public NameServer getNameServer()
    {
        return nameServer;
    }

    public void setNameServer( NameServer nameServer )
    {
        this.nameServer = nameServer;
    }
}
