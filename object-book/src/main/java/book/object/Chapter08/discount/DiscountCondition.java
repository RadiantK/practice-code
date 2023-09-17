package book.object.Chapter08.discount;

import book.object.Chapter08.Screening;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
