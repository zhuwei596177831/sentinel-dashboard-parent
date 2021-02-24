package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRulePublisher;

import com.alibaba.csp.sentinel.dashboard.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 朱伟伟
 * @date 2021-02-23 17:13:52
 * @description 往nacos配置中心推送sentinel控制台配置的热点参数规则
 */
@Component("paramFlowRuleNacosPublisher")
public class ParamFlowRuleNacosPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(ParamFlowRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<ParamFlowRule>, String> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public void publish(String app, List<ParamFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        List<ParamFlowRule> realRules = rules.stream().map(ParamFlowRuleEntity::getRule).collect(Collectors.toList());
        boolean flag = configService.publishConfig(app + nacosConfigProperties.getParamFlowRuleSuffix(),
                nacosConfigProperties.getGroupId(), converter.convert(realRules));
        String name = "失败";
        if (flag) {
            name = "成功";
        }
        logger.info("推送{}热点参数规则" + name + "：\n{}", app, JSON.toJSONString(realRules, true));
    }
}
