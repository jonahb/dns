package jonahb.dns.io;

import java.io.IOException;

public interface ByteChannel
{
    public byte[] read( int length ) throws IOException;
    public void write( byte[] bytes ) throws IOException;
}