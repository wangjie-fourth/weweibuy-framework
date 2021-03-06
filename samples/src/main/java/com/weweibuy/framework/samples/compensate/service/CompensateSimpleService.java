package com.weweibuy.framework.samples.compensate.service;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.annotation.Recover;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/14 14:27
 **/
@Service("compensateSimpleService")
@Slf4j
public class CompensateSimpleService {

    private Integer integer = 0;

    @Compensate(key = "CompensateSimpleService", recover = @Recover(beanName = "compensateSimpleService", method = "run2"))
    public String run() {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }


    @Compensate(key = "CompensateSimpleService_3")
    public String run3(Collection<User> users) {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_2", bizId = "#user.name + '_' + #dog.age")
    public String run3(User user, Dog dog) {
        if (integer < 2) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_4")
    public String run4(String name, String age) {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_5", bizId = "#user.name + '_' + #user.age",
            recover = @Recover(beanName = "compensateSimpleService", method = "run61"))
    public String run5(User user, Dog dog) {
        if (integer < 10) {
            integer++;
            throw new RuntimeException("...." + integer);
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_61")
    public String run61(User user, Dog dog) {
        if (integer < 20) {
            integer++;
            throw new RuntimeException("...." + integer);
        }
        integer = 0;
        return "success";
    }


    @Compensate(key = "CompensateSimpleService_6")
    public String run6(User user, Dog... dog) {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_7")
    public String run7(User... user) {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_8")
    public String run8(Map<String, User> userMap) {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        integer = 0;
        return "success";
    }


    private void run2(String str) {
        log.info("run2 .... {} {} {}", str);
    }
}
