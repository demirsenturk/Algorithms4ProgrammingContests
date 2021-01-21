public class test {
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE);
        int[] array = new int[Integer.MAX_VALUE];
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            array[i] = i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("1000 1000\n");
        for (int i = 0; i < 1000; i++) {
            sb.append( i + " " + (i*(1000-i)) + "\n");
        }
        for (int i = 0; i < 1000; i++) {
            sb.append( i + "\n");
        }
        System.out.println(sb.toString());
    }
}
