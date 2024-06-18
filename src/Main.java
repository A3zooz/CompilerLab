import java.io.*;
import java.util.*;


public class Main {
    static PrintWriter pw;

    static class Scanner {
        StringTokenizer st;
        BufferedReader br;

        public Scanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }

        public String next() throws IOException {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        public String nextLine() throws IOException {
            return br.readLine();
        }

        public double nextDouble() throws IOException {
            String x = next();
            StringBuilder sb = new StringBuilder("0");
            double res = 0, f = 1;
            boolean dec = false, neg = false;
            int start = 0;
            if (x.charAt(0) == '-') {
                neg = true;
                start++;
            }
            for (int i = start; i < x.length(); i++)
                if (x.charAt(i) == '.') {
                    res = Long.parseLong(sb.toString());
                    sb = new StringBuilder("0");
                    dec = true;
                } else {
                    sb.append(x.charAt(i));
                    if (dec)
                        f *= 10;
                }
            res += Long.parseLong(sb.toString()) / f;
            return res * (neg ? -1 : 1);
        }

        public boolean ready() throws IOException {
            return br.ready();
        }
    }

    static final int MOD = 1000000007;

    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(System.out);
        int x = 10;
        int y = x++ * 2 + --x - x-- % 2;
        int z = ++x +(x-- % 3) * y;
        pw.println("x: " +x+" y: " +y +" z: "+z);
        pw.println(x+y+z);
        pw.flush();
        pw.close();
    }

    public static String numberofBills(Integer[] billCount, String[] billValue, Integer withdrawAmount) {
        //Insert your code here 
        ArrayList<Integer> billVal = new ArrayList<>();
        String res = "";
        for(String x: billValue){
            billVal.add(Integer.parseInt(x));
        }
        int index = billCount.length-1;
        while(withdrawAmount>0 && index >= 0){
            if(withdrawAmount < billVal.get(index)){
                index--;
                continue;

            }
            if(billCount[index] < 1){
                index--;
                continue;
            }
            int count = 0;
            while(withdrawAmount >= billVal.get(index) && billCount[index] > 0){
                if(withdrawAmount % billVal.get(index) == 0){
                    withdrawAmount = 0;
                    count++;
                    billCount[index]--;

                    break;
                }
                else{
                withdrawAmount -= billVal.get(index);
                }
                count++;
                billCount[index]--;
            }
            if(count!=0)
            res += billVal.get(index)+"x"+count +",";
            
        }
        return res.substring(0,res.length()-1);
    }
















    static final String TRANSFER_TO_CONSTANT = "transfer to";
    static final String TRANSFER_FROM_CONSTANT = "transfer from";
	
	public static String trucateStr(String trxDescription) {
		 //Insert your code here 
        String[] trx = trxDescription.split(" ");
        String[] const1 = TRANSFER_TO_CONSTANT.split(" ");
        String[] const2 = TRANSFER_FROM_CONSTANT.split(" ");
        int index = 0;
        int sizeSoFar = 0;
        for(int i = 0; i < trx.length; i++){
            if(trx[i].equals(const1[0])){
                index = i+1;
                break;
            }
            sizeSoFar += trx[i].length() + 1;
        }
        int offset = 0;
        if(trx[index].substring(0, 3).equals(const1[1])){
            offset = 2;
        if(trx[index].substring(0, 5).equals(const2[1]))
        {
            offset = 4;
            
        }
        }
        String res = "";
            char[] arr = trxDescription.toCharArray();
            int size = trxDescription.length() - sizeSoFar + 1;
            System.out.println(size);
            System.out.println(sizeSoFar);
            if(size > 20){
                String[] z = trxDescription.substring(sizeSoFar+offset, trxDescription.length()).split(" ");
                if(z.length == 1){
                    if(offset == 2)
                        res = "transfer to "+z[0].substring(0,7);
                    if(offset == 4)
                    res = "transfer from "+z[0].substring(0,9);

                }
                else{
                    if(offset == 2)
                    {
                        res += "transfer to ";
                        for(int i = sizeSoFar + offset + 9; i < trxDescription.length(); i++){
                            res+= trxDescription.charAt(i);
                        }
                    }
                }
            }
            
        
        return res;
	}
}
