package jonahb.dns;

public class ResourceRecordType
{
    private ResourceRecordType() {}

    public static final int A           = 1;
    public static final int NS          = 2;
    public static final int CNAME       = 5;
    public static final int SOA         = 6;
    public static final int WKS         = 11;
    public static final int PTR         = 12;
    public static final int HINFO       = 13;
    public static final int MINFO       = 14;
    public static final int MX          = 15;
    public static final int TXT         = 16;
    public static final int AAAA        = 28;

    public static String toString( int type )
    {
        switch ( type )
        {
            case 1: return "A";
            case 2: return "NS";
            case 5: return "CNAME";
            case 6: return "SOA";
            case 11: return "WKS";
            case 12: return "PTR";
            case 13: return "HINFO";
            case 14: return "MINFO";
            case 15: return "MX";
            case 16: return "TXT";
            case 28: return "AAAA";
            default:
            {
                return "Unknown (" + type + ")";
            }
        }
    }
}