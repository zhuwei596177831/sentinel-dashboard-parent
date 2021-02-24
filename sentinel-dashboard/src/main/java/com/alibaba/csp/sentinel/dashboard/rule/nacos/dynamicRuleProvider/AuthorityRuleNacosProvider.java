package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRuleProvider;

import com.alibaba.csp.sentinel.dashboard.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
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
 * @date 2021-02-23 16:51:49
 * @description 从nacos配置中心拉取sentinel访问控制规则
 */
@Component("authorityRuleNacosProvider")
public class AuthorityRuleNacosProvider implements DynamicRuleProvider<List<AuthorityRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(AuthorityRuleNacosProvider.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<AuthorityRule>> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public List<AuthorityRuleEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + nacosConfigProperties.getAuthorityRuleSuffix(), nacosConfigProperties.getGroupId(), 3000);
        logger.info("拉取{}访问控制规则:\n{}", appName, rules);
        if (StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<AuthorityRule> authorityRules = converter.convert(rules);
        List<AuthorityRuleEntity> result = new ArrayList<>(authorityRules.size());
        for (AuthorityRule authorityRule : authorityRules) {
            result.add(AuthorityRuleEntity.fromAuthorityRule(appName, null, null, authorityRule));
        }
        return result;
    }
}
