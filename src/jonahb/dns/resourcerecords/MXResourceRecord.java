package jonahb.dns.resourcerecords;

import jonahb.dns.*;
import java.net.InetAddress;

/*
3.3.9. MX RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                  PREFERENCE                   |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   EXCHANGE                    /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

PREFERENCE      A 16 bit integer which specifies the preference given to
                this RR among others at the same owner.  Lower values
                are preferred.

EXCHANGE        A <domain-name> which specifies a host willing to act as
                a mail exchange for the owner name.

MX records cause type A additional section processing for the host
specified by EXCHANGE.  The use of MX RRs is explained in detail in
[RFC-974].
*/

public class MXResourceRecord extends ResourceRecord
{
    private int preference = 0;
    private DomainName exchange = null;

    @Override
    public String toString()
    {
        return super.toString() + " " + preference + " " + exchange.toString();
    }

    public DomainName getExchange()
    {
        return exchange;
    }

    public int getPreference()
    {
        return preference;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        this.preference = parser.parseUnsignedShort();
        this.exchange = DomainName.parse( parser );
    }
}
