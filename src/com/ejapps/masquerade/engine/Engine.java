package com.ejapps.masquerade.engine;

import com.ejapps.masquerade.constants.PosTag;

public abstract class Engine implements IEngine{

    protected String clearParticle(String normalized){
        for(String s : PosTag.PARTICLES) normalized = normalized.replaceAll(s, "");

        return normalized;
    }

}
