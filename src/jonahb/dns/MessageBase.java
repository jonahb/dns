package jonahb.dns;

import java.net.*;
import java.io.*;

public abstract class MessageBase
{
    // headers
    private int id                          = 0;
    private boolean query                   = true;
    private int opCode                      = OpCode.STANDARD_QUERY;
    private boolean answerAuthoritative     = false;
    private boolean truncated               = false;
    private boolean recursionDesired        = false;
    private boolean recursionAvailable      = false;
    private ResponseCode responseCode       = ResponseCode.NO_ERROR;

    // these are set by readHeader, but we do not maintain their
    // equality with the lengths of corresponding Vectors
    protected int questionCount               = 0;
    protected int answerCount                 = 0;
    protected int authorityCount              = 0;
    protected int additionalCount             = 0;

    public abstract void write( OutputStream out ) throws IOException;

    public abstract Question getQuestion();

    public byte[] toByteArray() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {
            this.write( out );
            return out.toByteArray();
        }
        finally
        {
            out.close();
        }
    }

    public boolean recursionDesired()
    {
        return recursionDesired;
    }

    public boolean recursionAvailable()
    {
        return recursionAvailable;
    }

    public int getId()
    {
        return id;
    }

    public boolean query()
    {
        return query;
    }

    public int getOpCode()
    {
        return opCode;
    }

    public ResponseCode getResponseCode()
    {
        return responseCode;
    }

    public boolean answerAuthoritative()
    {
        return answerAuthoritative;
    }

    public boolean truncated()
    {
        return truncated;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public void setQuery( boolean query )
    {
        this.query = query;
    }

    public void setOpCode( int opCode )
    {
        this.opCode = opCode;
    }

    public void setAnswerAuthoritative( boolean answerAuthoritative )
    {
        this.answerAuthoritative = answerAuthoritative;
    }

    public void setTruncated( boolean truncated )
    {
        this.truncated = truncated;
    }

    public void setRecursionDesired( boolean recursionDesired )
    {
        this.recursionDesired = recursionDesired;
    }

    public void setRecursionAvailable( boolean recursionAvailable )
    {
        this.recursionAvailable = recursionAvailable;
    }

    public void setResponseCode( ResponseCode responseCode )
    {
        this.responseCode = responseCode;
    }

    protected int getQuestionCount()
    {
        return questionCount;
    }

    protected int getAnswerCount()
    {
        return answerCount;
    }

    protected int getAuthorityCount()
    {
        return authorityCount;
    }

    protected int getAdditionalCount()
    {
        return additionalCount;
    }    

    /*
                                    1  1  1  1  1  1
      0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                      ID                       |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    QDCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ANCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    NSCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ARCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

    */

    // mutates message
    protected static void parseHeader( Parser parser, MessageBase message  ) throws ParseException
    {
        message.id                          = parser.parseUnsignedShort();
        int fields                          = parser.parseUnsignedShort();
        message.questionCount               = parser.parseUnsignedShort();
        message.answerCount                 = parser.parseUnsignedShort();
        message.authorityCount              = parser.parseUnsignedShort();
        message.additionalCount             = parser.parseUnsignedShort();

        message.query                       = ( 0 == (fields & 0x8000) );
        message.answerAuthoritative         = ( 0 != (fields & 0x400) );
        message.truncated                   = ( 0 != (fields & 0x200) );
        message.recursionDesired            = ( 0 != (fields & 0x100) );
        message.recursionAvailable          = ( 0 != (fields & 0x80) );

        message.responseCode                = new ResponseCode( fields & 0xf );
        message.opCode                      = (fields & 7800) >>> 11;
    }

    protected static void writeHeader( OutputStream stream, MessageBase message  ) throws IOException
    {
        DataOutputStream dos = new DataOutputStream( stream );

        int fields = 0;

        if ( !message.query )               fields |= ( 1 << 15 );
        if ( message.answerAuthoritative )  fields |= ( 1 << 10 );
        if ( message.truncated )            fields |= ( 1 << 9 );
        if ( message.recursionDesired )     fields |= ( 1 << 8 );
        if ( message.recursionAvailable )   fields |= ( 1 << 7 );
        fields |= message.opCode << 11;
        fields |= message.responseCode.getCode();

        dos.writeShort( message.id );
        dos.writeShort( fields );
        dos.writeShort( message.getQuestionCount() );
        dos.writeShort( message.getAnswerCount() );
        dos.writeShort( message.getAuthorityCount() );
        dos.writeShort( message.getAdditionalCount() );
    }
}