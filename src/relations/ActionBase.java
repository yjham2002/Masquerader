package relations;

import kr.co.shineware.util.common.model.Pair;
import services.CommonSVC;
import utils.KoreanUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by a on 2017-05-12.
 */
public class ActionBase extends HashMap<String, ActionFraction> {

    private CommonSVC commonSVC;

    {
        commonSVC = new CommonSVC();
    }

    public ActionBase(){
        super();
        wakeUp();
    }

    public Pair<String, Double> getIntentionProbPair(String keyValue){
        Pair<String, Double> pair = new Pair<>();

        double prob = 0.0; // 유사도 척도
        String prediction = ""; // 예측된 사용자 의도

        prediction = "";
        prob = 0.0;
        for (String str : this.keySet()) {
            double newProb = KoreanUtil.getEditDistanceRate(str, keyValue, true);
            if (prob < newProb) {
                prediction = this.get(str).getIntentionCode();
                prob = newProb;
            }
        }

        pair.setFirst(prediction);
        pair.setSecond(prob);

        return pair;
    }

    private void wakeUp(){
        List<ActionFraction> list = commonSVC.getActions();
        for(ActionFraction actionFraction : list){
            this.put(actionFraction.getKeyValue(), actionFraction);
        }
        System.out.println("[INFO :: ActionBase is ready to use.]");
    }

}
