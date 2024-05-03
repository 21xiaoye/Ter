package mq;

import com.cabin.ter.TerApplication;
import com.cabin.ter.common.constants.entity.msg.WebSocketParticipant;
import com.cabin.ter.common.constants.enums.ClusterTopicEnum;
import com.cabin.ter.common.util.MsgUtil;
import com.cabin.ter.common.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TerApplication.class)
@RunWith(SpringRunner.class)
public class WebSocketTest {
    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void test(){
        WebSocketParticipant msgAgreement = new WebSocketParticipant();
        redisUtil.push(ClusterTopicEnum.REDIS_USER_MESSAGE_PUSH.getMessage(), MsgUtil.obj2Json(msgAgreement));
    }
}
