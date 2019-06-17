package com.alibaba.chaosblade.exec.plugin.http.model;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http.model
 * @Date 2019-05-21 10:10
 */
public class HttpModelSpec extends FrameworkModelSpec {


    @Override
    public String getShortDesc() {
        return "http experiment";
    }

    @Override
    public String getLongDesc() {
        return "http experiment for testing service delay and exception.";
    }

    @Override
    public String getExample() {
        return "http delay --rest --time 1000 --uri http://127.0.0.1:8801/getName";
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new RestTemplateMatcherSpec());
        matcherSpecs.add(new HttpClient4MatcherSpec());
        matcherSpecs.add(new HttpClient3MatcherSpec());
        matcherSpecs.add(new UriMatcherDefSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return "http";
    }
}
