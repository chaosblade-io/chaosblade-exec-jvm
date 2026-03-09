package com.otel.business.c.exec.plugin.elasticsearch.index.impl;

import com.otel.business.c.exec.common.util.ReflectUtil;
import com.otel.business.c.exec.plugin.elasticsearch.index.AbstractRequestIndex;
import java.util.List;

/**
 * @Author Yuhan Tang
 *
 * @package: com.otel.business.c.exec.plugin.elasticsearch.index.impl @Date 2020-10-30 16:16
 */
public class MultiPercolateRequestIndex extends AbstractRequestIndex {
  @Override
  public String getName() {
    return "multiPercolate";
  }

  @Override
  public List<String> getIndex0(Object target) throws Exception {
    List list = ReflectUtil.invokeMethod(target, "requests");
    return indexList(list);
  }
}
