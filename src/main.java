import bitlib.BitUtils;
import cypher.MD4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class main {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        var cyph = new MD4();
//        var res1 = cyph.digest("The quick brown fox jumps over the lazy dogshdkasjhflksdjhflkasjhdflkjshdfkljasdhlfkjajhjhhldfkjashldf".getBytes());
        var res2 = old.MD4.MD4.hash("The quick brown fox jumps over the lazy dogshdkasjhflksdjhflkasjhdflkjshdfkljasdhlfkjajhjhhldfkjashldf".getBytes());
//        var res1 = cyph.digest("".getBytes());

        System.out.println(encodeHexString(res2));
//        System.out.println(new BitUtils(res1).getNextLong(25) + "\n");

        System.exit(0);

        var secPreimg8 = secondPreimage("test", 8, 1000);
        System.out.println("Second preimage avg over 8 bits: " + secPreimg8);
        dumpData("preimg.txt", true, "8 " + secPreimg8 + "\n");

        var secPreimg12 = secondPreimage("test", 12, 1000);
        System.out.println("Second preimage avg over 12 bits: " + secPreimg12);
        dumpData("preimg.txt", false, "12 " + secPreimg12 + "\n");

        var secPreimg16 = secondPreimage("test", 16, 500);
        System.out.println("Second preimage avg over 16 bits: " + secPreimg16);
        dumpData("preimg.txt", false, "16 " + secPreimg16 + "\n");

        var secPreimg20 = secondPreimage("test", 20, 50);
        System.out.println("Second preimage avg over 20 bits: " + secPreimg20);
        dumpData("preimg.txt", false, "20 " + secPreimg20 + "\n");

        var secPreimg24 = secondPreimage("test", 24, 10);
        System.out.println("Second preimage avg over 24 bits: " + secPreimg24);
        dumpData("preimg.txt", false, "24 " + secPreimg24 + "\n");

        var coll8 = collisions(8, 1000);
        System.out.println("\nCollisions for 8 bits: " + coll8);
        dumpData("coll.txt", true, "8 " + coll8 + "\n");

        var coll12 = collisions(12, 1000);
        System.out.println("Collisions for 12 bits: " + coll12);
        dumpData("coll.txt", false, "12 " + coll12 + "\n");

        var coll16 = collisions(16, 1000);
        System.out.println("Collisions for 16 bits: " + coll16);
        dumpData("coll.txt", false, "16 " + coll16 + "\n");

        var coll20 = collisions(20, 1000);
        System.out.println("Collisions for 20 bits: " + coll20);
        dumpData("coll.txt", false, "20 " + coll20 + "\n");

        var coll24 = collisions(24, 1000);
        System.out.println("Collisions for 24 bits: " + coll24);
        dumpData("coll.txt", false, "24 " + coll24 + "\n");
    }

    public static void dumpData(String fileName, boolean replace, String data) {
        try {
            FileWriter fw = new FileWriter(fileName, !replace); //the true will append the new data
            fw.write(data);//appends the string to the file
            fw.close();
        } catch (
                IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    public static long collisions(int bit, int rounds) {
        List<Long> images8 = new LinkedList<>();
        boolean found = false;
        var cypher = new MD4();

        long key8_avg = 0;
        int count = 1;
        System.out.print("Calculating collisions for " + bit + " bits:      ");
        for (int i = 0; i < rounds; ++i) {
            System.out.print("\b\b\b\b");
            System.out.printf("%04d", i + 1);
            while (!found) {
                long buf = new BitUtils(cypher.digest(randomString(8).getBytes())).getNextLong(bit);
                if (images8.contains(buf))
                    found = true;
                images8.add(buf);
                ++count;
            }
            key8_avg += count;
            found = false;
            count = 1;
        }
        System.out.println();

        return key8_avg / rounds;
    }

    public static long secondPreimage(String source, int bit, int rounds) {
        var cypher = new MD4();
        int count = 0;
        long iteration_avg = 0;

        var sourceHash = cypher.digest(source.getBytes());
        var y0 = new BitUtils(sourceHash).getNextLong(bit);

        System.out.print("Calculating second preimage for " + bit + " bits:      ");

        for (int i = 0; i < rounds; i++) {
            System.out.print("\b\b\b\b");
            System.out.printf("%04d", i + 1);
            while (y0 != (new BitUtils(cypher.digest(randomString(8).getBytes()))).getNextLong(bit)) {
                count++;
            }
            iteration_avg += count;
            count = 0;
        }
        System.out.println();

        iteration_avg /= rounds;

        return iteration_avg;
    }

    public static String randomString(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
