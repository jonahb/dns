package jonahb.dns;

import java.nio.charset.Charset;

public class Parser
{
    private byte[] bytes;
    private int offset;
    private final String DEFAULT_CHARSET_NAME = "US-ASCII";

    public Parser( byte[] bytes, int offset )
    {
        this.bytes = bytes;
        this.offset = offset;
    }

    public Parser( Parser original, int offset )
    {
        this.bytes = original.getBytes();
        this.offset = offset;
    }

    @Override
    public Parser clone()
    {
        return new Parser( this.bytes, this.offset );
    }

    public String parseString( int length ) throws ParseException
    {
        return parseString( length, Charset.forName( DEFAULT_CHARSET_NAME ) );
    }

    public String parseString( int length, Charset charset ) throws ParseException
    {
        if ( length < 0 )
        {
            throw new IllegalArgumentException( "length must be non-negative" );
        }

        if ( offset + length > bytes.length )
        {
            throw new ParseException( "Underflow parsing String" );
        }

        String val = new String( bytes, offset, length, charset );

        offset += length;

        return val;
    }

    public int parseByte() throws ParseException
    {
        if ( offset + 1 > bytes.length )
        {
            throw new ParseException( "Underflow parsing byte" );
        }

        int val = 0xff & (int)bytes[ offset ];
        offset += 1;

        return val;
    }

    public byte[] parseBytes( int length ) throws ParseException
    {
        if ( length <= 0 )
        {
            throw new IllegalArgumentException( "length must be positive" );
        }

        if ( offset + length > bytes.length )
        {
            throw new ParseException( "Underflow parsing Bytes" );
        }

        byte[] val = new byte[ length ];

        for ( int i = 0; i < length; ++i )
        {
            val[ i ] = bytes[ offset + i ];
        }

        offset += length;

        return val;
    }

    public int parseUnsignedShort() throws ParseException
    {
        if ( offset + 2 > bytes.length )
        {
            throw new ParseException( "Underflow parsing unsigned short" );
        }

        int val =  ( ( 0xff & bytes[ offset ] ) << 8 ) | ( 0xff & bytes[ offset + 1 ] );
        offset += 2;

        return val;
    }

    public int parseInt() throws ParseException
    {
        if ( offset + 4 > bytes.length )
        {
            throw new ParseException( "Underflow parsing int" );
        }

        int val = ( 0xff & bytes[offset] ) << 24 |
                  ( 0xff & bytes[offset + 1] ) << 16 |
                  ( 0xff & bytes[offset + 2] ) << 8 |
                  ( 0xff & bytes[offset + 3] );

        offset += 4;

        return val;
    }

    public byte[] getBytes()
    {
        return this.bytes;
    }

    public int getOffset()
    {
        return this.offset;
    }

    public boolean EOF()
    {
        return ( offset >= bytes.length );
    }
}
