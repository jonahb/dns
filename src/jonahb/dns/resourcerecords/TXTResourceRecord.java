package jonahb.dns.resourcerecords;

import jonahb.dns.*;
import java.net.InetAddress;

/*
3.3.14. TXT RDATA format

    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   TXT-DATA                    /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

TXT-DATA        One or more <character-string>s.

TXT RRs are used to hold descriptive text.  The semantics of the text
depends on the domain where it is found.
*/

public class TXTResourceRecord extends ResourceRecord
{
    private String text = null;

    public TXTResourceRecord()
    {
        super();
    }

    public String getText()
    {
        return text;
    }

    @Override
    public String toString()
    {
        return super.toString() + " " + text;
    }

    @Override
    protected void parseData( Parser parser ) throws ParseException
    {
        int length = parser.parseByte();

        if ( ( length < 0 ) || ( length > 255 ) )
        {
            throw new ParseException( "TXT data length byte must be between 0 and 255" );
        }

        this.text = parser.parseString( length );
    }
}
