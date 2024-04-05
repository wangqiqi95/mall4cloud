package com.mall4j.cloud.common.util;

public class NumberTo64 {

    private static NumberTo64 numberTo64=new NumberTo64();

    private NumberTo64() {
    }

    public static String to64(long number) {
        return numberTo64.compressNumber(number);
    }

    public static Long form64(String number) {
        return numberTo64.unCompressNumber(number);
    }

    final  char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', '(', ')' };
    
    

    /**
     * 把10进制的数字转换成64进制
     * 
     * @param number
     * @param shift
     * @return
     */
    private String compressNumber(long number) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 64;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= 6;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }

    /**
     * 把64进制的字符串转换成10进制
     * 
     * @param decompStr
     * @return
     */
    private long unCompressNumber(String decompStr) {
        long result = 0;
        for (int i = decompStr.length() - 1; i >= 0; i--) {
            if (i == decompStr.length() - 1) {
                result += getCharIndexNum(decompStr.charAt(i));
                continue;
            }
            for (int j = 0; j < digits.length; j++) {
                if (decompStr.charAt(i) == digits[j]) {
                    result += ((long) j) << 6 * (decompStr.length() - 1 - i);
                }
            }
        }
        return result;
    }

    /**
     * @param ch
     * @return
     */
    private long getCharIndexNum(char ch) {
        int num = ((int) ch);
        //处理 0 ~ 9
        if (num >= 48 && num <= 57) {
            return num - 48;
            // 处理  A-Z
        }  else if (num >= 65 && num <= 90) {
            return num - 55;
            //处理 a-z
        } else if (num >= 97 && num <= 122) {
            return num - 61;
            //处理 ( )
        } else if (num == 40 || num == 41) {
            return num + 22;
        }
        return 0;
    }
    
    public static void main(String[] args) {
        Long a=738043328267964437L;
        System.out.println(to64(a));
        
        System.out.println(form64("e(4GmoGb0o"));
        t1();
    }
    
    static void t1() {
        char i ='0';
        int v = (int)i;
        char i_1 ='9';
        int v_1 = (int)i_1;
        char i_2 ='A';
        int v_2 = (int)i_2;
        char i_3 ='Z';
        int v_3 = (int)i_3;
        char i_4 ='a';
        int v_4 = (int)i_4;
        char i_5 ='z';
        int v_5 = (int)i_5;
        char i_6 ='(';
        int v_6 = (int)i_6;
        char i_7 =')';
        int v_7 = (int)i_7;
        System.out.println("i : 0 , v:"+v +",  diff : "+(v - 0));
        System.out.println("i : 9 , v:"+v_1 +",  diff : "+(v_1 - 9));
        System.out.println("i : A , v:"+v_2 +",  diff : "+(v_2 - 10));
        System.out.println("i : Z , v:"+v_3 +",  diff : "+(v_3 - 35));
        System.out.println("i : a , v:"+v_4 +",  diff : "+(v_4 - 36));
        System.out.println("i : z , v:"+v_5 +",  diff : "+(v_5 - 61));
        System.out.println("i : ( , v:"+v_6 +",  diff : "+(v_6 - 62));
        System.out.println("i : ) , v:"+v_7 +",  diff : "+(v_7 - 63));
        
    }
    
    
}
