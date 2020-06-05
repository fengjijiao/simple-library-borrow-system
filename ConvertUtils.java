package test7;

import java.util.ArrayList;

public class ConvertUtils {
    public static Object[][] ArrayListToObject2D(ArrayList<Object[]> arrayList) {
        Object[][] objset = new Object[arrayList.size()][];
        for (int i=0;i<arrayList.size();i++) {
            objset[i] = arrayList.get(i);
        }
        return objset;
    }
}
