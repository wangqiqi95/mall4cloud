package com.mall4j.cloud.biz.wx.wx.util;

/**
 * 会话存档数据解密
 */
public class SessionDecryptUtil {
    private static String RSA = "RSA";

//    static String priKey =
//            "-----BEGIN RSA PRIVATE KEY-----\n" +
//                    "MIICXAIBAAKBgQCtX0eWgh9AD2hPuiNGMSH24ocwB3bpIYruuKk8jUeARb/qfjEx\n" +
//                    "WYNRtdGFs79Pm9L7Sfl29JjuBCpg70iTrF0pIgXd9pjLWdwmeF7IF9e3c5IgjA4I\n" +
//                    "Nml1qe4g/8baXynfhX90vUGdy3UfpFgzT//Danxa/W8jg8Bfj0irgqyIGQIDAQAB\n" +
//                    "AoGAEgqsRHleDyiLTmCscw2B31NLhjAAq9oVvynwUqDRJAQeKKThMaWDCOnG2AcQ\n" +
//                    "jZRFrGjSURK7J2m/jz7XaqaxOv651Nf7xckc8Wr+CVKWcQp0Ekv6kbF6+gbsje9R\n" +
//                    "bP0MUJLtrd4SmNZgOItz7IC+kYuhd/SChDoX447G4HV49AECQQDU59b5PjqYmY81\n" +
//                    "T/bJl6kP2rT55KVVPGOh38EMGy3VZua9HFx4QihxTYOTpmc9cxyfuwMcppyGnm19\n" +
//                    "mgQhdBC5AkEA0HbuJbhXNyxGL2i+XR+jc5ccSqjMR9noXpgG62/BA+6ahOL9ZxBQ\n" +
//                    "1HwIVUs/qQf8cXR71sfmKKu33L0/KjbCYQJAFrYQgY/40jR3SVmZWtHZz/4llg6k\n" +
//                    "8F27xxXGUxNHJV+Pt5ah6pYsGEILiiGTG8P+xq89Wr4PLnER/vcB/8uQyQJAV6or\n" +
//                    "6+DhjGop+bXql+6+JdXeJ+dkQLL6bQ0xm8CbQrQMduWd+sF5vGGMf5Hta3/YQT3i\n" +
//                    "9ieKOoA8Ca/r6CyvAQJBAImOZakR3BndNXJNqQT/a5OPaGTT5D5P1i4GyzixxV7n\n" +
//                    "yZgoxrg+Us6rm30byXCyqF8JC68O/CXaGLlHGPRgiLA=\n" +
//                    "-----END RSA PRIVATE KEY-----";


//    static String priKey_PKCS8 = "-----BEGIN PRIVATE KEY-----\n" +
//            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK1fR5aCH0APaE+6\n" +
//            "I0YxIfbihzAHdukhiu64qTyNR4BFv+p+MTFZg1G10YWzv0+b0vtJ+Xb0mO4EKmDv\n" +
//            "SJOsXSkiBd32mMtZ3CZ4XsgX17dzkiCMDgg2aXWp7iD/xtpfKd+Ff3S9QZ3LdR+k\n" +
//            "WDNP/8NqfFr9byODwF+PSKuCrIgZAgMBAAECgYASCqxEeV4PKItOYKxzDYHfU0uG\n" +
//            "MACr2hW/KfBSoNEkBB4opOExpYMI6cbYBxCNlEWsaNJRErsnab+PPtdqprE6/rnU\n" +
//            "1/vFyRzxav4JUpZxCnQSS/qRsXr6BuyN71Fs/QxQku2t3hKY1mA4i3PsgL6Ri6F3\n" +
//            "9IKEOhfjjsbgdXj0AQJBANTn1vk+OpiZjzVP9smXqQ/atPnkpVU8Y6HfwQwbLdVm\n" +
//            "5r0cXHhCKHFNg5OmZz1zHJ+7AxymnIaebX2aBCF0ELkCQQDQdu4luFc3LEYvaL5d\n" +
//            "H6NzlxxKqMxH2ehemAbrb8ED7pqE4v1nEFDUfAhVSz+pB/xxdHvWx+Yoq7fcvT8q\n" +
//            "NsJhAkAWthCBj/jSNHdJWZla0dnP/iWWDqTwXbvHFcZTE0clX4+3lqHqliwYQguK\n" +
//            "IZMbw/7Grz1avg8ucRH+9wH/y5DJAkBXqivr4OGMain5teqX7r4l1d4n52RAsvpt\n" +
//            "DTGbwJtCtAx25Z36wXm8YYx/ke1rf9hBPeL2J4o6gDwJr+voLK8BAkEAiY5lqRHc\n" +
//            "Gd01ck2pBP9rk49oZNPkPk/WLgbLOLHFXufJmCjGuD5SzqubfRvJcLKoXwkLrw78\n" +
//            "JdoYuUcY9GCIsA==\n" +
//            "-----END PRIVATE KEY-----";


    static String priKey_PKCS8 = "MIIEogIBAAKCAQEAgvxyLaNOnlWTLFye9NPwveU/xE3ERn32cuiYrpJuPJ1Je/0v\n" +
            "UWM5IB6Ljuhjflz3ltQilzlNut2u+k+E282XvEe8x65W/Z9R3FZOb2Fw36K3yAbi\n" +
            "AIR53wDZiyD0cUJxrGiGJyDj6Q7z1e/yun9flI2im1TlSk/gNqPAfo+Xj0UasKJ6\n" +
            "frAlXc/5JiUjW6FXDgvd4SWDnpVDYR2DwA/yFQafSsRKgjF8CV6tDy9GGEN91+6j\n" +
            "GfLXwOYiqWQG1G8gpBFtqLnwyif/YAk0Uv+I9vhlwUMry+1i7HQXo076XFkAIv6q\n" +
            "cyjiCw5CUPWbEG2+VnlXpi6ycRtxLAY6aF5d4QIDAQABAoIBAADYZhh8LYCFNPug\n" +
            "HqS99ieUkll3BsNtckzmKj+UqXZVzcTJgs/ZtgN6fPwzbNpyuUSReoZrL9CrF/Ms\n" +
            "tpLYUCCOfjXz7qLo/31Od4e9m03PKhRV8okOMc7mDZFZuWgedZc3LrfzjPL65ikR\n" +
            "GQn58tUFp7ksxmTJEI1rt70dWj0aAsOYDBdrAYTBY/fiWJRDni5hAgbfZuqPbXK4\n" +
            "ykJB1q0xG/NkQMRVhe7b03uy65GvxVz4FEFD439m7D9fkbPwalUZZYXEB4OXIys4\n" +
            "8ozECDLdPekaf/lp+v/82vIVPsaD24UCwnbeeh25eCJRnUdIZabbkOExm/vmvi6d\n" +
            "tUC19wECgYEAs46YL8EPzpjovZ6osIGtmiWvrlnbHy3ohii0Um4mHZhRr15vrafa\n" +
            "lMXRII5+qfIN5TFc55n8PtJf/KIpWcGcQxQ3TFA2Au+sXSKrIOA28smI8It41g68\n" +
            "BfZSo3+Z4uuEkBAwnc7YI5qFvvVijQDW/whoRMb8RHTUQ0/nhkN09GECgYEAusA/\n" +
            "rBmb/S3mh5d8xgMbJxmnCKxLUIa2Cleg2m6kwOkIOsofLO9ZWh0S2KQBq46uUlRc\n" +
            "QXBdo+py1ww4Zd8JoZduUJyKkb0voFt8ZxONU9j1LJfdIKOb3uQioQGVBW1YOAMz\n" +
            "c1mPSXVv+JJkWdJ/5jrUlxWHSidEhLDez7vp2YECgYBTjhqRtlSFvjAn9QnowR+o\n" +
            "6Lqc9qF6G+HldFGdPjasdPImGlr7w21656bOcKDOsp32tobWG5j/HuSd6lQ1uVve\n" +
            "9yxYbccEjMV3vadGsC/BwTjhPvXnHooueBaykLFqoFBavb3x4YxA3So513SZyvdB\n" +
            "TIdA4hwIOxl/jVJe70sgYQKBgGqOW0ZFbn3tfMYNJ8KL7yhGOMp4FJkj5CbOjRdV\n" +
            "Xmg8DtgMj/FBuIf0YtZ97f4HcWvEesdT5/opQdF2RY/KzWIIV4djaqVxfwVObhMa\n" +
            "Ut9uwaXqGOAQulVZcnDPyEnKdDWxjd/ZUCqn+XWpgdzGgPfKxZZXUFR9R1k22ktJ\n" +
            "e0sBAoGAQKx4V5Eovs0JDUqBHXjiuA0Mvhz514JZqY76pxiiCavR3b02V/d0P7rF\n" +
            "0bS3cQ5hyUCn9jPyyYIG91GxoX7+FjJptwSiejjM/4zJfmvgq1mMHAKbU/ArgMRo\n" +
            "VEaiPA3lrnAmTB5pwQcp6hsvagKk4yqnBmv5ckKxAqO6+0p8yWA=";

    static String priKey_PKCS1="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/Xnu8vTa/eWwn\n" +
            "EFhxPNAHEvnLclwxCJ3e3lsdq8HmidPYsmjJvkph8TnE0MYVgu5U3E5ullGzL/4l\n" +
            "FdQ0/j6eZlDpsF2pEsef/433Jr7O2XXSqsBOzDozXr7iOOZCQmqvTcPzV7OrztZC\n" +
            "LOYpDX8DpthwJHRH+Z7ew7WfhO4+af3GaY9ZV2bUY9ur3wfv1Lj0zBl7UC5vyNnI\n" +
            "Tgz4DAlqjROPQKMt7OVjsvOH6O1c8wByXJr9pYoqf68clRCN8ppUVKLB+RGhSZi8\n" +
            "7Nn/ePMDCumofbwPWElMkAuf9AEeB1DYpy+x6zc/HD4XSAnfMfVpeUmCWcR9Krkb\n" +
            "0Gj4C2PvAgMBAAECggEADv9IkzIA6GCLqUQOqlEpvGQwT1FXCEC0npKucVTUjUyf\n" +
            "3YudyrGvAp30+QejfmwUYXIic2g0XuaT9DQRvVVvctpx3N6FeIqQOmm8pnHS4kMI\n" +
            "iMn46DP5G62BKBltciPiWHqvsJJe8kfzYpxCLVVxbsfUjnVM55cYbDzKLwYr5Zgm\n" +
            "JxYpOW1J+BolC0E8XCDyeHO56Ol+2Voh1bTUwg4N0SnKHlQP/yTlsA1QzgCbrxrE\n" +
            "S4CcHdNNjaxBRDSVtTiynUIwrLH/GpIbnZLyrtJiYFIiajaQuR81unyLHIs7OIlC\n" +
            "Z6lKkUPPPJNJ7zvcSjqc1zgbxRg4UucuvcaVO5K9AQKBgQDjG73XbO5lHx4Lb5y+\n" +
            "aPq0q4bun02vFpTv26IcQkAAWP8mnk6jlj8Uaf4AaQ+jqIWL/QytZ9ucjgM3CkZQ\n" +
            "meVcadK4Tg6JMOkaf26YnXK35OKqNJ3WKPpTAWYmlDT5faCJPPGIRKme7qifN5wD\n" +
            "lDWgfXEDAsyFxN/ESqMW/XsneQKBgQDXttEvkfeXXu/TKAIbZxXmT1dkcpGpeXP0\n" +
            "TNZ77kenz8eUz4pn5/u7Mi4gz9M4+LES2Y3BAK8LEgdJywmrht9ksnFFbJr91/4X\n" +
            "YKe2h0Ev0V8juM9nHJnVHMuCf92+3ZYVt9Y9oSzVtAOlIMdc6vDQccRWlnsH71hu\n" +
            "lrGOe1zEpwKBgQDFKhBmNevBXoJ26y7pH7qHn2JFR4fpuXETy1mxrtJheuIIZOdg\n" +
            "nc+lqV8afBtXubY3EjkvUjsWbzPbVNA2qWS2FikaWKnF8PBuULNzSz2M6OjfW06r\n" +
            "3md0KILGe2SCNM5qVr5Zz0Jy+D7r+xs3ADSOYFj8hDGK8KBTiXiNPQLuAQKBgDUz\n" +
            "RkYIJtrzNAyMqnBdrR3+5VvFkb0NUhanOKKZ8pwld2BxCv5zTTcuugenmb1MQEeY\n" +
            "1ggKhiEh0iBV7c2a1s973smzbko6kdTsJpCyrNYBOgiSVLxmZl4T2vEVXffrqE80\n" +
            "5qaOcINIATohamm1G60c0cJVHISpd9LWYXX1YLyzAoGAbZ8L/qg+ZhR976gmB7ko\n" +
            "CPz6Yc7nmKrBmifm2TIrrFkd22OlU66mpj/Z6QXoaUH3XmXg5ufv/12XqTRMcsm7\n" +
            "Wb336Fv/xdSvpZtr1oi9H54ME/vg6fAt+b0vPzahShZd8woDgeIKXnHp0i7EEl2/\n" +
            "Mm5e8f9WY1mLYWf8MTBMTfw=";



    /**
     * 用私钥解密
     *
     * @param encryptedData
     *            经过encryptedData()加密返回的byte数据
     *            私钥
     * @return
     */
    public static String decryptData(String encryptedData,String type)
    {
        try
        {
            if("1".equals(type)){
                return RSAUtil.decryptByPriKey(encryptedData, priKey_PKCS8,type);
            }else{
                return RSAUtil.decryptByPriKey(encryptedData, priKey_PKCS1,type);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }







}
