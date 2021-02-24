package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRuleProvider;

import com.alibaba.csp.sentinel.dashboard.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
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
 * @date 2021-02-23 15:59:41
 * @description 从nacos配置中心拉取sentinel熔断降级规则
 */
@Component("degradeRuleNacosProvider")
public class DegradeRuleNacosProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(DegradeRuleNacosProvider.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<DegradeRuleEntity>> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + nacosConfigProperties.getDegradeRuleSuffix(), nacosConfigProperties.getGroupId(), 3000);
        logger.info("拉取{}熔断降级规则:\n{}", appName, rules);
        if (StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
