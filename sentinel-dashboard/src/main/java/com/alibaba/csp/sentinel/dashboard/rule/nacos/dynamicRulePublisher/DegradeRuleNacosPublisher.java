package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRulePublisher;

import com.alibaba.csp.sentinel.dashboard.entity.rule.DegradeRuleEntity;
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
 * @date 2021-02-23 16:13:45
 * @description 往nacos配置中心推送sentinel控制台配置的熔断降级规则
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(DegradeRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<DegradeRuleEntity>, String> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;


    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        boolean flag = configService.publishConfig(app + nacosConfigProperties.getDegradeRuleSuffix(),
                nacosConfigProperties.getGroupId(), converter.convert(rules));
        String name = "失败";
        if (flag) {
            name = "成功";
        }
        logger.info("推送{}熔断降级规则" + name + "：\n{}", app,
                JSON.toJSONString(rules.stream().map(DegradeRuleEntity::toRule).collect(Collectors.toList()), true));
    }
}
