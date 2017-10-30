package com.ejapps.masquerade.analysis.model;

/**
 * @author EuiJin.Ham
 * @version 1.0.0
 * @description 문장의 특성을 추출하여 캡슐화하기 위한 클래스로 FeatureState 열거형을 통해 상태를 기술한다.
 */
public class Feature {

    /**
     * 원본 문자열
     */
    private String origin;
    /**
     * 정규화 문자열
     */
    private String normalized;
    /**
     * 현재 특성 객체의 상태를 기술하기 위한 변수
     */
    private FeatureState featureState = FeatureState.NEW;

    public Feature(String origin){
        this.origin = origin;
        this.featureState = FeatureState.DETERMINED;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        featureState = FeatureState.DETERMINED;
        this.origin = origin;
    }

    public String getNormalized() {
        return normalized;
    }

    public void setNormalized(String normalized) {
        featureState = FeatureState.NORMALIZED;
        this.normalized = normalized;
    }

    public FeatureState getFeatureState() {
        return featureState;
    }

    public void setFeatureState(FeatureState featureState) {
        this.featureState = featureState;
    }

}
