import java.util.*;


public class ProblemaP3 {

    private static int overlap(String a, String b, int k) {
        int max = Math.min(k - 1, Math.min(a.length(), b.length()));
        for (int len = max; len > 0; --len)
            if (a.regionMatches(a.length() - len, b, 0, len)) return len;
        return 0;
    }

    private static String greedySuperstring(List<String> words, int k) {
        /*
         * Mientras haya ≥2 cadenas:
         *   1) Encuentre par (i,j) con solapamiento máximo.
         *   2) En empate → fusión lexicográficamente menor.
         *   3) Reemplace i,j por la fusión.
         * Termina con la única cadena.
         */
        while (words.size() > 1) {
            int bestOv = -1, bestI = 0, bestJ = 1;
            String bestMerge = null;

            int m = words.size();
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < m; ++j) {
                    if (i == j) continue;
                    int ov = overlap(words.get(i), words.get(j), k);
                    String merged = words.get(i) + words.get(j).substring(ov);
                    if (ov > bestOv || (ov == bestOv && merged.compareTo(bestMerge) < 0)) {
                        bestOv = ov;
                        bestI = i;
                        bestJ = j;
                        bestMerge = merged;
                    }
                }
            }

            List<String> next = new ArrayList<>(m - 1);
            for (int idx = 0; idx < m; ++idx)
                if (idx != bestI && idx != bestJ) next.add(words.get(idx));
            next.add(bestMerge);
            words = next;
        }
        return words.get(0);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        while (T-- > 0) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            List<String> strings = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) strings.add(sc.next());

            String answer;
            if (n == 0) answer = "";
            else if (n == 1) answer = strings.get(0);
            else answer = greedySuperstring(strings, k);

            System.out.println(answer);
        }
        sc.close();
    }
}
