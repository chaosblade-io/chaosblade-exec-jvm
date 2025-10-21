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

package com.alibaba.chaosblade.exec.plugin.mongodb;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Lingjian Xu */
public class MongoDBEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || object == null) {
      LOGGER.warn("The necessary parameters is null");
      return null;
    }

    Object namespace = ReflectUtil.getFieldValue(object, "namespace", false);

    String database = null;
    String collection = null;

    boolean isMongoClient =
        ReflectUtil.isAssignableFrom(
            classLoader, namespace.getClass(), "com.mongodb.MongoNamespace");
    if (isMongoClient) {
      database = ReflectUtil.invokeMethod(namespace, "getDatabaseName", new Object[0], false);
      collection = ReflectUtil.invokeMethod(namespace, "getCollectionName", new Object[0], false);
    }

    String sqlType = method.getName();

    MatcherModel matcherModel = new MatcherModel();

    if (sqlType != null) {
      switch (sqlType) {
        case ("find"):
        case ("findOneAndDelete"):
        case ("executeFindOneAndDelete"):
        case ("findOneAndReplace"):
        case ("executeFindOneAndReplace"):
        case ("findOneAndUpdate"):
        case ("executeFindOneAndUpdates"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "find");
          break;
        case ("drop"):
        case ("executeDrop"):
        case ("dropIndex"):
        case ("dropIndexes"):
        case ("executeDropIndex"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "drop");
          break;
        case ("insertOne"):
        case ("executeInsertOne"):
        case ("insertMany"):
        case ("executeInsertMany"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "insert");
          break;
        case ("deleteOne"):
        case ("deleteMany"):
        case ("executeDelete"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "delete");
          break;
        case ("updateOne"):
        case ("updateMany"):
        case ("executeUpdate"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "update");
          break;
        case ("createIndex"):
        case ("createIndexes"):
        case ("executeCreateIndexes"):
        case ("createDistinctIterable"):
        case ("createFindIterable"):
        case ("createAggregateIterable"):
        case ("createChangeStreamIterable"):
        case ("createMapReduceIterable"):
        case ("createListIndexesIterable"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "create");
          break;
        case ("doCount"):
        case ("doEstimatedCount"):
        case ("executeCount"):
        case ("estimatedDocumentCount"):
          matcherModel.add(MongoDBConstant.SQL_TYPE_MATCHER_NAME, "count");
          break;
        default:
          LOGGER.debug("unknown mongo operations: {}", sqlType);
      }
    }

    if (collection != null) {
      matcherModel.add(MongoDBConstant.COLLECTION_MATCHER_NAME, collection);
    }

    if (database != null) {
      matcherModel.add(MongoDBConstant.DATABASE_MATCHER_NAME, database);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("mongodb matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
