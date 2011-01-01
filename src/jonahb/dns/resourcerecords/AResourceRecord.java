package jonahb.dns.resourcerecords;

import jonahb.dns.*;
import java.net.InetAddress;

/*
3.4.1. A RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ADDRESS                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

ADDRESS         A 32 bit Internet address.
*/

public class AResourceRecord extends AddressResourceRecord
{
    private InetAddress address = null;

    public AResourceRecord()
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
            address = InetAddress.getByAddress( parser.parseBytes( 4 ) );
        }
        catch ( java.net.UnknownHostException e )
        {
            // only thrown if IP address is not length 4, which
            // we checked for; so do nothing
        }
    }
}
