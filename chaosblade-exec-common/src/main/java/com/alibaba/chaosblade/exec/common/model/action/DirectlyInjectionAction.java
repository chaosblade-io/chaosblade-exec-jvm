package com.alibaba.chaosblade.exec.common.model.action;

import com.alibaba.chaosblade.exec.common.model.Model;

/**
 * @author haibin
 * @date 2019-04-19
 */
public interface DirectlyInjectionAction {

    /**
     * create injection
     *
     * @param uid   the injection uid
     * @param model the injection model
     * @throws Exception
     */
    public void createInjection(String uid, Model model) throws Exception;

    /**
     * destroy injection
     *
     * @param uid   the injection uid
     * @param model the injection model
     * @throws Exception
     */
    public void destroyInjection(String uid, Model model) throws Exception;
}
