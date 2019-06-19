import com.sun.istack.internal.NotNull;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Java8 {
    static @NotNull
    List<Room> roomList = Arrays.asList(new Room("t1", 3), new Room("t3", 4), new Room("t2", 9));

    static List<String> teamIndia = Arrays.asList("Virat", "Dhoni", "Jadeja");
    static List<String> teamAustralia = Arrays.asList("Warner", "Watson", "Smith");
    static List<String> teamEngland = Arrays.asList("Alex", "Bell", "Broad");
    static List<String> teamNewZeland = Arrays.asList("Kane", "Nathan", "Vettori");
    static List<String> teamSouthAfrica = Arrays.asList("AB", "Amla", "Faf");
    static List<String> teamWestIndies = Arrays.asList("Sammy", "Gayle", "Narine");
    static List<String> teamSriLanka = Arrays.asList("Mahela", "Sanga", "Dilshan");
    static List<String> teamPakistan = Arrays.asList("Misbah", "Afridi", "Shehzad", "ffddsf", "fdfvd", "fdfwewe", "ewq");

    static List<List<String>> playersInWorldCup2016 = new ArrayList<>();
    {
        playersInWorldCup2016.add(teamIndia);
        playersInWorldCup2016.add(teamAustralia);
        playersInWorldCup2016.add(teamEngland);
        playersInWorldCup2016.add(teamNewZeland);
        playersInWorldCup2016.add(teamSouthAfrica);
        playersInWorldCup2016.add(teamWestIndies);
        playersInWorldCup2016.add(teamSriLanka);
        playersInWorldCup2016.add(teamPakistan);
    }

    @Test
    public void findAnyTest(){
        List<String> teamPakistan = Arrays.asList("Misbah", "Afridi", "Afridi", "ffddsf", "fdfvd", "fdfwewe", "ewq");
        Optional<String> o =teamPakistan.parallelStream().filter( s-> s.startsWith("f")).findAny();
        System.out.println(o.get());
    }

    @Test
    public void mapReduceTest(){
        int summ1 = roomList.stream().map( r-> r.getLeg()).reduce(0, (x,y)->x+y );
        int summ2 = roomList.stream().collect(Collectors.summingInt(Room::getLeg));
        Optional<Integer> summ3 = roomList.stream().map( r-> r.getLeg()).reduce(Integer::max);
        System.out.println(summ1+","+summ2+","+summ3);

        System.out.println(roomList.stream().collect(Collectors.reducing("", Room::getTable, (s3, s2) -> s3 + s2)));

        System.out.println(roomList.stream().map(Room::getTable).reduce("+", (s2, s3) -> s2 + "," + s3));

        List<Integer> numList = Arrays.asList(5, 9, 3);
        System.out.println("num test:" + numList.stream().reduce(Integer::max).get());
        System.out.println("num test:" + numList.stream().reduce(1, (x, y) -> x + y).intValue());

        roomList.stream().max(Comparator.comparing(Room::getLeg));
        Optional.of(roomList.stream().reduce(BinaryOperator.maxBy(Comparator.comparing(Room::getLeg)))).ifPresent(System.out::println);
    }

    @Test
    public void collectTest(){
        roomList.stream().collect(Collectors.toMap(Room::getTable,Room::getLeg)).forEach( (k,v) -> System.out.println(k+":"+v) );


        System.out.println("------join and mapping-------");
        Optional.of(roomList.stream().collect(Collectors.mapping(Room::getTable,Collectors.joining()))).ifPresent(System.out::println);
        Optional.of(roomList.stream().map(Room::getTable).collect(Collectors.joining(",","[","]"))).ifPresent(System.out::println);
        System.out.println("------counting-------");

        Optional.of(roomList.stream().collect(Collectors.counting())).ifPresent(System.out::println);
        System.out.println("------summarize-------");
        Optional.of(roomList.stream().collect(Collectors.summarizingInt(Room::getLeg))).ifPresent(System.out::println);

        System.out.println("------grouping by-------");
        Map<String,List<Room>> mapRlist1 = roomList.stream().collect(Collectors.groupingBy(Room::getTable));
        mapRlist1.forEach( (s,l) -> System.out.println(s+":"+l.size()));

        Map<String, Integer> mapRList2 = roomList.stream().collect(Collectors.groupingBy(Room::getTable, Collectors.summingInt(Room::getLeg)));
        mapRList2.forEach( (s,i) -> System.out.println(s+","+i));

        roomList.stream().collect(Collectors.groupingBy(Room::getTable,TreeMap::new,Collectors.averagingInt(Room::getLeg))).forEach( (s,i) -> System.out.println(s+","+i));


        System.out.println("------collectingAndThen by-------");

        Optional.of(roomList.stream().collect(Collectors.groupingBy(Room::getTable, Collectors.collectingAndThen(Collectors.toSet(), list -> {
            list.add(new Room("hhf", 10));
            return list;
        })))).ifPresent(System.out::println);

        System.out.println(roomList.stream().collect(Collectors.groupingBy(Room::getTable, Collectors.mapping(Room::getLeg, Collectors.toSet()))));


        Optional.of(roomList.stream().collect(Collectors.collectingAndThen(Collectors.averagingInt(Room::getLeg), i -> " this is average leg "+i ))).ifPresent(System.out::println);

        System.out.println("------Collector -------");

        List<Room> cTest1 = roomList.stream().collect(ArrayList::new, List::add, List::addAll);
        System.out.println(cTest1);

        List<Room> cTest2 = roomList.stream().collect(ArrayList::new, (list, r) -> {
            r.setLeg(r.getLeg() + 1);
            list.add(r);
        }, (list1, list2) -> {
            list1 = null;
            list2 = null;
        });
        cTest2.stream().forEach(r -> System.out.println(r.getLeg()));

        String roomStr = roomList.stream().collect(StringBuilder::new, (r, sb) -> {
            r.append(sb.getTable());
        }, (sw1, sw2) -> {
            sw1.append("sw1");
            sw2.append(sw1);
        }).toString();

        System.out.println(roomStr);
        System.out.println("cTest3---------");
        System.out.println("cTest34---------");
        List<String> list =Arrays.asList(new String[]{"1","2","3","4","5","6","7","8"});
        list.stream().collect(new ToListCollector<String>());

        System.out.println("cTest34---------");
        List<Room> cTest3 = roomList.parallelStream().collect(new ToListCollector<Room>());

        cTest3.stream().forEach(r -> System.out.println(r.getTable()));

        Set<String> hashSetTest = new HashSet<>();
        hashSetTest.add("1");
        hashSetTest.add("2h");
        hashSetTest.add("3");
        hashSetTest.add("4k");
        hashSetTest.add("5");
        hashSetTest.add("6");


        System.out.println("cTest3---------");
        hashSetTest.stream().forEach(e -> System.out.print(e + " "));
        System.out.println("---------");
        hashSetTest.parallelStream().collect(new HashSetCollector<String>()).forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });

        int[] tt = teamPakistan.stream().collect(Collector.of(
                () -> new int[1],
                (result, item) -> result[0] += item.length(),
                (result1, result2) -> {
                    System.out.println("combine");
                    //result1[0] += result2[0];
                    return result1;
                }
        ));
        for (int i : tt) {
            System.out.println(i);
        }

        Map<Boolean,List<Room>> pMap= roomList.stream().collect(Collectors.partitioningBy(r-> r.getLeg()>5 ) );
        System.out.println(pMap);

    }

    @Test
    public void mapFlatMapTest(){
        teamIndia.stream().map(s -> s.split("")).flatMap(Arrays::stream).forEach(System.out::println);

        System.out.println("flat map -----");
        teamIndia.stream().flatMap(s1 -> teamAustralia.stream().
                filter(s2 -> {
                    //System.out.println("---"+s1);
                    return s2.contains(s1);
                })).forEach(System.out::println);

        System.out.println("-----");

        playersInWorldCup2016.stream()
                .flatMap(list -> list.stream().skip(1))
                .map(s -> s + "ff").sorted((s1, s2) -> {
            return s1.compareTo(s2);
        })
                .forEach(System.out::println);


    }
    @Test
    public  void splitorTest(){
        teamIndia.spliterator().tryAdvance(System.out::println);
        teamIndia.spliterator().forEachRemaining(System.out::println);
        teamPakistan.spliterator().trySplit().forEachRemaining(System.out::println);
        Optional.of(teamPakistan.spliterator().trySplit()).ifPresent( s -> System.out.println(s.estimateSize()));
    }

    public static void main(String[] args) {




        Collections.sort(teamIndia, String::compareToIgnoreCase);

        Set<String> setTest = new HashSet();
        String setStr = "hh";
        setTest.add("gg");
        setTest.add("vv");
        setTest.add("hh");
        setTest.add(setStr);
        setTest.add(setStr);
        setTest.stream().limit(10).forEach(System.out::println);



        Consumer<String> c = (t) -> System.out.println(t);
        c.andThen((s) -> System.out.println(s + ",then")).accept("g");



    }

    @Test
    public void testNumericStream(){
        int a = 9;
        IntStream.range(1,100).filter(b -> Math.sqrt(a * a + b * b) % 1==0).mapToObj( t -> new int[]{ a, t , (int)Math.sqrt(a*a +t*t)})
                .forEach( z -> System.out.println(z[0]+","+z[1]+","+z[2]));
    }

    @Test
    public void optionalTest(){
        Optional<String> e = Optional.empty();
        System.out.println(e.get());

        Optional.of(null);

        QuoteBuilderFactory fac = new QuoteBuilderFactory();
        String nothing = "tt";
        //Optional
        String name = Optional.ofNullable(fac).map(f -> f.getB1()).map(b -> b.getQuote())
                .map(q -> q.getName()).orElse(nothing);

        Optional.ofNullable(fac).flatMap(f -> Optional.ofNullable(f.getB1())).filter( b-> b.getQuote()!=null);
        System.out.println("name is " + name);

    }


    @Test
    public void lambdaExceptionTest(){
        //lambda Exception
        Function<String, String> function1 = (String t) -> {
            try {

            } catch (Exception e) {

            }
            return "";
        };

        Foo1 f1 = (List<String> list) -> {
            throw new IOException();
        };
        try {
            f1.bar(teamIndia);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructMethod() {
        Function<Integer, String[]> fs = String[]::new;
        //Constructor lambda
        Function<String, Room> s1 = Room::new;
        s1.apply("t");
        Supplier<String> su = String::new;
        su.get();

    }

    @Test
    public void testForkJoin(){
        System.setProperty("java.util.concurrent.ForkJoinPool.commonPool().getParallelism()","12");

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> test = new ForkJoinSumTest(0,200);
        Long sum = pool.invoke(test);
        System.out.println(sum);
        LongStream.range(0,1000L)
                .parallel()
                .reduce(0,Long::sum);
    }

    @Test
    public void testNewDate(){
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);
        ldt.minusDays(2);
        ldt.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        Instant in = Instant.now();//UTC 时区
        System.out.println(in);

    }
}

class Room {
    public Room(String table) {
        this.table = table;
    }

    public Room(String table, int l) {
        this.table = table;
        this.leg = l;
    }

    private String table;
    private int leg;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getLeg() {
        return leg;
    }

    public void setLeg(int leg) {
        this.leg = leg;
    }

    @Override
    public String toString() {
        return "[ "+ this.table+","+this.getLeg()+" ]";
    }
}

interface QuoteFactory<T, O> {
    O create(T t);
}

class QuoteBuilderFactory {
    private QuoteBuilder1 b1;

    public QuoteBuilder1 getB1() {
        return b1;
    }

    public void setB1(QuoteBuilder1 b1) {
        this.b1 = b1;
    }

}

class QuoteBuilder1 {
    private Quote quote;

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

}

class Quote {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

interface Foo1 {
    void bar(List<String> arg) throws IOException;
}

interface Foo2 {
    void bar(List arg);
}

interface Foo extends Foo1, Foo2 {
    public static void bar1(){
        System.out.println("");
    }
}