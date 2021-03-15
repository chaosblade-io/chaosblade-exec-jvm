package com.alibaba.chaosblade.exec.plugin.servlet.code;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yefei
 */
public class ModifyHttpCodeActionExecutor implements ActionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ModifyHttpCodeActionExecutor.class);

    private static final String HTTP_RESPONSE_CLASS_NAME = "javax.servlet.http.HttpServletResponse";

    private HttpCodeFlagSpec httpCodeFlagSpec;

    public ModifyHttpCodeActionExecutor(HttpCodeFlagSpec httpCodeFlagSpec) {
        this.httpCodeFlagSpec = httpCodeFlagSpec;
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        Object[] methodArguments = enhancerModel.getMethodArguments();
        if (methodArguments.length < 2) {
            logger.warn("un support modify");
            return;
        }
        ReflectUtil.invokeMethod(methodArguments[1], "setStatus",
                new Object[]{Integer.parseInt(enhancerModel.getActionFlag(httpCodeFlagSpec.getName()))});

        InterruptProcessException.throwReturnImmediately(null);
    }
}
