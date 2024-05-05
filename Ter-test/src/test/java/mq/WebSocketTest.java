package mq;

import com.cabin.ter.TerApplication;
import com.cabin.ter.common.constants.participant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
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
        WebSocketSingleParticipant msgAgreement = new WebSocketSingleParticipant();
        redisUtil.push(TopicConstant.REDIS_USER_MESSAGE_PUSH, MsgUtil.obj2Json(msgAgreement));
    }
}
