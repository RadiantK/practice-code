package book.object.Chapter08.discount;

import book.object.Chapter08.Money;
import book.object.Chapter08.Screening;

public class NoneDiscountPolicy extends DiscountPolicy {
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
