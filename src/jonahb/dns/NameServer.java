package jonahb.dns;
import jonahb.dns.io.*;

import java.net.*;

public class NameServer
{
    private InetAddress address = null;
    private int port = 0;

    public NameServer( InetAddress address )
    {
        this( address, Util.DEFAULT_DNS_PORT );
    }

    public NameServer( InetAddress address, int port )
    {
        if ( port < 0 )
        {
            throw new IllegalArgumentException( "localPort must be non-negative" );
        }

        this.port = port;
        this.address = address;        
    }

    @Override
    public String toString()
    {
        return this.address.toString() + ":" + this.port;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public MessageChannel createMessageChannel()
    {
        return new MessageChannel( new DatagramChannel( address, port ) );        
    }
}