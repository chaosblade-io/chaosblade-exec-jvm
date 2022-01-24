package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

public enum BusinessParamPatternEnum {
    AND("and", new BusinessParamAndMatcher()),
    OR("or", new BusinessParamOrMatcher()),
    NOT("not", new BusinessParamNotMatcher());

    BusinessParamPatternEnum(String pattern, BusinessParamPatternMatcher patternMatcher) {
        this.pattern = pattern;
        this.patternMatcher = patternMatcher;
    }

    private String pattern;

    private BusinessParamPatternMatcher patternMatcher;

    public String getPattern() {
        return pattern;
    }

    public BusinessParamPatternMatcher getPatternMatcher() {
        return patternMatcher;
    }

    public static BusinessParamPatternMatcher getPatternMatcher(String pattern) {
        for (BusinessParamPatternEnum t : BusinessParamPatternEnum.values()) {
            if (t.getPattern().equalsIgnoreCase(pattern)) {
                return t.getPatternMatcher();
            }
        }
        return null;
    }
}
