package com.demo;

import com.demo.Entity.Person;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;


//https://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
//https://www.oracle.com/technetwork/articles/java/architect-streams-pt2-2227132.html
//https://blog.lahteenmaki.net/java-streams-vs-c-linq-vs-java6-updated.html
//Java的Lambda的的实现方法就是定义一堆接口，让你写。

public class AboutCollection {

    public static void RunDemo() {
        //jdk9对List,Set,Map都加了.of(),来生成只读的集合
        RunStreamDemo();
        RunListDemo();
        RunHashSetDemo();
    }

    public static void RunStreamDemo() {
        //C#.Linq与Java.Stream不同。Java有必要单独介绍下Stream()

        //演示：Stream初始化的几种方法
        Stream<Integer> init01 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Stream<Integer> init02 = Stream.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Stream<String> init03 = Stream.of("A", "B", "C");
        Stream<String> init04 = Stream.of("DEF".split(""));
        Stream<Integer> init05 = List.of(1, 2, 3).stream();
        Stream<LocalTime> init06 = Stream.generate(() -> LocalTime.now()); //得到一个无限循环的
        Stream<Integer> init07 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        //演示：IntStream, LongStream, DoubleStream的一些常用方法
        // 创建包含基本类型1， 2， 3的 IntStream
        IntStream intStream = IntStream.of(1, 2, 3);

        // 创建一个包含1到9的 IntStream，后边不闭合
        IntStream range = IntStream.range(1, 10);

        // 创建一个包含1到10的 IntStream
        IntStream rangeClosed = IntStream.rangeClosed(1, 10);

        // 创建一个包含3的 IntStream
        IntStream generated = IntStream.generate(() -> 3);

        // 得到一个无限循环的 IntStream, 值为 1, 3, 5, 7 ...
        IntStream infinite = IntStream.iterate(1, p -> p + 2);

        //随机10个
        IntStream randomint01 = new Random().ints(10);

        //1-100之间，无限循环的IntStream
        IntStream randomint02 = new Random().ints(1, 100);

        //1-100之间，随机10个
        IntStream randomint03 = new Random().ints(10, 1, 100);

        //基类型int,bool,float,long等需要.boxed()下才能转成泛型的
        Stream<Integer> boxed = IntStream.of(1, 2, 3).boxed();


        //演示：转换为List或Array,map等
        var toarray01 = IntStream.of(1, 2, 3).toArray();
        var toarray02 = Stream.of(1, 2, 3, 4).toArray(Integer[]::new);

        var tolist01 = Stream.of(1, 2, 3, 4, 5, 6).filter(p -> p % 2 == 0).collect(Collectors.toList());
        var tolist02 = Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.toList());

        var tolist03 = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        var tolist04 = IntStream.of(1, 2, 3).mapToObj(String::valueOf).collect(Collectors.toList());


        var map01 = Stream.of("A", "B", "C").map(String::toLowerCase);
        var map02 = Stream.of("A", "B", "C").map(p -> new Person(1, p.repeat(10))); //转换成新对象


        //★stream只能用一次，当终结后就不能再用了，如：sum,collect。C#的Linq产生的中间物IEnumerable是可以重用的。
        var tempStream = Stream.of("A", "B", "C");
        var tempStream1 = tempStream.max(Comparator.comparing(p -> p));
        //var tempStream2 = tempStream.min(Comparator.comparing(p -> p)); //stream只能用一次，再次使用会报错，与C#不同

        //演示：并行

        //串行，正常顺序
        LongStream.range(1, 10).forEach(System.out::println);

        //并行，乱序的，与串行写法类似，就加个.parallel()跟C#一样好用
        LongStream.range(1, 10).parallel().forEach(System.out::println);

        //其他操作就放到List里说了
    }


    private static void RunListDemo() {
        //演示：初始化List或ArrayList的几种方法
        var lstInit01 = new ArrayList<String>();
        var lstInit02 = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        var lstInit03 = new ArrayList<Person>() {{
            add(new Person(1, "A"));
            add(new Person(2, "B"));
            add(new Person(2, "B"));
        }};

        //演示：添加元素的方法，单个的，List的，实现IEnumerable<T>接口的
        lstInit01.add("A");

        lstInit02.add("B");
        lstInit02.add("C");

        lstInit02.addAll(List.of("4", "5", "6")); //不能addAll数组，C#可以
        lstInit02.addAll(lstInit01);

        //以下几个生成不可修改的List,add,remove会报错
        var lstInitNCopy = Collections.nCopies(10, "ABC"); //ABC COPY10份，这是测试用的功能吧

        var lst_ReadOnly_1 = Arrays.asList(new String[]{"1", "2", "3", "3"});

        var lst_ReadOnly_2 = Arrays.asList("1", "2", "3", "3");

        var lst_ReadOnly_3 = List.of("1", "2", "3", "3"); //★推荐用这个，原因：短小精悍。

        //初始化后还想改，那就再包装下吧。
        var lstInit05 = new ArrayList<>(List.of("1", "2", "3", "3")); //★推荐
        lstInit05.add("4");

        //演示：lst的各种方法
        var lstData = new ArrayList<Person>();

        var lsthas = lstData != null && !lstData.isEmpty();//没有扩展方法自己写吧
        var lstEmpty = lstData == null || lstData.isEmpty();//自己写的扩展方法

        for (int i = 0; i < 100; i++) {
            var height = BigDecimal.valueOf(i).divide(new BigDecimal(3), 1, RoundingMode.HALF_EVEN);
            lstData.add(new Person(i, "Name" + i, LocalDateTime.now().plusDays(1), true, height, true));
        }


        var lst01 = lstData.stream().map(Person::getId).collect(Collectors.toList()); //将Id组成新的List
        var lst02 = lstData.stream().map(p -> p.getId()).collect(Collectors.toList()); //将Id组成新的List
        var lst03 = lstData.stream().max(Comparator.comparing(p -> p.getId())).get();
        var lst04 = lstData.stream().min(Comparator.comparing(p -> p.getId())).get();
        //没有findLast,可以用Google Guava's  Iterables.getLast(myIterable)，或者自己多写几行代码实现
        var lst05 = lstData.stream().findFirst().get();
        var lst06 = lstData.stream().filter(p -> p.getId() > 10).findFirst().get();
        var lst09 = lstData.stream().anyMatch(p -> p.getHuman());
        var lst10 = lstData.stream().allMatch(p -> p.getHuman());
        var lst11 = Stream.concat(lstData.stream(), lstData.stream()).collect(Collectors.toList());
        var lst12 = lstData.removeIf(p -> p.getId() > 80);

        var lst13 = lstData.stream()
                .filter(p -> p.getBirthday().isAfter(LocalDateTime.now().plusDays(30)) && p.getId() < 50)//筛选
                //级联排序 https://www.concretepage.com/java/jdk-8/java-8-stream-sorted-example
                .sorted(Comparator.comparing(Person::getName).thenComparing(Person::getId).reversed())
                .skip(1) //跳过
                .limit(10) //获取
                .map(p -> new Person(p.getId(), p.getName())) //没有类似C#的匿名类，所以还是转为原生的吧
                .collect(Collectors.toMap(p -> p.getId(), p -> p)); //转为map


        //Distinct的默认扩展不适合复杂类型，这个是自己写的方法
        var lst14 = lstData.stream().filter(ComparatorHelper.distinct(p -> p.getId())).collect(Collectors.toList());

        //演示：GroupBy的更强大的实现
        var lst15 = lstData.stream().limit(10).collect(Collectors.groupingBy(p -> {
            //这里的内容就是构造Key的，最终返回的Key相同的会放到同一组
            if (p.getHeight().compareTo(BigDecimal.valueOf(2)) > 0) {
                return "Height>2";
            } else {
                return "Height<=2";
            }
        }));

        //演示：相当于线程安全的ArrayList
        var lstUnModify = Collections.unmodifiableCollection(lstData); //变成只读集合还有上面一堆.of()的也是只读集合

        var lstSync = Collections.synchronizedList(List.of(1, 2, 3)); //读少写多，这个性能高，读写都锁住

        var conlist = new CopyOnWriteArrayList<Person>(); //读多写少，这个性能高，只锁住写操作
        if (conlist.isEmpty()) {
            conlist.add(new Person());
        }


    }


    private static void RunHashMapDemo() {
        var map1 = new HashMap<Integer, String>();
        map1.put(1, "111");
        map1.put(2, "222");
        map1.put(3, "333");


        var map1_1 = new HashMap<Integer, String>() {{
            put(1, "111");
            put(2, "222");
            put(3, "333");
        }};





/*

        var dict2 = new Dictionary<string, string>(StringComparer.OrdinalIgnoreCase)
        {
            { "A","111"},
            { "a","222"},
            { "b","333"},
        };

        //自定义比较器
        var dict3 = new Dictionary<Person, int>(new PredicateEqualityComparer<Person>((x, y) => x.Id == y.Id))
        {
            { new Person{ Id=1,Name="A"} ,1},
            { new Person{ Id=2,Name="B"} ,2},
            { new Person{ Id=1,Name="B" } ,3},
        };

        var dict = new Dictionary<string, int>();
        dict.Add("1", 2);//添加赋值
        dict["a"] = 1; //直接赋值
        dict.Add("2", 2);
        //dict3.Add("2", 2);//key重复会报错
        var dd = dict.TryAdd("2", 2);//不会报错，会返回false

        if (dict.ContainsKey("a"))
        {
            Console.WriteLine(dict["a"]);
        }

        if (dict.TryGetValue("2", out int getResult))
        {
            Console.WriteLine(getResult);
        }



        var keys = dict.Keys.ToList();
        var valus = dict.Values.ToList();


        //线程安全的
        var condict = new ConcurrentDictionary<int, Person>();
        condict.TryAdd(1, new Person { Id = 1, Height = 1 });
        condict.TryRemove(1, out Person resultTemp);
        condict.TryGetValue(1, out Person resultTemp2);
        */


    }

    public static void RunHashSetDemo() {
        var hsInit1 = new HashSet<String>(); //C#可以指定比较器，Java.HashSet不能指定。
        var hsInit2 = new HashSet<String>(List.of("1", "2", "3", "3"));

        var tsInit1 = new TreeSet<String>();
        var tsInit2 = new TreeSet<String>(List.of("1", "2", "3", "3"));
        var tsInit3 = new TreeSet<String>(String::compareToIgnoreCase);
        tsInit3.addAll(List.of("A", "a", "b")); //["A","b"]


        //自定义比较器
        var tsInit4 = new TreeSet<Person>(Comparator.comparing(Person::getId));
        var tsInit5 = new TreeSet<Person>((x, y) -> x.getId() == y.getId() && x.getName() == y.getName() ? 0 : 1);
        var tsInit6 = new TreeSet<Person>(ComparatorHelper.bool((x, y) -> x.getId() == y.getId() && x.getName().equalsIgnoreCase(y.getName())));

        var tsTest = tsInit5;
        tsTest.add(new Person(1, "A"));
        tsTest.add(new Person(1, "A"));
        tsTest.add(new Person(1, "B"));
        tsTest.add(new Person(2, "B"));


        var h3 = new HashSet<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};

        var b1 = h3.contains(3);
        var h4 = h3.stream().filter(p -> p > 1).collect(Collectors.toSet());
        System.out.println("断点用");

    }

    public static void RunQueueDemo() {


    }


}


class ComparatorHelper {
    @FunctionalInterface
    interface BooleanFunction<T> {
        Boolean go(T a, T b);
    }

    public static <T> Comparator<T> bool(BooleanFunction<T> func) {
        return (x, y) -> func.go(x, y) ? 0 : 1;
    }

    //TODO:为什么?不能用别的替代？
    //https://howtodoinjava.com/java8/stream-distinct-by-multiple-fields/
    public static <T> Predicate<T> distinct(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return p -> seen.add(keyExtractor.apply(p));
    }
}


