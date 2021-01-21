import java.util.*;
import java.util.stream.Collectors;

public class room_find {

    public static int getNthElement(int k, int[][] intervals){
        // counting cumulatively which line is it
        int cumulative_line_count = 0;
        int count = 0;
        int index = 0;
        int here = intervals[0][0];
        while( index < intervals.length ){
            // size of the interval
            int size = intervals[index][0] - here;
            // check if the value is here
//            System.out.println(cumulative_line_count + count * size);
            if ( k < cumulative_line_count + count * size ){
//                System.out.println(count);
                if( count == 0 ){
                    return here + ( k - cumulative_line_count);
                }
                return here + ( k - cumulative_line_count) / count;
            }
            // if not continue the search
            cumulative_line_count += count * size;
            count += intervals[index][1];
            here += size;
            index += 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int j = 1; j <= t; j++) {
            if(j != 1){
                scanner.nextLine();
            }
            // parsing s and f
            int s = scanner.nextInt();
            int f = scanner.nextInt();
            scanner.nextLine();
            // store the inputs as intervals
            int[][] intervals = new int[s][2];
            for (int i = 0; i < s; i++) {
                // parsing ui and vi
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                // storing it as a tuple in interval array
                intervals[i] = new int[]{u,v};
            }
            // hashMap to store endpoints of the intervals with counts
            HashMap<Integer, Integer> endpoints = new HashMap<>();

            for (int i = 0; i < intervals.length; i++) {
                int start = intervals[i][0];
                // counting the overlapping intervals update count as the value of hashmap
                endpoints.putIfAbsent(start,0);
                endpoints.put(start, endpoints.get(start) + 1 );
            }
            for (int i = 0; i < intervals.length; i++) {
                int limit = intervals[i][1] + 1;
                endpoints.putIfAbsent(limit,0);
                endpoints.put(limit, endpoints.get(limit) - 1 );
            }

            // idea from the source
            // https://stackoverflow.com/questions/13507925/nth-smallest-element-in-a-union-of-an-array-of-intervals-with-repetition

            // source code for changing 0 values of hashmap
            // https://www.techiedelight.com/remove-null-values-map-java/
            Iterator<Map.Entry<Integer,Integer>> itr = endpoints.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Integer,Integer> curr = itr.next();
                if (curr.getValue() == 0)
                    itr.remove();
            }

            // for sorting hashmap
            // source https://www.baeldung.com/java-hashmap-sort
            endpoints = endpoints.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            // convert hashmap to array
            int[][] lines = new int[endpoints.size()][2];
            Set entries = endpoints.entrySet();
            Iterator entriesIterator = entries.iterator();
            int i = 0;
            while(entriesIterator.hasNext()){
                Map.Entry<Integer,Integer> mapping = (Map.Entry<Integer,Integer>) entriesIterator.next();
                lines[i][0] = mapping.getKey();
                lines[i][1] = mapping.getValue();
                i++;
            }

            sb.append("Case #" + j + ":" + "\n");
            for (int m = 0; m < f; m++) {
                int r = scanner.nextInt();
                // get the nth element refer to function
                int room_number_of_friend = getNthElement( r - 1 , lines );
                sb.append(room_number_of_friend + "\n");
            }
        }
        System.out.println(sb.toString());
        scanner.close();
    }

}
