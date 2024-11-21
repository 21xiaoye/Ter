package com.cabin.ter.strategy;

import com.cabin.ter.constants.enums.CommonErrorEnum;
import com.cabin.ter.util.AsserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     消息工厂，出来不同的消息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 14:44
 */
public class MsgHandlerFactory {

    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy){
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategyNoNull(Integer code){
        AbstractMsgHandler abstractMsgHandler = STRATEGY_MAP.get(code);
        AsserUtil.isEmpty(abstractMsgHandler, CommonErrorEnum.PARAM_VALID.getMessage());
        return abstractMsgHandler;
    }
}
