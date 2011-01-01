package jonahb.dns;

import java.util.Vector;
import java.nio.charset.Charset;
import java.io.*;
import java.util.regex.*;
import java.util.List;

public class DomainName
{
    private Vector<String> labels;
    private static Pattern domainNamePattern;
    private static Pattern labelPattern;
    private static final int MAX_LABEL_LENGTH = 63;

    public DomainName()
    {
        this.labels = new Vector<String>();
    }

    static
    {
        domainNamePattern = Pattern.compile( "^[a-zA-z]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-z]([a-zA-Z0-9-]*[a-zA-Z0-9])?)*$" );
        labelPattern = Pattern.compile( "[a-zA-z]([a-zA-Z0-9-]*[a-zA-Z0-9])?" );
    }

    public static DomainName parse( String s ) throws IllegalArgumentException
    {
        if ( s == null )
        {
            throw new NullPointerException();
        }

        if ( !domainNamePattern.matcher( s ).matches() )
        {
            throw new IllegalArgumentException( "Malformatted domain name" );
        }

        DomainName name = new DomainName();
        Matcher labelMatcher = labelPattern.matcher( s );

        while ( labelMatcher.find() )
        {
            String label = labelMatcher.group();

            // we should instead check when the label is added to labels
            if ( label.length() > MAX_LABEL_LENGTH )
            {
                throw new IllegalArgumentException( "Domain name contains label longer than 63 characters" );
            }

            name.getLabels().add( labelMatcher.group() );
        }

        return name;
    }

    public static DomainName parse( Parser parser ) throws ParseException
    {
        DomainName name = new DomainName();

        while ( !parser.EOF() )
        {
            int header = parser.parseByte();

            // terminal byte
            if ( header == 0 )
            {
                break;
            }

            // pointer
            else if ( ( header & 0xC0 ) != 0 )
            {
                // take low 14 bits
                int pointer = ~0xC000 & ( ( header << 8 ) | parser.parseByte() );

                DomainName suffix  = DomainName.parse( new Parser( parser, pointer ) );
                name.getLabels().addAll( suffix.getLabels() );

                break;
            }

            // length
            else
            {
                name.getLabels().add( parser.parseString( header ) );
            }
        }

        return name;
    }

    public void write( OutputStream stream ) throws IOException
    {
        for ( String label : labels )
        {
            stream.write( label.length() );
            stream.write( label.getBytes( Charset.forName( "US-ASCII" ) ) );
        }

        stream.write( 0 );
    }

    public Vector<String> getLabels()
    {
        return this.labels;
    }

    // returns zones in descending order of generality
    // e.g cs.columbia.edu results in:
    // edu
    // columbia.edu
    // cs.columbia.edu

    public List<DomainName> getZones()
    {
        Vector<DomainName> zones = new Vector<DomainName>();

        int labelCount = this.getLabels().size();

        for ( int i = labelCount - 1; i >= 0; --i  )
        {
            DomainName zone = new DomainName();
            zones.add( zone );

            for ( int j = i; j < labelCount; ++j )
            {
                zone.labels.add( this.labels.get( j ) );
            }
        }

        return zones;
    }

    @Override
    public boolean equals( Object other )
    {
        if ( other == null )
        {
            return false;
        }
        else if ( other.getClass() != this.getClass() )
        {
            return false;
        }
        else
        {
            return labels.equals( ((DomainName)other).getLabels() );
        }
    }

    @Override
    public String toString()
    {
        int size = this.labels.size();

        if ( size == 0 )
        {
            return "[root]";
        }
        else
        {
            StringBuffer buf = new StringBuffer();

            for ( int i = 0; i < size; ++i )
            {
                buf.append( this.labels.get( i ) );

                if ( i < size - 1 )
                {
                    buf.append( "." );
                }
            }

            return buf.toString();
        }
    }
}
