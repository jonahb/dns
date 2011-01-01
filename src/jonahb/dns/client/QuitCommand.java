package jonahb.dns.client;

public class QuitCommand extends Command
{
    public QuitCommand( Client client, String[] args ) throws CommandException
    {
        super( client, args );
    }

    @Override
    public void execute()
    {
        client.quit();
    }
}
