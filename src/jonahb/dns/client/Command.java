package jonahb.dns.client;

import jonahb.dns.*;

public abstract class Command
{
    protected Client client;

    public Command( Client client, String[] args )
    {
        this.client = client;
    }

    public abstract void execute() throws Exception;    
}