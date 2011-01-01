package jonahb.dns.resolvers;

import jonahb.dns.*;
import jonahb.dns.resolvers.Resolver;
import java.net.InetAddress;
import java.util.Vector;
import jonahb.dns.resourcerecords.*;
import java.io.IOException;

public abstract class AbstractResolver implements Resolver
{
    public InetAddress[] hostNameToHostAddress( DomainName hostName ) throws IOException, DNSException
    {
        Message response = this.query( hostName, QueryType.A, QueryClass.INTERNET  );

        Vector<InetAddress> addresses = new Vector<InetAddress>();

        for ( ResourceRecord rr : response.getAnswers() )
        {
            if ( AddressResourceRecord.class.isAssignableFrom( rr.getClass() ) )
            {
                AddressResourceRecord arr = (AddressResourceRecord)rr;
                addresses.add( arr.getAddress() );
            }
        }

        if ( addresses.isEmpty() )
        {
            throw new DataNotFoundException( "Data not found" );
        }

        return addresses.toArray( new InetAddress[ 0 ] );
    }

    public DomainName hostAddressToHostName( InetAddress hostAddress ) throws IOException, DNSException
    {
        throw new java.lang.UnsupportedOperationException();
    }
}
