package jonahb.dns.resolvers;

import jonahb.dns.*;
import jonahb.dns.resourcerecords.*;
import jonahb.dns.io.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.util.Vector;

// A stub resolver forwards requests to another resolver

public class StubResolver extends AbstractResolver
{
    private MessageChannel nameServerChannel;
    private NameServer nameServer;
    private boolean requestRecursion = true;

    public StubResolver( NameServer nameServer )
    {
        this( nameServer, true );
    }

    public StubResolver( NameServer nameServer, boolean requestRecursion )
    {
        this.nameServer = nameServer;
        this.nameServerChannel = nameServer.createMessageChannel();
        this.requestRecursion = requestRecursion;
    }

    public Message query( DomainName domainName, QueryType queryType, QueryClass queryClass ) throws IOException, DNSException
    {
        Message query = new Message();
        query.setRecursionDesired( requestRecursion );
        query.getQuestions().add( new Question( domainName, queryType, queryClass ) );

        nameServerChannel.write( query );
        Message response = nameServerChannel.read();

        response.getResponseCode().throwIfError();

        if ( response.query() )
        {
            throw new QueryException( "Bad data received from server: Response bit inccorectly set" );
        }

        if ( requestRecursion && !response.recursionAvailable() )
        {
            throw new QueryException( "Name server '" + nameServer.toString() + "' does not support recursive queries"); 
        }

        return response;
    }

    @Override
    public DomainName hostAddressToHostName( InetAddress address ) throws IOException, DNSException
    {
        if ( !Inet4Address.class.isAssignableFrom( address.getClass() ) )
        {
            throw new IllegalArgumentException( "StubResolver.HostAddressToHostName only supports IPv4 addresses" );
        }

        DomainName addressDomainName = createHostAddressToHostNameDomain( (Inet4Address)address );

        Message response = this.query( addressDomainName, QueryType.PTR, QueryClass.INTERNET );

        for ( ResourceRecord rr : response.getAnswers() )
        {
            if ( PTRResourceRecord.class.isAssignableFrom( rr.getClass() ) )
            {
                return ((PTRResourceRecord)rr).getDomainName();
            }
        }

        throw new DataNotFoundException( "Data not found" );
    }

    private DomainName createHostAddressToHostNameDomain( Inet4Address address )
    {
        DomainName name = new DomainName();
        Vector<String> labels = name.getLabels();

        byte[] addressBytes = address.getAddress();

        for ( int i = addressBytes.length - 1; i >= 0; --i )
        {
            labels.add( String.valueOf( 0xff & addressBytes[ i ] ) );
        }

        labels.add( "IN-ADDR" );
        labels.add( "ARPA" );

        return name;
    }

    public boolean getRequestRecursion()
    {
        return requestRecursion;
    }

    public void setRequestRecursion( boolean requestRecursion )
    {
        this.requestRecursion = requestRecursion;
    }
}
