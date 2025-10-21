/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.clickhouse;

import com.alibaba.chaosblade.exec.common.aop.*;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.SimplePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clickhouse的插件
 *
 * @author liuhq
 */
public class ClickhousePlugin implements Plugin {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClickhousePlugin.class);

  private static final String TARGET_NAME = "ck";
  private static final String GETSERVER_METHOD = "getServer";
  private static final String GETDATABASE_METHOD = "getDatabase";
  private static final String GETCLUSTER_METHOD = "getCluster";
  private static final String GETADDRESS_METHOD = "getAddress";
  private static final Class<InetSocketAddress> ADDRESS_CLAZZ = InetSocketAddress.class;
  private static final String TABLE_MATCH_NAME = "table";
  private static final String CLUSTER_MATCH_NAME = "cluster";
  private static final String HOST_MATCH_NAME = "host";
  private static final String PORT_MATCH_NAME = "port";
  private static final String DATABASE_MATCH_NAME = "database";
  private static final String CK_CLASS_HTTP = "com.clickhouse.client.http.ClickHouseHttpClient";
  private static final String CK_CLASS_GRPC = "com.clickhouse.client.grpc.ClickHouseGrpcClient";
  private static final String CK_METHOD = "execute";
  private static final String PARAM_FIELD = "sql";
  private static final String PARAM_CLASS = "com.clickhouse.client.ClickHouseRequest";

  @Override
  public String getName() {
    return TARGET_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new FrameworkModelSpec() {
      @Override
      protected List<MatcherSpec> createNewMatcherSpecs() {
        MatcherSpec tableMatcher =
            new SimplePredicateMatcherSpec(TABLE_MATCH_NAME, "The first table name in sql");
        MatcherSpec databaseMatcher =
            new SimplePredicateMatcherSpec(DATABASE_MATCH_NAME, "The database which used");
        MatcherSpec clusterMatcher =
            new SimplePredicateMatcherSpec(CLUSTER_MATCH_NAME, "The cluster which used");
        MatcherSpec hostMatcher =
            new SimplePredicateMatcherSpec(HOST_MATCH_NAME, "The host which used");
        MatcherSpec portMatcher =
            new SimplePredicateMatcherSpec(PORT_MATCH_NAME, "The ip which used");
        List<MatcherSpec> res = new ArrayList<MatcherSpec>();
        res.add(tableMatcher);
        res.add(databaseMatcher);
        res.add(clusterMatcher);
        res.add(hostMatcher);
        res.add(portMatcher);
        return res;
      }

      @Override
      public String getTarget() {
        return TARGET_NAME;
      }

      @Override
      public String getShortDesc() {
        return "Clickhouse experiment ";
      }

      @Override
      public String getLongDesc() {
        return "Clickhouse experiment contains delay and exception by table name and so on";
      }
    };
  }

  @Override
  public PointCut getPointCut() {
    return new PointCut() {
      @Override
      public ClassMatcher getClassMatcher() {
        OrClassMatcher orClassMatcher = new OrClassMatcher();
        orClassMatcher
            .or(new NameClassMatcher(CK_CLASS_GRPC))
            .or(new NameClassMatcher(CK_CLASS_HTTP));
        return orClassMatcher;
      }

      @Override
      public MethodMatcher getMethodMatcher() {
        return new OrMethodMatcher().or(new NameMethodMatcher(CK_METHOD));
      }
    };
  }

  @Override
  public Enhancer getEnhancer() {

    return new BeforeEnhancer() {
      @Override
      public EnhancerModel doBeforeAdvice(
          ClassLoader classLoader,
          String className,
          Object object,
          Method method,
          Object[] methodArguments)
          throws Exception {
        if (methodArguments == null || methodArguments.length == 0) {
          LOGGER.info(
              "The necessary parameters is empty, {}",
              methodArguments != null ? methodArguments.length : null);
          return null;
        }
        Object methodArgument = methodArguments[0];
        boolean assignableFrom =
            ReflectUtil.isAssignableFrom(classLoader, methodArgument.getClass(), PARAM_CLASS);
        if (!assignableFrom) {
          return null;
        }
        String database = null;
        String cluster = null;
        String host = null;
        String port = null;
        String table = null;

        try {
          // 读取server
          Object server = ReflectUtil.invokeMethod(methodArgument, GETSERVER_METHOD);
          // 读取database
          Optional<String> databaseOpt = ReflectUtil.invokeMethod(server, GETDATABASE_METHOD);
          database = databaseOpt.get();
          // 读取cluster
          cluster = ReflectUtil.invokeMethod(server, GETCLUSTER_METHOD);
          // 读取IP
          Object address = ReflectUtil.invokeMethod(server, GETADDRESS_METHOD);
          if (address.getClass().isAssignableFrom(ADDRESS_CLAZZ)) {
            host = ((InetSocketAddress) address).getHostName();
            port = String.valueOf(((InetSocketAddress) address).getPort());
          }

          // 读取SQL字段信息,获取table
          String fieldValue = ReflectUtil.getFieldValue(methodArgument, PARAM_FIELD, true);
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sql ck: {}", fieldValue);
          }
          if (StringUtil.isBlank(fieldValue)) {
            return null;
          }
          table = SQLParserUtil.findTableName(fieldValue);
        } catch (Exception e) {
          LOGGER.error("reflect invoke error", e);
        }

        MatcherModel matcherModel = new MatcherModel();
        if (!StringUtil.isBlank(table)) {
          matcherModel.add(TABLE_MATCH_NAME, table);
        }
        if (!StringUtil.isBlank(database)) {
          matcherModel.add(DATABASE_MATCH_NAME, table);
        }
        if (!StringUtil.isBlank(cluster)) {
          matcherModel.add(CLUSTER_MATCH_NAME, cluster);
        }
        if (!StringUtil.isBlank(host)) {
          matcherModel.add(HOST_MATCH_NAME, host);
        }
        if (!StringUtil.isBlank(port)) {
          matcherModel.add(PORT_MATCH_NAME, port);
        }

        LOGGER.info(
            "table database cluster host host : {},{},{},{},{}",
            table,
            database,
            cluster,
            host,
            port);

        return new EnhancerModel(classLoader, matcherModel);
      }
    };
  }
}
