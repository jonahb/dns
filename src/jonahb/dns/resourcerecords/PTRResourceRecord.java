package jonahb.dns.resourcerecords;

import jonahb.dns.*;

/*
3.3.12. PTR RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   PTRDNAME                    /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

PTRDNAME        A <domain-name> which points to some location in the
                domain name space.
*/

public class PTRResourceRecord extends ResourceRecord
{
    private DomainName domainName = null;

    public DomainName getDomainName()
    {
        return domainName;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        domainName = DomainName.parse( parser );
    }
}
