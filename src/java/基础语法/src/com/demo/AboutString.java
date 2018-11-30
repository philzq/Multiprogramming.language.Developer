package com.demo;

import com.demo.Helper.AESHelper;
import com.demo.Helper.SecurityHelper;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;


public class AboutString {

    public static void RunDemo() throws Exception {

        //region 构造个string
        {
            // 平时使用最多的
            var strCommon = "c:\\a\\b"; //要转义，貌似比较麻烦，其实你在Paste及Copy时idea会自动转义或自动去掉转义，还是挺方便的，
            var strEmpty = "";
            var strNormal = "now";
            var strPlus = 1 + "2" + ZonedDateTime.now() + true + "3"; // 如果不是string的会使用object.ToString()后连接在一起。
            var strConcat = "1".concat(String.valueOf(true)).concat(LocalDateTime.now().toString()).concat(Double.toString(3.333)); // 1true2018-11-29T15:56:19.8074423003.333 concat只支持string,与C#不同。

            // hello 123 2018-11-22T11:04:58.364802200 ，一般写法，按顺序的，C#能把这个秒成渣。
            var strFormat = String.format("%s %s %s", "hello", 123, LocalDateTime.now());
            // hello 123 hello 2018-11-22T11:04:58.364802200，格式化的写法，比较麻烦，还是按上一种写吧
            var strFormat1 = String.format("%1$s %2$s %1$s %3$s", "hello", 123, LocalDateTime.now());

            // java 没有PadLeft及PadRight，但可以左右pad空格, 有repeat,也可以用org.apache.commons.lang.StringUtils#rightPad
            var strPad = "@1A".repeat(100);
            var strPadLeft = String.format("%10s", "foo").replace(' ', '*'); //*******foo
            var strPadRight = String.format("%-10s", "bar").replace(' ', '*'); //bar*******

            // 大量字符串操作时+性能很差，要换成StringBuilder,没有类似C#的AppendLine,AppendFormat
            var sb = new StringBuilder();
            sb.append(1).append("2").append(true).append(ZonedDateTime.now());
            var sbresult = sb.toString();

            System.out.println("断点用");
        }
        //endregion


        //region string的各种操作，比较
        {
            var strischars = "x123".charAt(0); // x 取相关位置字符

            //startsWith,endsWith,contains没有忽略大小写的参数，regionMatches(true,可以变相实现。
            var strStart = "Usp_SearchCity".startsWith("usp_"); // false

            var strEnd = "SearchCityDTO".endsWith("DTO"); // true

            var strContains = "abc123".contains("ABC"); //false

            // 只是演示一下有哪些常用操作
            var strOpt = " 123|a|b|c456| ".strip().toUpperCase().toLowerCase()
                    .replace("a", "aa") //普通字符替换
                    .replaceAll("/(\\d+)/", "$1$1") //正则的
                    .substring(0, 10) //长度取多了会报错
                    .split("|") //split只支持正则
                    ;

            var strTrim = "\r\n\t   　 　  12 12　 　  \r\n\t".trim(); //“　 　  12 12　 　”， 前后有中英文空格及tab回车换行，有些替换不了

            var strStrip = "\r\n\t   　 　  12 12　 　  \r\n\t".strip(); //“12 12”， ★前后有中英文空格及tab回车换行, 与C#的trim行为相同。

            var lstTemp = Arrays.asList("A", "BC", "DEF");
            var sjoin = String.join(",", lstTemp); //A,BC,DEF


            // split 是用正则的，所以就一种写法，至多加个参数0可以移除后面的空item,没有像C#的移除所有 空item 的参数
            var split2 = "aa@bb|cc#dd$ee,ff".split("[@|#$,]");


            //region string的比较，要用equals而不要用==,因为equals是比较内容的，==是比较引用的，与C#不同
            {
                // These two have the same value
                var x = new String("test").equals("test"); // --> true

                // ... but they are not the same object
                var x1 = new String("test") == "test"; // --> false

                // ... neither are these
                var x2 = new String("test") == new String("test"); // --> false

                // ... but these are because literals are interned by the compiler and thus refer to the same object
                var x3 = "test" == "test"; // --> true

                // ... string literals are concatenated by the compiler and the results are interned.
                var x4 = "test" == "te" + "st"; // --> true

                // ... but you should really just call Objects.equals()
                var x5 = Objects.equals("test", new String("test")); // --> true
                var x6 = Objects.equals(null, "test"); // --> false
                var x7 = Objects.equals(null, null); // --> true
            }
            //endregion
            System.out.println("断点用");

        }
        //endregion


        //region 正则表达式
        {
            //要忽略大小写，可以在正则前加(?i)实现，如：(?i)[a-z]
            //java的正则split及replace比较方便，直接就是字符串的操作，不用再引入正则库。

            var str1 = "aa@bb|cc#dd$ee,ff".split("[@|#$,]"); // {"aa","bb","cc","dd","ee","ff"}
            var str1_1 = "A1b2C3".split("(?i)[a-z]"); //{"","1","2","3"}

            var str2 = "a@b,c".replaceAll("[@,]", "-"); // a-b-c

            var str3 = "a111b222c".replaceAll("(\\d+).(\\d+)", "$2-$1"); // a222-111c 后向引用啦。

            var str4 = "a111b222C".replaceAll("(?i)[a-z]", "_");// _111_222_

            var p = Pattern.compile("[a-z]", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            var str5 = p.split("a111b222C");
            var str6 = p.matcher("a111b222C").replaceAll("_");

            var match0 = Pattern.matches(",", "a@b,c"); // true

            // 匹配一个 就是find()一次
            var match = Pattern.compile("(\\d+).(\\d+)").matcher("a111b222c");
            if (match.find()) {  //必须find()后才执行了，直接拿m.group(1)是不行的，这是因为他find()后才会去找下一个匹配的
                var mv = match.group(); // 111b222 代表匹配的内容
                var mg0 = match.group(0); // 111b222 代表匹配的内容
                var mg1 = match.group(1); // 111 代表匹配的内容的第1个引用
                var mg2 = match.group(2); // 222 代表匹配的内容的第2个引用
            }

            // 匹配多个 就是find()多次，并且这时的group也是本次匹配的group。如果想跟C#做成一样的，可以先转数组再遍历match2.results().collect(Collectors.toList())
            var match2 = Pattern.compile("(\\d+).(\\d+)").matcher("a111b222c x333y444z  o555p666q"); // 匹配了3组
            while (match2.find()) {
                var mv = match2.group();
                var mg0 = match2.group(0);
                var mg1 = match2.group(1);
                var mg2 = match2.group(2);
                System.out.println("断点用");
            }

            System.out.println("断点用");


        }
        //endregion


        //region 加解密,MD5,SHA,AES,RSA
        {
            //具体实现参见：Helper/*

            var src = "我是123の뭐라구";

            var base64_to = SecurityHelper.ToBase64(src);
            var base64_from = SecurityHelper.FromBase64(base64_to);

            var md5_16bit = SecurityHelper.To16bitMD5(src);
            var md5_32bit = SecurityHelper.To32bitMD5(src);

            var sha1 = SecurityHelper.ToSHA1(src);
            var sha512 = SecurityHelper.ToSHA512(src);

            var aes_key_iv = AESHelper.GetKeys();
            var aes_en = AESHelper.AESEncrypt(src, aes_key_iv[0],aes_key_iv[1]);
            var aes_de = AESHelper.AESDecrypt(aes_en, aes_key_iv[0],aes_key_iv[1]);

            System.out.println("断点用");

/*


            var rsaKeys = RSAHelper.GetKeys();
            var rsa_en = src.RSAEncrypt(rsaKeys.PublicKey);
            var rsa_de = rsa_en.RSADecrypt(rsaKeys.PrivateKey);*/

        }
        //endregion


    }
}
