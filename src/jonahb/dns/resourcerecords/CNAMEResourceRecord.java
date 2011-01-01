package jonahb.dns.resourcerecords;

import jonahb.dns.*;

/*
3.3.1. CNAME RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                     CNAME                     /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

CNAME           A <domain-name> which specifies the canonical or primary
                name for the owner.  The owner name is an alias.
*/

public class CNAMEResourceRecord extends ResourceRecord
{
    private DomainName canonicalName = null;

    public CNAMEResourceRecord()
    {
        super();
    }

    @Override
    public String toString()
    {
        return super.toString() + " " + canonicalName.toString();
    }

    public DomainName getCanonicalName()
    {
        return canonicalName;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        this.canonicalName = DomainName.parse( parser );
    }
}
