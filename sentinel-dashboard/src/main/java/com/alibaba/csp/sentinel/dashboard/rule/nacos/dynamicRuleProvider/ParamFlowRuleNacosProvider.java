package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRuleProvider;

import com.alibaba.csp.sentinel.dashboard.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 朱伟伟
 * @date 2021-02-23 17:12:10
 * @description 从nacos配置中心拉取sentinel热点参数规则
 */
@Component("paramFlowRuleNacosProvider")
public class ParamFlowRuleNacosProvider implements DynamicRuleProvider<List<ParamFlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(ParamFlowRuleNacosProvider.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<ParamFlowRule>> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public List<ParamFlowRuleEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + nacosConfigProperties.getParamFlowRuleSuffix(), nacosConfigProperties.getGroupId(), 3000);
        logger.info("拉取{}热点参数规则:\n{}", appName, rules);
        if (StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<ParamFlowRule> paramFlowRules = converter.convert(rules);
        List<ParamFlowRuleEntity> result = new ArrayList<>(paramFlowRules.size());
        for (ParamFlowRule paramFlowRule : paramFlowRules) {
            result.add(ParamFlowRuleEntity.fromAuthorityRule(appName, null, null, paramFlowRule));
        }
        return result;
    }

}
