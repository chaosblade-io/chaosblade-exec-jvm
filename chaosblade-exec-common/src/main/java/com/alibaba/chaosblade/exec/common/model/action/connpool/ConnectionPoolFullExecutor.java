package com.alibaba.xblade.exec.common.model.action.connpool;

import com.alibaba.xblade.exec.common.model.action.ActionExecutor;
import javax.sql.DataSource;

/** @author Changjun Xiao */
public interface ConnectionPoolFullExecutor extends ActionExecutor {

  /**
   * Trigger connection pool full.
   *
   * @param dataSource
   */
  void full(DataSource dataSource);

  /** Revoke connection pool full. */
  void revoke();

  /**
   * Get max pool size from datasource
   *
   * @return
   */
  int getMaxPoolSize();
}
