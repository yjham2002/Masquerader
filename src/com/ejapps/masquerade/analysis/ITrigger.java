package com.ejapps.masquerade.analysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a on 2017-05-11.
 */
public interface ITrigger extends Serializable{
    public boolean run(HashMap<String, Object> extra, List<String> ref);
}
