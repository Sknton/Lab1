import java.util.regex.*;
import java.util.*;
public class StringCalculator {
    public static String delim(String numbers){
        String det_str = "";
        int count = 0;
        Set<String> DelimLst = new HashSet<>();
        String reg = "(?<delims>[+$\\-!\\(\\){}\\[\\]^\"~*?:\\\\.]|[&\\|])";
        while (numbers.charAt(count) != '\n') {
            det_str += numbers.charAt(count);
            count++;
        }
        Pattern pat = Pattern.compile("(?<=\\[)[^]]+");
        Matcher mat = pat.matcher(det_str);
        while (mat.find()) {
            String delimiter = mat.group().replaceAll(reg, "\\\\${delims}");
            DelimLst.add(delimiter);
        }
        if (det_str.charAt(2)!='[') {
            det_str = det_str.substring(2, det_str.length());
            det_str = det_str.replaceAll(reg, "\\\\${delims}");
            DelimLst.add(det_str);
        }
        List<String> DelimList = new ArrayList<>(DelimLst);
        Comparator<String> c = new Comparator<>()
        {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        };
        DelimList.sort(Comparator.comparingInt(String::length));
        Collections.reverse(DelimList);
        String del = String.join("|", DelimList);
        return del;
    }
    public int add(String numbers){
        int sum=0, ind=0;
        String delimiter = ",|\n", without_delim = "", neg_int = "Список містить такі негативні числа:";
        String[] number_s;
        if (numbers.charAt(0)=='/' & numbers.charAt(1)=='/'){
            if (numbers.charAt(2)=='['){
                delimiter += "|" + delim(numbers);
                for (int k=0; k<numbers.length(); k++){
                    if (numbers.charAt(k)=='\n'){
                        without_delim=numbers.substring(k+1);
                        break;
                    }
                }
                number_s=without_delim.split(delimiter);
            } else {
                delimiter += "|" + delim(numbers);
                for (int j = 4; j<numbers.length(); j++){
                    without_delim += numbers.charAt(j);
                }
                number_s = without_delim.split(delimiter);
            }

        }else{
            number_s = numbers.split(delimiter);
        }
        for (int i = 0; i < number_s.length; i++){
            try{
                Integer.parseInt(numbers.substring(numbers.length()-1));
            } catch (NumberFormatException e){
                System.err.println("Роздільник не може бути в кінці");
                break;
            }
            if (number_s.length==1 & number_s[i]==""){
                break;
            }
            if (Integer.parseInt(number_s[i])<0){
                ind =1;
                neg_int+=number_s[i];
                continue;
            }
            if (Integer.parseInt(number_s[i])>1000){
                continue;
            }
            sum+=Integer.parseInt(number_s[i]);
        }
        if (ind==1){
            try{
                Integer.parseInt(neg_int);
            } catch (NumberFormatException ex){
                System.err.println(neg_int);
                return 0;
            }
        }
        return  sum;
    }


    public static void main(String[] args) {
        String str = "//[?][???][2]\n1?6???121";
        StringCalculator stringCalculator = new StringCalculator();
        int sum = stringCalculator.add(str);
        System.out.println(sum);
    }
}
