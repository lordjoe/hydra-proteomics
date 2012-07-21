package com.lordjoe.algorithms;

/**
 * com.lordjoe.algorithms.SegwayCRC
 * see http://tech.groups.yahoo.com/group/ETList/message/2390
 *
 * @author Steve Lewis
 * @date 7/15/12
 */
public class SegwayCRC
{
    public static SegwayCRC[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = SegwayCRC.class;


    protected static int crc16(short[] b, int start, int len)
    {
        int i, j, k;
        short c;
        int r = 0;

        for (k = start; k < (start + len); k++) {
            c = b[k];
            for (i = 0; i != 8; c >>= 1, i++) {
                j = (c ^ r) & 1;
                r >>= 1;

                if (j != 0)
                    r ^= 0xa001;
            }
        }
        return r;
    }

    public static final short[] STEVE_RED_KEY_VALUES = {
            0x15, 0x24, 0x10, 0x0C, 0xb7, 0x19, 0x84, 0x2e,
            0x83, 0xfd, 0x76, 0xAA, 0xa3, 0x19, 0x00, 0xb0
    };
    public static final short[] STEVE_BLACK_KEY_VALUES = {
            0x59, 0x34, 0x10, 0x0C, 0xb7, 0x19, 0x84, 0x2e, 0x83,
            0xfd, 0x76, 0xAA, 0xa3, 0x0C, 0x00, 0x50
    };
    public static final short[] STEVE_SLOW_KEY_VALUES = {
            0x59, 0x34, 0x10, 0x0C, 0xb7, 0x19, 0x84, 0x2e, 0x83, 0xfd, 0x76,
            0xAA, 0xa3, 0x06, 0x00, 0x30
    };
    // b0 red key
    public static final short[] STEVE_ZERO_KEY_VALUES = {
            0x66, 0xF4, 0x10, 0x0C, 0xb7, 0x19, 0x84, 0x2e, 0x83, 0xfd, 0x76,
            0xAA, 0xa3, 0x00, 0x00, 0x00
    };


    public static final short[] ORIGINAL_KEY_VALUES = {0x94, 0xb6, 0x15, 0xdc, 0x16, 0x23, 0x05, 0x42, 0x2d,
            0xf1, 0xaf, 0x01, 0x05, 0x19, 0x00, 0xb0};

    /**
     * seehthttp://tech.groups.yahoo.com/group/ETList/message/2390tp://tech.groups.yahoo.com/group/ETList/message/2390
     *
     * @param args
     */
    public static void main(String[] args)
    {
        short buf[] = STEVE_ZERO_KEY_VALUES; //ORIGINAL_KEY_VALUES;
        int i, crc;
        short p1, p2, p3;

//        if (args.length 2) {
//            printf("%s [translate velocity] [00] [rotate velocity]\n", args[0]);
//          }
//
//        sscanf(args[1], "%x", & p1);
//        sscanf(args[2], "%x", & p2);
//        sscanf(args[3], "%x", & p3);
//
//        buf[13] = p1;
//        buf[14] = p2;
//        buf[15] = p3;

        crc = ~crc16(buf, 2, 14);
        buf[0] = (short) ((crc & 0xff00) >> 8);
        buf[1] = (short) (crc & 0xff);

        for (i = 0; i < 16; i++)
            System.out.printf("%x ", buf[i]);

        System.out.println();
    }

}
