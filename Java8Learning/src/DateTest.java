import org.junit.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
Joda.org

1.格林威治时间
2.UTC时间：世界协调时间（UTC）是世界上不同国家用来调节时钟和时间的主要时间标准。

               ：也就是零时区的时间

  CST时间：中央标准时间
 */
public class DateTest {
    @Test
    public void test1(){
        LocalDate date = LocalDate.now();
        System.out.println(date);
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime d2 = dateTime.minusYears(2).withMonth(2);
        System.out.println(dateTime);
        System.out.println(d2);
        Duration between = Duration.between(dateTime, d2);
        System.out.println(between.toDays());
    }
}
