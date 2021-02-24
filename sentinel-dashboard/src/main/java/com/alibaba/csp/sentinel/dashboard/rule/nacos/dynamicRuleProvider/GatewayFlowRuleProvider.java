package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRuleProvider;

import com.alibaba.csp.sentinel.dashboard.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 朱伟伟
 * @date 2021-02-24 17:26:02
 * @description 从nacos配置中心拉取gateway-sentinel流量控制规则配置
 */
@Component("gatewayFlowRuleNacosProvider")
public class GatewayFlowRuleProvider implements DynamicRuleProvider<List<GatewayFlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(GatewayFlowRuleProvider.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<GatewayFlowRuleEntity>> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public List<GatewayFlowRuleEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + nacosConfigProperties.getGatewayFlowRuleSuffix(), nacosConfigProperties.getGroupId(), 3000);
        logger.info("拉取gateway:{}流量控制规则:\n{}", appName, rules);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
