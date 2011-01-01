package jonahb.dns.io;

import java.io.IOException;
import jonahb.dns.Message;
import jonahb.dns.Util;
import jonahb.dns.ParseException;

public class MessageChannel implements Channel
{
    private ByteChannel byteChannel;

    public MessageChannel( ByteChannel byteChannel )
    {
        this.byteChannel = byteChannel;
    }

    public Message read() throws IOException, ParseException
    {
        return Message.parse( byteChannel.read( Util.DATAGRAM_PACKET_MAX_BYTES ) );
    }

    public void write( Message message ) throws IOException, ParseException
    {
        byteChannel.write( message.toByteArray() );
    }
}