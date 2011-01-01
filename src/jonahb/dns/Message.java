package jonahb.dns;

import java.net.*;
import java.io.*;
import java.util.Vector;

public class Message extends MessageBase
{
    // sections
    private Vector<Question> questions;
    private Vector<ResourceRecord> answers;
    private Vector<ResourceRecord> authorities;
    private Vector<ResourceRecord> additionals;

    public Message()
    {
        this.questions = new Vector<Question>();
        this.answers = new Vector<ResourceRecord>();
        this.authorities = new Vector<ResourceRecord>();
        this.additionals = new Vector<ResourceRecord>();
    }

    public static Message parse( byte[] bytes ) throws ParseException
    {
        return parse( new Parser( bytes, 0 ) );
    }

    public static Message parse( Parser parser ) throws ParseException
    {
        Message message = new Message();

        MessageBase.parseHeader( parser, message );

        for ( int i = 0; i < message.questionCount; ++i )
        {
            message.getQuestions().add( Question.parse( parser ) );
        }

        for ( int i = 0; i < message.answerCount; ++i )
        {
            message.getAnswers().add( ResourceRecord.parse( parser ) );
        }

        for ( int i = 0; i < message.authorityCount; ++i )
        {
            message.getAuthorities().add( ResourceRecord.parse( parser ) );
        }

        for ( int i = 0; i < message.additionalCount; ++i )
        {
            message.getAdditionals().add( ResourceRecord.parse( parser ) );
        }

        return message;
    }

    public Question getQuestion()
    {
        return this.questions.get( 0 );
    }

    public void write( OutputStream stream ) throws IOException
    {
        MessageBase.writeHeader( stream, this );

        for ( Question question : this.questions )
        {
            question.write( stream );
        }

        for ( ResourceRecord answer : this.answers )
        {
            answer.write( stream );
        }

        for ( ResourceRecord authority : this.authorities )
        {
            authority.write( stream );
        }

        for ( ResourceRecord additional : this.additionals )
        {
            additional.write( stream );
        }
    }

    public Vector<Question> getQuestions()
    {
        return questions;
    }

    public Vector<ResourceRecord> getAnswers()
    {
        return answers;
    }

    public Vector<ResourceRecord> getAuthorities()
    {
        return authorities;
    }

    public Vector<ResourceRecord> getAdditionals()
    {
        return additionals;
    }

    @Override
    protected int getQuestionCount()
    {
        return questions.size();
    }

    @Override
    protected int getAnswerCount()
    {
        return answers.size();
    }

    @Override
    protected int getAuthorityCount()
    {
        return authorities.size();
    }

    @Override
    protected int getAdditionalCount()
    {
        return additionals.size();
    }    
}