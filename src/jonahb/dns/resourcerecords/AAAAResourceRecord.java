package jonahb.dns.resourcerecords;

import jonahb.dns.*;
import java.net.InetAddress;

/*
(from RFC 1886)

2.2 AAAA data format

   A 128 bit IPv6 address is encoded in the data portion of an AAAA
   resource record in network byte order (high-order byte first).
*/

public class AAAAResourceRecord extends AddressResourceRecord
{
    private InetAddress address = null;

    public AAAAResourceRecord()
    {
        super();
    }

    public InetAddress getAddress()
    {
        return address;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        try
        {
            address = InetAddress.getByAddress( parser.parseBytes( 16 ) );
        }
        catch ( java.net.UnknownHostException e )
        {
            // only thrown if IP address is not length 16, which
            // we checked for; so do nothing
        }
    }
}
