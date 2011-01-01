package jonahb.dns;

import java.io.*;

public class Question
{
    private DomainName domainName = null;
    private QueryType queryType;
    private QueryClass queryClass;

    public Question()
    {
    }

    public Question( DomainName domainName, QueryType queryType )
    {
        this( domainName, queryType, QueryClass.INTERNET );
    }

    public Question( DomainName domainName, QueryType queryType, QueryClass queryClass )
    {
        this.domainName = domainName;
        this.queryType = queryType;
        this.queryClass = queryClass;
    }

    public static Question parse( Parser parser ) throws ParseException
    {
        Question question = new Question();

        question.setDomainName( DomainName.parse( parser ) );
        question.setQueryType( new QueryType( parser.parseUnsignedShort() ) );
        question.setQueryClass( new QueryClass( parser.parseUnsignedShort() ) );

        return question;
    }

    public void write( OutputStream stream ) throws IOException
    {
        this.domainName.write( stream );

        DataOutputStream dos = new DataOutputStream( stream );
        dos.writeShort( this.queryType.getCode() );
        dos.writeShort( this.queryClass.getCode() );
    }

    public DomainName getDomainName()
    {
        return this.domainName;
    }

    public QueryType getQueryType()
    {
        return this.queryType;
    }

    public QueryClass getQueryClass()
    {
        return this.queryClass;
    }

    public void setDomainName( DomainName domainName )
    {
        this.domainName = domainName;
    }

    public void setQueryType( QueryType queryType )
    {
        this.queryType = queryType;
    }

    public void setQueryClass( QueryClass queryClass )
    {
        this.queryClass = queryClass;
    }

}
