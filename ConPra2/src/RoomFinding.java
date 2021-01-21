import java.util.*;

public class RoomFinding {

    static class line {
        // inner class for lines in order to store
        public int room_number;
        public int station;

        public line(int room_number, int station) {
            this.room_number = room_number;
            this.station = station;
        }

        public int getRoom_number() {
            return room_number;
        }

        public int getStation() {
            return station;
        }
    }

    private static void order(List<line> lines) {
        // sort function for list
        Collections.sort( lines, new Comparator() {
            // comparation first with roomNr then with stationNr
            public int compare(Object o1, Object o2) {

                Integer x1 = ((line) o1).getRoom_number();
                Integer x2 = ((line) o2).getRoom_number();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                Integer y1 = ((line) o1).getStation();
                Integer y2 = ((line) o2).getStation();
                return y1.compareTo(y2);
            }});
    }
    // source of the above method https://stackoverflow.com/questions/4805606/how-to-sort-by-two-fields-in-java

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
            // create a list to store all lines
            List<line> lines = new ArrayList<line>();
            // parsing the line information
            for (int i = 1; i <= s; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                for (int k = u; k <= v ; k++) {
                    // creating and adding the lines with u and v
                    lines.add(new line(k,i));
                }
            }
            // sort the arraylist
            order(lines);
            sb.append("Case #" + j + ":" + "\n");
            for (int i = 0; i < f; i++) {
                int r = scanner.nextInt();
                int room_number_of_friend = lines.get(r-1).getRoom_number();
                sb.append(room_number_of_friend + "\n");
            }
        }
        System.out.println(sb.toString());
    }
}
