import java.util.*;



public class ProblemaP3 {

    private static class Edge {
        int dest;       
        char lastChar;  
        Edge(int d, char c) { dest = d; lastChar = c; }
    }

    private static int getNodeId(String node,
                                 HashMap<String,Integer> map,
                                 ArrayList<String> list,
                                 ArrayList<ArrayDeque<Edge>> adj) {
        Integer id = map.get(node);
        if (id == null) {
            id = list.size();
            map.put(node, id);
            list.add(node);
            adj.add(new ArrayDeque<>()); 
        }
        return id;
    }

    private static String buildComponentString(int startId,
                                               ArrayList<ArrayDeque<Edge>> adj,
                                               ArrayList<String> idToStr) {
        Deque<Integer> st = new ArrayDeque<>();
        Deque<Character> edgeChars = new ArrayDeque<>();
        StringBuilder pathChars = new StringBuilder();

        st.push(startId);
        while (!st.isEmpty()) {
            int u = st.peek();
            if (!adj.get(u).isEmpty()) {
                Edge e = adj.get(u).pollFirst();
                st.push(e.dest);
                edgeChars.push(e.lastChar);
            } else {
                st.pop();
                if (!edgeChars.isEmpty()) {
                    pathChars.append(edgeChars.pop());
                }
            }
        }
        pathChars.reverse();
        return idToStr.get(startId) + pathChars.toString();
    }

    private static int overlap(String a, String b, int overlapMax) {
        int max = Math.min(overlapMax, Math.min(a.length(), b.length()));
        for (int len = max; len > 0; --len) {
            if (a.regionMatches(a.length() - len, b, 0, len)) return len;
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            int k = sc.nextInt();

            HashMap<String,Integer> idMap = new HashMap<>(2 * n);
            ArrayList<String> idToStr = new ArrayList<>();
            ArrayList<ArrayDeque<Edge>> adj = new ArrayList<>();

            for (int i = 0; i < n; ++i) {
                String s = sc.next();
                String prefix = s.substring(0, k - 1);
                String suffix = s.substring(1);

                int u = getNodeId(prefix, idMap, idToStr, adj);
                int v = getNodeId(suffix, idMap, idToStr, adj);
                adj.get(u).addLast(new Edge(v, s.charAt(k - 1)));
            }

            ArrayList<String> componentStrings = new ArrayList<>();
            for (int node = 0; node < adj.size(); ++node) {
                if (!adj.get(node).isEmpty()) {
                    componentStrings.add(buildComponentString(node, adj, idToStr));
                }
            }

            String answer;
            if (componentStrings.isEmpty()) {
                answer = "";
            } else if (componentStrings.size() == 1) {
                answer = componentStrings.get(0);
            } else {
                answer = greedySuperstring(componentStrings, k);
            }

            System.out.println(answer);
        }
        sc.close();
    }

    private static String greedySuperstring(ArrayList<String> comps, int k) {
        while (comps.size() > 1) {
            int bestOv = -1, bestI = 0, bestJ = 1;
            String bestMerge = null;

            for (int i = 0; i < comps.size(); ++i) {
                for (int j = 0; j < comps.size(); ++j) {
                    if (i == j) continue;
                    int ov = overlap(comps.get(i), comps.get(j), k - 1);
                    String merged = comps.get(i) + comps.get(j).substring(ov);
                    if (ov > bestOv || (ov == bestOv && (bestMerge == null || merged.compareTo(bestMerge) < 0))) {
                        bestOv = ov;
                        bestI = i;
                        bestJ = j;
                        bestMerge = merged;
                    }
                }
            }

            ArrayList<String> next = new ArrayList<>(comps.size() - 1);
            for (int idx = 0; idx < comps.size(); ++idx) {
                if (idx != bestI && idx != bestJ) next.add(comps.get(idx));
            }
            next.add(bestMerge);
            comps = next;
        }
        return comps.get(0);
    }
}