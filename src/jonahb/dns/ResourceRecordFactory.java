package jonahb.dns;

import jonahb.dns.resourcerecords.*;

public class ResourceRecordFactory
{
    static ResourceRecord create( int type )
    {
        switch ( type )
        {
            case ResourceRecordType.A:      return new AResourceRecord();
            case ResourceRecordType.NS:     return new NSResourceRecord();
            case ResourceRecordType.CNAME:  return new CNAMEResourceRecord();
            case ResourceRecordType.AAAA:   return new AAAAResourceRecord();
            case ResourceRecordType.TXT:    return new TXTResourceRecord();
            case ResourceRecordType.MX:     return new MXResourceRecord();
            case ResourceRecordType.PTR:    return new PTRResourceRecord();
            default:                        return new GenericResourceRecord();
        }
    }
}
