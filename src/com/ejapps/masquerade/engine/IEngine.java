package com.ejapps.masquerade.engine;

import com.ejapps.masquerade.analysis.model.Feature;

public interface IEngine {

    Feature analyze(String text);

    Feature normalizeFeature(String text);

    String normalize(String text);

}
