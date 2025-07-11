package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService service;

    @Test
    void runtimeException() {
        assertThatThrownBy(() -> service.runtimeException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        assertThatThrownBy(() -> service.checkedException()).isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor() {
        assertThatThrownBy(() -> service.rollbackFor()).isInstanceOf(MyException.class);
    }


    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService {

        // runtime exception: rollback
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        // checked exception: commit
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // unchecked exception, rollbackFor 지정: rollback
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}
