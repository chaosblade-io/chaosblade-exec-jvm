package com.alibaba.chaosblade.exec.plugin.elasticsearch.index;

import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRequestIndex implements RequestIndex {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRequestIndex.class);

    @Override
    public String getName() {
        return "abstract";
    }

    @Override
    public List<String> getIndex(Object target) throws Exception{
        return getIndex0(target);
    }

    public String getIndexOfString(Object target) {
        try{
            List<String> lists = getIndex0(target);
            if (lists.size() < 1) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < lists.size(); i++) {
                stringBuilder.append(lists.get(i));
                if (i < lists.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        }catch(Exception e){
            logger.error("elasticsearch plugin error : " + e.getMessage(), e);
            return null;
        }
    }

    public abstract List<String> getIndex0(Object target) throws Exception;

    protected List<String> indexList(List list){
        List<String> indexes = new ArrayList<String>();
        try{
            for (Object req : list) {
                if (req == null) {
                    continue;
                }
                RequestIndex requestIndex = RequestIndexProvider.get(req);
                if (requestIndex != null) {
                    indexes.addAll(requestIndex.getIndex(req));
                }
            }
        }catch(Exception e){
            logger.error("elasticsearch plugin error : " + e.getMessage(), e);
        }
        return  indexes;
    }

    public String index(Object target, String methodName) throws Exception{
        return ReflectUtil.invokeMethod(target, methodName);
    }
}
