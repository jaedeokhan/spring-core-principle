package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A 사용자가 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B 사용자가 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA : 사용자 A 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        // A는 10,000이 나오길 예상했지만 20,000이 나올거 같으니 테스트 코드 작성
        // 현재는 상태로 설계, 스프링은 항상 무상태로 설계
        Assertions.assertThat(statefulService1.getPrice()).isNotEqualTo(20000);
    }

    @Test
    void nonStatefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        int userAPrice = statefulService1.order("userA", 10000);
        int userBPrice =statefulService2.order("userB", 20000);

        // ThreadA : 사용자 A 주문 금액 조회
        System.out.println("userAPrice = " + userAPrice);
        System.out.println("userBPrice = " + userBPrice);

        // A는 10,000이 나오길 예상했지만 20,000이 나올거 같으니 테스트 코드 작성
        // 현재는 상태로 설계, 스프링은 항상 무상태로 설계
        Assertions.assertThat(userAPrice).isEqualTo(10000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}