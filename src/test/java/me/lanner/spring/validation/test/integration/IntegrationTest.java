package me.lanner.spring.validation.test.integration;

import me.lanner.spring.validation.pojo.PlainObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/me/lanner/spring/validation/test/integration/integration-test.xml")
public class IntegrationTest {

    @Autowired
    private PlainObject plainObject;

    @Test
    public void doTest() {
        try {
            plainObject.functionA(null);
        } catch (Exception ex) {
            assert ex.getMessage().equals("Str can't be null");
        }
    }
}
