package old.MD4;

import java.util.ArrayList;


public class MD4 {
//    private static int[] X = new int[16];

    public static byte[] hash(byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (byte b : input) {
            sb.append(String.format("%02X ", b));
        }

        ArrayList<Byte> Mlist = new ArrayList<>();

        for (byte value : input) {
            Mlist.add(value);
        }

        int bufferNdx = Mlist.size() % 64;
        int padLen = (bufferNdx < 56) ? (56 - bufferNdx) : (120 - bufferNdx);
        long prePaddingLength = Mlist.size();

        byte[] tail = new byte[padLen + 8];
        tail[0] = (byte) 0x80;

        for (int i = 0; i < 8; i++)
            tail[padLen + i] = (byte) ((prePaddingLength * 8) >>> (8 * i));


        for (byte b : tail) Mlist.add(b);


        byte[] result = new byte[16];

        int[] context = new int[4];
        context[0] = 0x67452301;
        context[1] = 0xEFCDAB89;
        context[2] = 0x98BADCFE;
        context[3] = 0x10325476;

        //do rounds
        int[] X = new int[16];

        for (int k = 0; k < Mlist.size(); ) {
            for (int i = 0; i < 16; i++)
                X[i] = (Mlist.get(k++) & 0xFF) |
                        (Mlist.get(k++) & 0xFF) << 8 |
                        (Mlist.get(k++) & 0xFF) << 16 |
                        (Mlist.get(k++) & 0xFF) << 24;

            int A = context[0];
            int B = context[1];
            int C = context[2];
            int D = context[3];

            A = FF(A, B, C, D, X[0], 3);
            D = FF(D, A, B, C, X[1], 7);
            C = FF(C, D, A, B, X[2], 11);
            B = FF(B, C, D, A, X[3], 19);
            A = FF(A, B, C, D, X[4], 3);
            D = FF(D, A, B, C, X[5], 7);
            C = FF(C, D, A, B, X[6], 11);
            B = FF(B, C, D, A, X[7], 19);
            A = FF(A, B, C, D, X[8], 3);
            D = FF(D, A, B, C, X[9], 7);
            C = FF(C, D, A, B, X[10], 11);
            B = FF(B, C, D, A, X[11], 19);
            A = FF(A, B, C, D, X[12], 3);
            D = FF(D, A, B, C, X[13], 7);
            C = FF(C, D, A, B, X[14], 11);
            B = FF(B, C, D, A, X[15], 19);

            A = GG(A, B, C, D, X[0], 3);
            D = GG(D, A, B, C, X[4], 5);
            C = GG(C, D, A, B, X[8], 9);
            B = GG(B, C, D, A, X[12], 13);
            A = GG(A, B, C, D, X[1], 3);
            D = GG(D, A, B, C, X[5], 5);
            C = GG(C, D, A, B, X[9], 9);
            B = GG(B, C, D, A, X[13], 13);
            A = GG(A, B, C, D, X[2], 3);
            D = GG(D, A, B, C, X[6], 5);
            C = GG(C, D, A, B, X[10], 9);
            B = GG(B, C, D, A, X[14], 13);
            A = GG(A, B, C, D, X[3], 3);
            D = GG(D, A, B, C, X[7], 5);
            C = GG(C, D, A, B, X[11], 9);
            B = GG(B, C, D, A, X[15], 13);

            A = HH(A, B, C, D, X[0], 3);
            D = HH(D, A, B, C, X[8], 9);
            C = HH(C, D, A, B, X[4], 11);
            B = HH(B, C, D, A, X[12], 15);
            A = HH(A, B, C, D, X[2], 3);
            D = HH(D, A, B, C, X[10], 9);
            C = HH(C, D, A, B, X[6], 11);
            B = HH(B, C, D, A, X[14], 15);
            A = HH(A, B, C, D, X[1], 3);
            D = HH(D, A, B, C, X[9], 9);
            C = HH(C, D, A, B, X[5], 11);
            B = HH(B, C, D, A, X[13], 15);
            A = HH(A, B, C, D, X[3], 3);
            D = HH(D, A, B, C, X[11], 9);
            C = HH(C, D, A, B, X[7], 11);
            B = HH(B, C, D, A, X[15], 15);

            context[0] += A;
            context[1] += B;
            context[2] += C;
            context[3] += D;
        }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result[i * 4 + j] = (byte) (context[i] >>> (8 * j));

        return result;
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    //aux functions
    private static int FF(int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & c) | (~b & d)) + x;
        return t << s | t >>> (32 - s);
    }

    private static int GG(int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & (c | d)) | (c & d)) + x + 0x5A827999;
        return t << s | t >>> (32 - s);
    }

    private static int HH(int a, int b, int c, int d, int x, int s) {
        int t = a + (b ^ c ^ d) + x + 0x6ED9EBA1;
        return t << s | t >>> (32 - s);
    }
}
