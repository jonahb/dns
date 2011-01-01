package jonahb.dns;

import java.io.*;

public abstract class ResourceRecord
{
    private DomainName domainName = null;
    private int type;
    private int klass = 0;
    private int ttl = 0;
    private int dataLength = 0;
    private byte[] data = new byte[ 0 ];

    public ResourceRecord()
    {
    }

    public static ResourceRecord parse( Parser parser ) throws ParseException
    {
        DomainName name = DomainName.parse( parser );
        int type = parser.parseUnsignedShort();

        // now using type, instantiate a concrete class
        ResourceRecord rr = ResourceRecordFactory.create( type );

        rr.setDomainName( name );
        rr.setType( type );
        rr.setKlass( parser.parseUnsignedShort() );
        rr.setTTL( parser.parseInt() );
        rr.setDataLength( parser.parseUnsignedShort() );

        // spawn a new parser for inheritors to parse the data
        // we can't just give them the byte array because they need
        // access to the whole packet (i.e. for domain name references)
        Parser dataParser = parser.clone();
        rr.parseData( dataParser );

        rr.setData( parser.parseBytes( rr.getDataLength() ) );

        return rr;
    }

    // called during parse; inheritors should override
    protected void parseData( Parser parser ) throws ParseException
    {
    }

    @Override
    public String toString()
    {
        return this.domainName.toString() + " " + ResourceRecordType.toString( this.type );
    }

    public void write( OutputStream stream ) throws IOException
    {
        this.domainName.write( stream );

        DataOutputStream dos = new DataOutputStream( stream );

        dos.writeShort( this.type );
        dos.writeShort( this.klass );
        dos.writeInt( this.ttl );
        dos.writeShort( this.dataLength );
        dos.write( this.data );
    }

    public DomainName getDomainName()
    {
        return this.domainName;
    }

    public int getType()
    {
        return this.type;
    }

    public int getKlass()
    {
        return this.klass;
    }

    public int getTTL()
    {
        return this.ttl;
    }

    public int getDataLength()
    {
        return this.dataLength;
    }

    public byte[] getData()
    {
        return this.data;
    }

    public void setDomainName( DomainName domainName )
    {
        this.domainName = domainName;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    public void setKlass( int klass )
    {
        this.klass = klass;
    }

    public void setTTL( int ttl )
    {
        this.ttl = ttl;
    }

    public void setDataLength( int dataLength )
    {
        this.dataLength = dataLength;
    }

    public void setData( byte[] data )
    {
        this.data = data;
    }
}
