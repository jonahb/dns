package jonahb.dns.io;

import jonahb.dns.*;
import java.net.*;
import java.io.IOException;

public class DatagramChannel implements ByteChannel
{
    private int localPort;
    private boolean localPortSpecified = false;
    private int destPort;
    private InetAddress destAddress;
    private int timeoutMilliseconds = 0;

    private static final int DEFAULT_TIMEOUT_MILLISECONDS = 10000;

    private DatagramSocket socket;

    public DatagramChannel( InetAddress destAddress, int destPort )
    {
        this.destAddress = destAddress;
        this.destPort = destPort;
    }

    public DatagramChannel( InetAddress destAddress, int destPort, int localPort )
    {
        this( destAddress, destPort, localPort, DEFAULT_TIMEOUT_MILLISECONDS );
    }

    public DatagramChannel( InetAddress destAddress, int destPort, int localPort, int timeoutMilliseconds )
    {
        if ( localPort < 0 )
        {
            throw new IllegalArgumentException( "localPort must be non-negative" );
        }
        if ( timeoutMilliseconds < 0 )
        {
            throw new IllegalArgumentException( "timeoutMilliseconds must be non-negative" );
        }

        this.localPortSpecified = true;
        this.destAddress = destAddress;
        this.destPort = destPort;
        this.localPort = localPort;
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    public byte[] read( int length ) throws IOException
    {
        if ( length < 1 )
        {
            throw new IllegalArgumentException( "length must be positive" );
        }

        byte[] buf = new byte[ length ];
        DatagramPacket packet = new DatagramPacket( buf, buf.length );

        this.ensureSocket();

        // blocks; fills buf
        socket.receive( packet );
        return packet.getData();
    }

    public void write( byte[] bytes ) throws IOException
    {
        this.ensureSocket();

        DatagramPacket packet = new DatagramPacket( bytes, bytes.length, destAddress, destPort );
        socket.send( packet );        
    }

    private void ensureSocket() throws SocketException
    {
        if ( socket == null )
        {
            if ( this.localPortSpecified )
            {
                socket = new DatagramSocket( localPort );
            }
            else
            {
                socket = new DatagramSocket();
            }

            socket.setSoTimeout( timeoutMilliseconds );
        }            
    }
}
