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

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ManyNameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import java.util.HashSet;
import java.util.Set;

/** @author Lingjian Xu */
public class MongoDBPointCut implements PointCut {

  private static final String MONGODB_COLLECTION_IMPL_CLASS_NEW =
      "com.mongodb.client.internal.MongoCollectionImpl";
  private static final String MONGODB_COLLECTION_IMPL_CLASS_OLD = "com.mongodb.MongoCollectionImpl";
  public static Set<String> enhanceMethodSet = new HashSet<String>();

  static {
    enhanceMethodSet.add("find");
    enhanceMethodSet.add("findOneAndDelete");
    enhanceMethodSet.add("executeFindOneAndDelete");
    enhanceMethodSet.add("findOneAndReplace");
    enhanceMethodSet.add("executeFindOneAndReplace");
    enhanceMethodSet.add("findOneAndUpdate");
    enhanceMethodSet.add("executeFindOneAndUpdates");

    enhanceMethodSet.add("drop");
    enhanceMethodSet.add("executeDrop");
    enhanceMethodSet.add("dropIndex");
    enhanceMethodSet.add("dropIndexes");
    enhanceMethodSet.add("executeDropIndex");

    enhanceMethodSet.add("insertOne");
    enhanceMethodSet.add("executeInsertOne");
    enhanceMethodSet.add("insertMany");
    enhanceMethodSet.add("executeInsertMany");

    enhanceMethodSet.add("deleteOne");
    enhanceMethodSet.add("deleteMany");
    enhanceMethodSet.add("executeDelete");

    enhanceMethodSet.add("updateOne");
    enhanceMethodSet.add("updateMany");
    enhanceMethodSet.add("executeUpdate");

    enhanceMethodSet.add("createIndex");
    enhanceMethodSet.add("createIndexes");
    enhanceMethodSet.add("executeCreateIndexes");
    enhanceMethodSet.add("createDistinctIterable");
    enhanceMethodSet.add("createFindIterable");
    enhanceMethodSet.add("createAggregateIterable");
    enhanceMethodSet.add("createChangeStreamIterable");
    enhanceMethodSet.add("createMapReduceIterable");
    enhanceMethodSet.add("createListIndexesIterable");

    enhanceMethodSet.add("doCount");
    enhanceMethodSet.add("doEstimatedCount");
    enhanceMethodSet.add("executeCount");
    enhanceMethodSet.add("estimatedDocumentCount");
  }

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher orClassMatcher = new OrClassMatcher();
    orClassMatcher
        .or(new NameClassMatcher(MONGODB_COLLECTION_IMPL_CLASS_NEW))
        .or(new NameClassMatcher(MONGODB_COLLECTION_IMPL_CLASS_OLD));
    return orClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher.or(new ManyNameMethodMatcher(enhanceMethodSet));
    return orMethodMatcher;
  }
}
