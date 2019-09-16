package com.xs.middle.compent.unittest;

import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@RunWith(MySpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        MockitoTestExecutionListener.class,
        UnitTestExecutionListener.class})
public class UnitTestApplication {
}
