package com.alibaba.chaosblade.exec.plugin.elasticsearch.index;

import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qianfan
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index
 * @Date 2020/11/4 4:51 下午
 */
public final class RequestIndexProvider {
    private static Map<String, AbstractRequestIndex> registry = new HashMap<String, AbstractRequestIndex>();

    static {
        registry.put("org.elasticsearch.client.indices.CreateIndexRequest", new CreateIndexRequestIndex());
        registry.put("org.elasticsearch.client.indices.GetIndexRequest", new GetIndexRequestIndex());
        registry.put("org.elasticsearch.action.search.SearchRequest", new SearchRequestIndex());
        registry.put("org.elasticsearch.action.index.IndexRequest", new IndexRequestIndex());
        registry.put("org.elasticsearch.action.get.GetRequest", new GetRequestIndex());
        registry.put("org.elasticsearch.action.update.UpdateRequest", new UpdateRequestIndex());
        registry.put("org.elasticsearch.action.delete.DeleteRequest", new DeleteRequestIndex());
        registry.put("org.elasticsearch.action.suggest.SuggestRequest", new SuggestRequestIndex());
        registry.put("org.elasticsearch.action.bulk.BulkRequest", new BulkRequestIndex());
        registry.put("org.elasticsearch.action.explain.ExplainRequest", new ExplainRequestIndex());
        registry.put("org.elasticsearch.action.termvectors.TermVectorsRequest", new TermVectorsRequestIndex());
        registry.put("org.elasticsearch.action.termvectors.MultiTermVectorsRequest", new MultiTermVectorsRequestIndex());
        registry.put("org.elasticsearch.action.percolate.PercolateRequest", new PercolateRequestIndex());
        registry.put("org.elasticsearch.action.percolate.MultiPercolateRequest", new MultiPercolateRequestIndex());
        registry.put("org.elasticsearch.action.get.MultiGetRequest", new MultiGetRequestIndex());
    }


    public static AbstractRequestIndex get(Object target) {
        if (target == null) {
            return null;
        }
        return registry.get(target.getClass().getName());
    }

    public static boolean isRequest(Object target){
        if (target == null) {
            return false;
        }
        return registry.get(target.getClass().getName()) != null;
    }
}
