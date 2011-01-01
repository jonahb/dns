package jonahb.dns.resolvers;

import jonahb.dns.*;
import java.net.InetAddress;
import java.io.IOException;

public interface Resolver
{
    public InetAddress[] hostNameToHostAddress( DomainName hostName ) throws IOException, DNSException;
    public DomainName hostAddressToHostName( InetAddress hostAddress ) throws IOException, DNSException;
    public Message query( DomainName domainName, QueryType queryType, QueryClass queryClass ) throws IOException, DNSException;
}
