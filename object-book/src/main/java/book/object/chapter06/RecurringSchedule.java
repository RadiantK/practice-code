package book.object.chapter06;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

// 반복 일정 : 일주일 단위로 돌아오는 특정 시간 간격에 발생하는 사건 전체를 포괄적으로 지칭하는 용
public class RecurringSchedule {
    private String subject;
    private DayOfWeek dayOfWeek;
    private LocalTime from;
    private Duration duration;

    public RecurringSchedule(String subject, DayOfWeek dayOfWeek, LocalTime from, Duration duration) {
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.from = from;
        this.duration = duration;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getFrom() {
        return from;
    }

    public Duration getDuration() {
        return duration;
    }
}
