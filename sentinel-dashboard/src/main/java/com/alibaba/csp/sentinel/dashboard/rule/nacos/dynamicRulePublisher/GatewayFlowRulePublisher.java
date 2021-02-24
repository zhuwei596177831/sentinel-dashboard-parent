package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRulePublisher;

import com.alibaba.csp.sentinel.dashboard.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
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
 * @date 2021-02-24 17:30:05
 * @description 往nacos配置中心推送gateway-sentinel控制台配置的流量控制规则配置
 */
@Component("gatewayFlowRuleNacosPublisher")
public class GatewayFlowRulePublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(GatewayFlowRulePublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<GatewayFlowRuleEntity>, String> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        boolean flag = configService.publishConfig(app + nacosConfigProperties.getGatewayFlowRuleSuffix(), nacosConfigProperties.getGroupId(), converter.convert(rules));
        String name = "失败";
        if (flag) {
            name = "成功";
        }
        logger.info("推送gateway:{}流量控制规则" + name + "：\n{}", app,
                JSON.toJSONString(rules.stream().map(GatewayFlowRuleEntity::toGatewayFlowRule).collect(Collectors.toList()), true));
    }
}
