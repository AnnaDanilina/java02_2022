package hwJava0203;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class HomeWork0203 {
    public static void main(String[] args) {
        HashMap<String, Integer> hm;
        hm = new HashMap<String, Integer>();
        HashSet<String> hs = new HashSet<String>();
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("A1");
        a1.add("A1");
        a1.add("b2");
        a1.add("B3");
        a1.add("b2");
        a1.add("A1");
        a1.add("A");
        a1.add("A");
        Iterator<String> it1 = a1.iterator();
        while (it1.hasNext()) {
            String str1 = it1.next();
            int i;
            hs.add(str1);
            if (hm.containsKey(str1)) {
                i = hm.get(str1) + 1;
                hm.put(str1, i);
            } else {
                hm.put(str1, 1);
            }
        }
        System.out.println(hm);
        System.out.println(hs);

        PhoneBook pb = new PhoneBook();
        pb.add("Petrov","123");
        pb.add("Sidorov","123");
        pb.add("Petrov","456");
        pb.add("Petrov","890");
        pb.add("Sidorov","222");
        pb.add("Ivanov","777");

        System.out.println("Petrov " + pb.get("Petrov"));
        System.out.println("Ivanov" + pb.get("Ivanov"));
        System.out.println("Sidorov " + pb.get("Sidorov"));
    }
}