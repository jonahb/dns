package jonahb.dns.resourcerecords;

import jonahb.dns.*;
import java.net.InetAddress;

public abstract class AddressResourceRecord extends ResourceRecord
{
    @Override
    public String toString()
    {
        return super.toString() + " " + getAddress().toString();
    }

    public abstract InetAddress getAddress();
}
