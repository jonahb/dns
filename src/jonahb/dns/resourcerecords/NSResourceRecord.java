package jonahb.dns.resourcerecords;

import jonahb.dns.*;

/*
3.3.11. NS RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   NSDNAME                     /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

NSDNAME         A <domain-name> which specifies a host which should be
                authoritative for the specified class and domain.
*/

public class NSResourceRecord extends ResourceRecord
{
    private DomainName nameServerDomainName = null;

    public NSResourceRecord()
    {
        super();
    }

    @Override
    public String toString()
    {
        return super.toString() + " " + nameServerDomainName.toString();
    }

    public DomainName getNameServerDomainName()
    {
        return nameServerDomainName;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        this.nameServerDomainName = DomainName.parse( parser );
    }
}
