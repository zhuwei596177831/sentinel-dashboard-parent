/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRuleProvider;

import com.alibaba.csp.sentinel.dashboard.entity.rule.FlowRuleEntity;
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
 * @author Eric Zhao
 * @author zhuweiwei
 * @description 从nacos配置中心拉取sentinel流量控制规则配置
 * @since 1.4.0
 */
@Component("flowRuleNacosProvider")
public class FlowRuleNacosProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(FlowRuleNacosProvider.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<FlowRuleEntity>> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
//        String rules = configService.getConfig(appName + NacosConfigUtil.FLOW_DATA_ID_POSTFIX, NacosConfigUtil.GROUP_ID, 3000);
        String rules = configService.getConfig(appName + nacosConfigProperties.getFlowRuleSuffix(), nacosConfigProperties.getGroupId(), 3000);
        logger.info("拉取{}流量控制规则:\n{}", appName, rules);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
